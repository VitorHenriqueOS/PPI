package servelet;

import db.Conector;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/CategoriaServlet")
public class CategoriaServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String acao = request.getParameter("acao");

        try (Connection conn = new Conector().getConexao()) {
            
            /* ================= CADASTRAR ================= */
            if ("cadastrar".equals(acao)) {
                String sql = "INSERT INTO Categoria (ID, preco, nome, capacidade, tipo_cama) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(request.getParameter("id")));
                ps.setDouble(2, Double.parseDouble(request.getParameter("preco")));
                ps.setString(3, request.getParameter("nome"));
                ps.setInt(4, Integer.parseInt(request.getParameter("capacidade")));
                ps.setString(5, request.getParameter("tipo_cama"));
                
                ps.executeUpdate();
                out.print("{\"ok\":true}");
                
            /* ================= CONSULTAR ================= */
            } else if ("consultar".equals(acao)) {
                String idStr = request.getParameter("id");
                String sql;
                PreparedStatement ps;

                if (idStr == null || idStr.trim().isEmpty()) {
                    sql = "SELECT * FROM Categoria";
                    ps = conn.prepareStatement(sql);
                } else {
                    sql = "SELECT * FROM Categoria WHERE ID = ?";
                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, Integer.parseInt(idStr));
                }

                ResultSet rs = ps.executeQuery();
                
                StringBuilder json = new StringBuilder();
                json.append("[");
                
                boolean primeiro = true;
                while (rs.next()) {
                    if (!primeiro) json.append(",");
                    primeiro = false;
                    
                    json.append("{");
                    json.append("\"id\":").append(rs.getInt("ID")).append(",");
                    json.append("\"nome\":\"").append(rs.getString("nome")).append("\",");
                    json.append("\"preco\":").append(rs.getDouble("preco")).append(",");
                    json.append("\"capacidade\":").append(rs.getInt("capacidade")).append(",");
                    json.append("\"tipo_cama\":\"").append(rs.getString("tipo_cama")).append("\"");
                    json.append("}");
                }
                
                json.append("]");
                out.print(json.toString());

            /* ================= ALTERAR ================= */
            } else if ("alterar".equals(acao)) {
                
                String sql = "UPDATE Categoria SET preco = ?, nome = ?, capacidade = ?, tipo_cama = ? WHERE ID = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setDouble(1, Double.parseDouble(request.getParameter("preco")));
                ps.setString(2, request.getParameter("nome"));
                ps.setInt(3, Integer.parseInt(request.getParameter("capacidade")));
                ps.setString(4, request.getParameter("tipo_cama"));
                ps.setInt(5, Integer.parseInt(request.getParameter("id")));
                
                ps.executeUpdate();
                out.print("{\"ok\":true}");
                    
            /* ================= REMOVER ================= */
            } else if ("remover".equals(acao)) {
                String sql = "DELETE FROM Categoria WHERE ID = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(request.getParameter("id")));
                
                ps.executeUpdate();
                out.print("{\"ok\":true}");
            }
            
        } catch (Exception e) {
            String msgErro = e.getMessage();
            
            // Tratamento básico de erro de Foreign Key
            if (msgErro != null && msgErro.contains("foreign key constraint fails")) {
                msgErro = "Não é possível excluir: Existem quartos vinculados a esta categoria.";
            }
            
            if (msgErro != null) {
                msgErro = msgErro.replace("\"", "'");
            }
            
            out.print("{\"erro\":\"" + msgErro + "\"}");
            e.printStackTrace();
        }
    }
}