package servelet;

import db.Conector;
import model.Categoria;
import java.io.IOException;
import java.math.BigDecimal;
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

@WebServlet("/CategoriaServlet")
public class CategoriaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String buscaId = request.getParameter("buscaId");
        List<Categoria> lista = new ArrayList<>();
        
        try (Connection conn = new Conector().getConexao()) {
            String sql;
            PreparedStatement ps;
            
            if (buscaId != null && !buscaId.trim().isEmpty()) {
                sql = "SELECT * FROM Categoria WHERE ID = ?";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(buscaId));
            } else {
                sql = "SELECT * FROM Categoria";
                ps = conn.prepareStatement(sql);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Categoria c = new Categoria();
                c.setId(rs.getInt("ID"));
                c.setPreco(rs.getBigDecimal("preco"));
                c.setNome(rs.getString("nome"));
                c.setCapacidade(rs.getInt("capacidade"));
                c.setTipoCama(rs.getString("tipo_cama"));
                lista.add(c);
            }
            
            if (buscaId != null && !buscaId.isEmpty() && lista.isEmpty()) {
                request.setAttribute("erro", "Nenhuma categoria encontrada com o ID: " + buscaId);
                request.getRequestDispatcher("erro.jsp").forward(request, response);
                return;
            }
            
        } catch (Exception e) {
            request.setAttribute("erro", "Erro de conexão: " + e.getMessage());
            request.getRequestDispatcher("erro.jsp").forward(request, response);
            return;
        }
        
        request.setAttribute("listaCategorias", lista);
        RequestDispatcher rd = request.getRequestDispatcher("/Categoria.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        
        String acao = request.getParameter("acao");
        String idStr = request.getParameter("id");
        String nome = request.getParameter("nome");
        String precoStr = request.getParameter("preco");
        String capacidadeStr = request.getParameter("capacidade");
        String tipoCama = request.getParameter("tipoCama");
        
        String msgSucesso = null;

        try (Connection conn = new Conector().getConexao()) {
            
            if ("remover".equals(acao)) {
                String sql = "DELETE FROM Categoria WHERE ID = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(idStr));
                ps.executeUpdate();
                msgSucesso = "Categoria removida com sucesso!";
                
            } else {
                if (idStr == null || idStr.isEmpty()) throw new Exception("ID é obrigatório.");
                
                int id = Integer.parseInt(idStr);
                BigDecimal preco = (precoStr != null) ? new BigDecimal(precoStr) : BigDecimal.ZERO;
                int capacidade = (capacidadeStr != null) ? Integer.parseInt(capacidadeStr) : 0;

                if ("cadastrar".equals(acao)) {
                    String sql = "INSERT INTO Categoria (ID, preco, nome, capacidade, tipo_cama) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setInt(1, id);
                    ps.setBigDecimal(2, preco);
                    ps.setString(3, nome);
                    ps.setInt(4, capacidade);
                    ps.setString(5, tipoCama);
                    ps.executeUpdate();
                    msgSucesso = "Categoria cadastrada com sucesso!";
                    
                } else if ("alterar".equals(acao)) {
                    String sql = "UPDATE Categoria SET preco = ?, nome = ?, capacidade = ?, tipo_cama = ? WHERE ID = ?";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setBigDecimal(1, preco);
                    ps.setString(2, nome);
                    ps.setInt(3, capacidade);
                    ps.setString(4, tipoCama);
                    ps.setInt(5, id);
                    ps.executeUpdate();
                    msgSucesso = "Categoria atualizada com sucesso!";
                }
            }

        } catch (Exception e) {
            request.setAttribute("erro", "Ocorreu um erro: " + e.getMessage());
            request.getRequestDispatcher("erro.jsp").forward(request, response);
            return;
        }

        request.setAttribute("msgSucesso", msgSucesso);
        doGet(request, response);
    }
}