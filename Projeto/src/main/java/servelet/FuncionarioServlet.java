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

@WebServlet("/FuncionarioServlet")
public class FuncionarioServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Define o retorno como JSON
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String acao = request.getParameter("acao");

        try (Connection conn = new Conector().getConexao()) {
            
            /* ================= CADASTRAR ================= */
            if ("cadastrar".equals(acao)) {
                String sql = "INSERT INTO Funcionario (ID, Nome, Turno) VALUES (?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(request.getParameter("id")));
                ps.setString(2, request.getParameter("nome"));
                ps.setString(3, request.getParameter("turno"));
                
                ps.executeUpdate();
                out.print("{\"ok\":true}");
                
            /* ================= CONSULTAR ================= */
            } else if ("consultar".equals(acao)) {
                String idStr = request.getParameter("id");
                String sql;
                PreparedStatement ps;

                if (idStr == null || idStr.trim().isEmpty()) {
                    sql = "SELECT * FROM Funcionario";
                    ps = conn.prepareStatement(sql);
                } else {
                    sql = "SELECT * FROM Funcionario WHERE ID = ?";
                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, Integer.parseInt(idStr));
                }

                ResultSet rs = ps.executeQuery();
                
                // Construção manual do JSON array
                StringBuilder json = new StringBuilder();
                json.append("[");
                
                boolean primeiro = true;
                while (rs.next()) {
                    if (!primeiro) json.append(",");
                    primeiro = false;
                    
                    json.append("{");
                    json.append("\"id\":").append(rs.getInt("ID")).append(",");
                    json.append("\"nome\":\"").append(rs.getString("Nome")).append("\",");
                    json.append("\"turno\":\"").append(rs.getString("Turno")).append("\"");
                    json.append("}");
                }
                
                json.append("]");
                out.print(json.toString());

            /* ================= ALTERAR ================= */
            } else if ("alterar".equals(acao)) {
                
                // Atualiza todos os campos baseados no ID
                String sql = "UPDATE Funcionario SET Nome = ?, Turno = ? WHERE ID = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, request.getParameter("nome"));
                ps.setString(2, request.getParameter("turno"));
                ps.setInt(3, Integer.parseInt(request.getParameter("id")));
                
                ps.executeUpdate();
                out.print("{\"ok\":true}");
                    
            /* ================= REMOVER ================= */
            } else if ("remover".equals(acao)) {
                String sql = "DELETE FROM Funcionario WHERE ID = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(request.getParameter("id")));
                
                ps.executeUpdate();
                out.print("{\"ok\":true}");
            }
            
        } catch (Exception e) {
            String msgErro = e.getMessage();
            
            // Tratamento simples para aspas no JSON
            if (msgErro != null) {
                msgErro = msgErro.replace("\"", "'");
            }
            
            out.print("{\"erro\":\"" + msgErro + "\"}");
            e.printStackTrace();
        }
    }
}