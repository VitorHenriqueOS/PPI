package servelet;

import db.Conector;
import model.Reserva;
<<<<<<< HEAD
import model.Quarto;
import model.Categoria;

=======
>>>>>>> 462dee34ddf02cabdd4d76dbd8a9eed41cac3b14
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
<<<<<<< HEAD
import java.util.HashMap;
import java.util.List;
import java.util.Map;

=======
import java.util.List;
>>>>>>> 462dee34ddf02cabdd4d76dbd8a9eed41cac3b14
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ReservaServlet")
public class ReservaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
<<<<<<< HEAD
        // --- PARTE 1: Busca de Reservas ---
        // Filtro de data removido conforme solicitado
        String buscaCpf = request.getParameter("buscaCpf");
        String buscaQuarto = request.getParameter("buscaQuarto");
        
        List<Reserva> listaReservas = new ArrayList<>();
        
        // --- PARTE 2: Listas para popular o Formulário de Cadastro ---
        List<Quarto> listaQuartos = new ArrayList<>();
        Map<Integer, Categoria> mapCategorias = new HashMap<>();
        
        try (Connection conn = new Conector().getConexao()) {
            
            // 1. Query principal de Reservas
            StringBuilder sql = new StringBuilder("SELECT * FROM Reserva WHERE 1=1");
            List<Object> params = new ArrayList<>();

            // Lógica de filtro por data removida
            
            if (buscaCpf != null && !buscaCpf.trim().isEmpty()) {
                sql.append(" AND Cpf LIKE ?");
                params.add("%" + buscaCpf.trim() + "%");
            }
            if (buscaQuarto != null && !buscaQuarto.isEmpty()) {
                sql.append(" AND Numero = ?");
                params.add(Integer.parseInt(buscaQuarto));
            }

            PreparedStatement ps = conn.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Reserva r = new Reserva();
                r.setId(rs.getInt("ID"));
                r.setDataInicio(rs.getDate("dataInicio"));
                r.setDataFim(rs.getDate("datafim"));
                r.setCpf(rs.getString("Cpf"));
                r.setNumero(rs.getInt("Numero"));
                listaReservas.add(r);
            }

            // 2. Buscar Categorias e mapear por ID
            String sqlCat = "SELECT * FROM Categoria";
            PreparedStatement psCat = conn.prepareStatement(sqlCat);
            ResultSet rsCat = psCat.executeQuery();
            while (rsCat.next()) {
                Categoria c = new Categoria();
                c.setId(rsCat.getInt("id"));
                c.setNome(rsCat.getString("nome"));
                c.setPreco(rsCat.getBigDecimal("preco"));
                c.setCapacidade(rsCat.getInt("capacidade"));
                c.setTipoCama(rsCat.getString("tipo_cama"));
                mapCategorias.put(c.getId(), c);
            }

            // 3. Buscar todos os Quartos
            String sqlQuartos = "SELECT * FROM Quarto ORDER BY Numero";
            PreparedStatement psQ = conn.prepareStatement(sqlQuartos);
            ResultSet rsQ = psQ.executeQuery();
            while (rsQ.next()) {
                Quarto q = new Quarto();
                q.setNumero(rsQ.getInt("Numero"));
                q.setAndar(rsQ.getInt("andar"));
                q.setStatus(rsQ.getString("status"));
                q.setIdCategoria(rsQ.getInt("ID"));
                listaQuartos.add(q);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erro", "Erro: " + e.getMessage());
            request.getRequestDispatcher("erro.jsp").forward(request, response);
            return;
        }
        
        request.setAttribute("listaReservas", listaReservas);
        request.setAttribute("listaQuartos", listaQuartos);
        request.setAttribute("mapCategorias", mapCategorias);
        
        RequestDispatcher rd = request.getRequestDispatcher("/Reserva.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        
        String acao = request.getParameter("acao");
        String idStr = request.getParameter("id");
        String dtIni = request.getParameter("dataInicio");
        String dtFim = request.getParameter("dataFim");
        String cpf = request.getParameter("cpf");
        String numStr = request.getParameter("numero");
        
        String msgSucesso = null;

        try (Connection conn = new Conector().getConexao()) {
            if ("remover".equals(acao)) {
                PreparedStatement ps = conn.prepareStatement("DELETE FROM Reserva WHERE ID = ?");
                ps.setInt(1, Integer.parseInt(idStr));
                ps.executeUpdate();
                msgSucesso = "Reserva cancelada!";
            } else {
                int id = Integer.parseInt(idStr);
                Date dataInicio = Date.valueOf(dtIni);
                Date dataFim = Date.valueOf(dtFim);
                int numero = Integer.parseInt(numStr);

                // --- VALIDAÇÃO 1: Verificar se Hóspede está Ativo ---
                if ("cadastrar".equals(acao)) {
                    String sqlCheckUser = "SELECT Ativo FROM Hospede WHERE Cpf = ?";
                    PreparedStatement psUser = conn.prepareStatement(sqlCheckUser);
                    psUser.setString(1, cpf);
                    ResultSet rsUser = psUser.executeQuery();
                    
                    if (rsUser.next()) {
                        boolean isAtivo = rsUser.getBoolean("Ativo");
                        if (!isAtivo) {
                            throw new Exception("Hóspede inativo. Não é possível realizar novas reservas.");
                        }
                    } else {
                        throw new Exception("CPF do hóspede não encontrado.");
                    }
                }

                // --- VALIDAÇÃO 2: Verificar Colisão de Datas ---
                StringBuilder sqlColisao = new StringBuilder();
                sqlColisao.append("SELECT COUNT(*) FROM Reserva WHERE Numero = ? ");
                sqlColisao.append("AND (dataInicio < ? AND datafim > ?) ");
                
                if ("alterar".equals(acao)) {
                    sqlColisao.append("AND ID != ?");
                }

                PreparedStatement psColisao = conn.prepareStatement(sqlColisao.toString());
                psColisao.setInt(1, numero);
                psColisao.setDate(2, dataFim);
                psColisao.setDate(3, dataInicio);
                
                if ("alterar".equals(acao)) {
                    psColisao.setInt(4, id);
                }

                ResultSet rsColisao = psColisao.executeQuery();
                if (rsColisao.next() && rsColisao.getInt(1) > 0) {
                    throw new Exception("Conflito de datas: O quarto " + numero + " já está reservado neste período.");
                }

                if ("cadastrar".equals(acao)) {
                    String sql = "INSERT INTO Reserva (ID, dataInicio, datafim, Cpf, Numero) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setInt(1, id);
                    ps.setDate(2, dataInicio);
                    ps.setDate(3, dataFim);
                    ps.setString(4, cpf);
                    ps.setInt(5, numero);
                    ps.executeUpdate();
                    msgSucesso = "Reserva cadastrada!";
                } else if ("alterar".equals(acao)) {
                    String sql = "UPDATE Reserva SET dataInicio=?, datafim=?, Cpf=?, Numero=? WHERE ID=?";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setDate(1, dataInicio);
                    ps.setDate(2, dataFim);
                    ps.setString(3, cpf);
                    ps.setInt(4, numero);
                    ps.setInt(5, id);
                    ps.executeUpdate();
                    msgSucesso = "Reserva atualizada!";
                }
            }
        } catch (Exception e) {
            request.setAttribute("erro", "Erro ao processar: " + e.getMessage());
            request.getRequestDispatcher("erro.jsp").forward(request, response);
            return;
        }
=======
        String buscaId = request.getParameter("buscaId");
        List<Reserva> lista = new ArrayList<>();
        
        try (Connection conn = new Conector().getConexao()) {
            String sql;
            PreparedStatement ps;
            
            if (buscaId != null && !buscaId.isEmpty()) {
                sql = "SELECT * FROM Reserva WHERE ID = ?";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(buscaId));
            } else {
                sql = "SELECT * FROM Reserva";
                ps = conn.prepareStatement(sql);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Reserva r = new Reserva();
                r.setId(rs.getInt("ID"));
                r.setDataInicio(rs.getDate("dataInicio"));
                r.setDataFim(rs.getDate("datafim"));
                r.setCpf(rs.getString("Cpf"));
                r.setNumero(rs.getInt("Numero"));
                lista.add(r);
            }
        } catch (Exception e) {
            request.setAttribute("erro", "Erro: " + e.getMessage());
            request.getRequestDispatcher("erro.jsp").forward(request, response);
            return;
        }
        
        request.setAttribute("listaReservas", lista);
        RequestDispatcher rd = request.getRequestDispatcher("/Reserva.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        
        String acao = request.getParameter("acao");
        String idStr = request.getParameter("id");
        String dtIni = request.getParameter("dataInicio");
        String dtFim = request.getParameter("dataFim");
        String cpf = request.getParameter("cpf");
        String numStr = request.getParameter("numero");
        
        String msgSucesso = null;

        try (Connection conn = new Conector().getConexao()) {
            if ("remover".equals(acao)) {
                PreparedStatement ps = conn.prepareStatement("DELETE FROM Reserva WHERE ID = ?");
                ps.setInt(1, Integer.parseInt(idStr));
                ps.executeUpdate();
                msgSucesso = "Reserva cancelada!";
            } else {
                int id = Integer.parseInt(idStr);
                Date dataInicio = Date.valueOf(dtIni);
                Date dataFim = Date.valueOf(dtFim);
                int numero = Integer.parseInt(numStr);

                if ("cadastrar".equals(acao)) {
                    String sql = "INSERT INTO Reserva (ID, dataInicio, datafim, Cpf, Numero) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setInt(1, id);
                    ps.setDate(2, dataInicio);
                    ps.setDate(3, dataFim);
                    ps.setString(4, cpf);
                    ps.setInt(5, numero);
                    ps.executeUpdate();
                    msgSucesso = "Reserva cadastrada!";
                } else if ("alterar".equals(acao)) {
                    String sql = "UPDATE Reserva SET dataInicio=?, datafim=?, Cpf=?, Numero=? WHERE ID=?";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setDate(1, dataInicio);
                    ps.setDate(2, dataFim);
                    ps.setString(3, cpf);
                    ps.setInt(4, numero);
                    ps.setInt(5, id);
                    ps.executeUpdate();
                    msgSucesso = "Reserva atualizada!";
                }
            }
        } catch (Exception e) {
            request.setAttribute("erro", "Erro ao processar: " + e.getMessage());
            request.getRequestDispatcher("erro.jsp").forward(request, response);
            return;
        }
>>>>>>> 462dee34ddf02cabdd4d76dbd8a9eed41cac3b14
        request.setAttribute("msgSucesso", msgSucesso);
        doGet(request, response);
    }
}