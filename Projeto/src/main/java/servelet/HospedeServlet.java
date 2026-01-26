package servelet;

import db.Conector;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/HospedeServlet")
public class HospedeServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String acao = request.getParameter("acao");

        try (Connection conn = new Conector().getConexao()) {

            /* ================= CADASTRAR ================= */
            if ("cadastrar".equals(acao)) {

                String sql = "INSERT INTO Hospede VALUES (?, ?, ?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, request.getParameter("cpf"));
                ps.setString(2, request.getParameter("nome"));
                ps.setString(3, request.getParameter("email"));
                ps.setString(4, request.getParameter("telefone"));
                ps.setDate(5, Date.valueOf(request.getParameter("dataNascimento")));
                ps.executeUpdate();

                out.print("{\"ok\":true}");

            /* ================= CONSULTAR ================= */
            } else if ("consultar".equals(acao)) {

                String cpf = request.getParameter("cpf");
                PreparedStatement ps;

                if (cpf == null || cpf.trim().isEmpty()) {
                    ps = conn.prepareStatement("SELECT * FROM Hospede");
                } else {
                    ps = conn.prepareStatement("SELECT * FROM Hospede WHERE Cpf=?");
                    ps.setString(1, cpf);
                }

                ResultSet rs = ps.executeQuery();
                StringBuilder json = new StringBuilder();
                json.append("[");

                boolean primeiro = true;
                while (rs.next()) {
                    if (!primeiro) json.append(",");
                    primeiro = false;

                    json.append("{");
                    json.append("\"cpf\":\"").append(rs.getString("Cpf")).append("\",");
                    json.append("\"nome\":\"").append(rs.getString("Nome")).append("\",");
                    json.append("\"email\":\"").append(rs.getString("Email")).append("\",");
                    json.append("\"telefone\":\"").append(rs.getString("Telefone")).append("\",");
                    json.append("\"data\":\"").append(rs.getDate("Data_nascimento")).append("\"");
                    json.append("}");
                }

                json.append("]");
                out.print(json.toString());

            /* ================= ALTERAR ================= */
            } else if ("alterar".equals(acao)) {

                String sql = "UPDATE Hospede SET Nome=?, Email=?, Telefone=?, Data_nascimento=? WHERE Cpf=?";
                PreparedStatement ps = conn.prepareStatement(sql);

                ps.setString(1, request.getParameter("nome"));
                ps.setString(2, request.getParameter("email"));
                ps.setString(3, request.getParameter("telefone"));
                ps.setDate(4, Date.valueOf(request.getParameter("dataNascimento")));
                ps.setString(5, request.getParameter("cpf"));

                ps.executeUpdate();
                out.print("{\"ok\":true}");

            /* ================= REMOVER ================= */
            } else if ("remover".equals(acao)) {

                String sql = "DELETE FROM Hospede WHERE Cpf=?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, request.getParameter("cpf"));
                ps.executeUpdate();

                out.print("{\"ok\":true}");
            }

        } catch (Exception e) {
            out.print("{\"erro\":\"" + e.getMessage().replace("\"", "") + "\"}");
            e.printStackTrace();
        }
    }
}
