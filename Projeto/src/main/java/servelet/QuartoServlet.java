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

@WebServlet("/QuartoServlet")
public class QuartoServlet extends HttpServlet {

    @Override
    public void init() {
        System.out.println("Servlet iniciado!");
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String acao = request.getParameter("acao");

        try (Connection conn = new Conector().getConexao()) {
            
            /* ================= CADASTRAR ================= */
            if ("cadastrar".equals(acao)) {
                String sql = "INSERT INTO Quarto (Numero, andar, status, ID) VALUES (?, ?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(request.getParameter("numero")));
                ps.setInt(2, Integer.parseInt(request.getParameter("andar")));
                ps.setString(3, request.getParameter("status"));
                ps.setInt(4, Integer.parseInt(request.getParameter("idCategoria")));
                
                ps.executeUpdate();
                out.print("{\"ok\":true}");
                
            /* ================= CONSULTAR ================= */
            } else if ("consultar".equals(acao)) {
                String numStr = request.getParameter("numero");
                String sql;
                PreparedStatement ps;

                if (numStr == null || numStr.trim().isEmpty()) {
                    sql = "SELECT * FROM Quarto";
                    ps = conn.prepareStatement(sql);
                } else {
                    sql = "SELECT * FROM Quarto WHERE Numero = ?";
                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, Integer.parseInt(numStr));
                }

                ResultSet rs = ps.executeQuery();
                
                StringBuilder json = new StringBuilder();
                json.append("[");
                
                boolean primeiro = true;
                while (rs.next()) {
                    if (!primeiro) json.append(",");
                    primeiro = false;
                    
                    json.append("{");
                    json.append("\"numero\":").append(rs.getInt("Numero")).append(",");
                    json.append("\"andar\":").append(rs.getInt("andar")).append(",");
                    json.append("\"status\":\"").append(rs.getString("status")).append("\",");
                    json.append("\"idCategoria\":").append(rs.getInt("ID"));
                    json.append("}");
                }
                
                json.append("]");
                out.print(json.toString());

            /* ================= ALTERAR ================= */
            } else if ("alterar".equals(acao)) {
                
                String sql = "UPDATE Quarto SET andar = ?, status = ?, ID = ? WHERE Numero = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(request.getParameter("andar")));
                ps.setString(2, request.getParameter("status"));
                ps.setInt(3, Integer.parseInt(request.getParameter("idCategoria")));
                ps.setInt(4, Integer.parseInt(request.getParameter("numero")));
                
                ps.executeUpdate();
                out.print("{\"ok\":true}");
                    
            /* ================= REMOVER ================= */
            } else if ("remover".equals(acao)) {
                String sql = "DELETE FROM Quarto WHERE Numero = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(request.getParameter("numero")));
                
                ps.executeUpdate();
                out.print("{\"ok\":true}");
            }
            
        } catch (Exception e) {
            // ===== AQUI ESTÁ A MUDANÇA =====
            String msgErro = e.getMessage();
            
            // Verificamos se a mensagem de erro contém o texto específico de chave estrangeira
            if (msgErro != null && msgErro.contains("foreign key constraint fails")) {
                msgErro = "A Categoria informada não existe! Verifique o ID.";
            }
            
            // Removemos aspas duplas da mensagem original (se houver) para não quebrar o JSON
            if (msgErro != null) {
                msgErro = msgErro.replace("\"", "'");
            }
            
            out.print("{\"erro\":\"" + msgErro + "\"}");
            e.printStackTrace();
        }
    }
    @Override
    public void destroy() {
        System.out.println("Servlet finalizado!");
    }
}