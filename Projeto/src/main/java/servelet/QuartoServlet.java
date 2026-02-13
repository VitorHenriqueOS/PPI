package servelet;

import db.Conector;
import model.Quarto;
import model.Categoria;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/QuartoServlet")
public class QuartoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String buscaNumero = request.getParameter("buscaNumero");
        List<Quarto> listaQuartos = new ArrayList<>();
        List<Categoria> listaCategorias = new ArrayList<>();
        
        try (Connection conn = new Conector().getConexao()) {
            
            // 1. Buscar lista de Categorias para o Dropdown
            String sqlCat = "SELECT * FROM Categoria";
            PreparedStatement psCat = conn.prepareStatement(sqlCat);
            ResultSet rsCat = psCat.executeQuery();
            while (rsCat.next()) {
                Categoria c = new Categoria();
                c.setId(rsCat.getInt("id"));
                c.setNome(rsCat.getString("nome"));
                // ALTERAÇÃO: Populando os demais campos para uso no Frontend
                c.setPreco(rsCat.getBigDecimal("preco"));
                c.setCapacidade(rsCat.getInt("capacidade"));
                c.setTipoCama(rsCat.getString("tipo_cama"));
                
                listaCategorias.add(c);
            }
            
            // 2. Buscar Quartos com JOIN para pegar o Nome da Categoria
            StringBuilder query = new StringBuilder("SELECT q.*, c.nome AS nomeCategoria FROM Quarto q INNER JOIN Categoria c ON q.ID = c.id");
            PreparedStatement ps;
            
            if (buscaNumero != null && !buscaNumero.trim().isEmpty()) {
                query.append(" WHERE q.Numero = ?");
                ps = conn.prepareStatement(query.toString());
                ps.setInt(1, Integer.parseInt(buscaNumero));
            } else {
                ps = conn.prepareStatement(query.toString());
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Quarto q = new Quarto();
                q.setNumero(rs.getInt("Numero"));
                q.setAndar(rs.getInt("andar"));
                q.setStatus(rs.getString("status"));
                q.setIdCategoria(rs.getInt("ID")); 
                q.setNomeCategoria(rs.getString("nomeCategoria"));
                listaQuartos.add(q);
            }
            
            if (buscaNumero != null && !buscaNumero.isEmpty() && listaQuartos.isEmpty()) {
                request.setAttribute("erro", "Nenhum quarto encontrado com o Número: " + buscaNumero);
                request.getRequestDispatcher("erro.jsp").forward(request, response);
                return;
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erro", "Erro de conexão: " + e.getMessage());
            request.getRequestDispatcher("erro.jsp").forward(request, response);
            return;
        }
        
        request.setAttribute("listaCategorias", listaCategorias);
        request.setAttribute("listaQuartos", listaQuartos);
        RequestDispatcher rd = request.getRequestDispatcher("/Quarto.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        
        String acao = request.getParameter("acao");
        String numeroStr = request.getParameter("numero");
        String andarStr = request.getParameter("andar");
        String status = request.getParameter("status");
        String idCategoriaStr = request.getParameter("idCategoria");
        
        String msgSucesso = null;

        try (Connection conn = new Conector().getConexao()) {
            
            if ("remover".equals(acao)) {
                String sql = "DELETE FROM Quarto WHERE Numero = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(numeroStr));
                ps.executeUpdate();
                msgSucesso = "Quarto removido com sucesso!";
                
            } else {
                if (numeroStr == null || numeroStr.isEmpty()) {
                    throw new Exception("Número do quarto é obrigatório.");
                }
                
                int numero = Integer.parseInt(numeroStr);
                int andar = (andarStr != null && !andarStr.isEmpty()) ? Integer.parseInt(andarStr) : 0;
                int idCategoria = (idCategoriaStr != null && !idCategoriaStr.isEmpty()) ? Integer.parseInt(idCategoriaStr) : 0;

                if ("cadastrar".equals(acao)) {
                    String sql = "INSERT INTO Quarto (Numero, andar, status, ID) VALUES (?, ?, ?, ?)";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setInt(1, numero);
                    ps.setInt(2, andar);
                    ps.setString(3, status);
                    ps.setInt(4, idCategoria);
                    ps.executeUpdate();
                    msgSucesso = "Quarto cadastrado com sucesso!";
                    
                } else if ("alterar".equals(acao)) {
                    String sql = "UPDATE Quarto SET andar = ?, status = ?, ID = ? WHERE Numero = ?";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setInt(1, andar);
                    ps.setString(2, status);
                    ps.setInt(3, idCategoria);
                    ps.setInt(4, numero);
                    ps.executeUpdate();
                    msgSucesso = "Quarto atualizado com sucesso!";
                }
            }

        } catch (Exception e) {
            String errorMsg = e.getMessage();
            if (errorMsg != null && errorMsg.contains("foreign key constraint fails")) {
                errorMsg = "A Categoria informada não existe! Verifique o ID.";
            }
            request.setAttribute("erro", "Ocorreu um erro: " + errorMsg);
            request.getRequestDispatcher("erro.jsp").forward(request, response);
            return;
        }

        request.setAttribute("msgSucesso", msgSucesso);
        doGet(request, response);
    }
}