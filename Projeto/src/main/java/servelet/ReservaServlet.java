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

@WebServlet("/ReservaServlet")
public class ReservaServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String acao = request.getParameter("acao");

        try (Connection conn = new Conector().getConexao()) {
            
            /* ================= CADASTRAR ================= */
            if ("cadastrar".equals(acao)) {
                int id = Integer.parseInt(request.getParameter("id"));
                Date dataInicio = Date.valueOf(request.getParameter("dataInicio"));
                Date dataFim = Date.valueOf(request.getParameter("dataFim"));
                int numero = Integer.parseInt(request.getParameter("numero"));
                String cpf = request.getParameter("cpf");

                // Validação de disponibilidade (Overlapping dates)
                String sqlCheck = "SELECT COUNT(*) FROM Reserva WHERE Numero = ? AND dataInicio <= ? AND datafim >= ?";
                PreparedStatement psCheck = conn.prepareStatement(sqlCheck);
                psCheck.setInt(1, numero);
                psCheck.setDate(2, dataFim);
                psCheck.setDate(3, dataInicio);
                ResultSet rsCheck = psCheck.executeQuery();
                rsCheck.next();
                
                if (rsCheck.getInt(1) > 0) {
                    out.print("{\"erro\":\"O quarto " + numero + " já está reservado para este período.\"}");
                    return;
                }

                String sql = "INSERT INTO Reserva (ID, dataInicio, datafim, Cpf, Numero) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, id);
                ps.setDate(2, dataInicio);
                ps.setDate(3, dataFim);
                ps.setString(4, cpf);
                ps.setInt(5, numero);
                
                ps.executeUpdate();
                out.print("{\"ok\":true}");
                
            /* ================= CONSULTAR ================= */
            } else if ("consultar".equals(acao)) {
                String idStr = request.getParameter("id");
                String dataIniStr = request.getParameter("dataInicio");
                String cpfStr = request.getParameter("cpf");
                
                StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM Reserva WHERE 1=1");
                
                if (idStr != null && !idStr.trim().isEmpty()) {
                    sqlBuilder.append(" AND ID = ?");
                }
                if (dataIniStr != null && !dataIniStr.trim().isEmpty()) {
                    sqlBuilder.append(" AND dataInicio = ?");
                }
                if (cpfStr != null && !cpfStr.trim().isEmpty()) {
                    sqlBuilder.append(" AND Cpf LIKE ?");
                }
                
                PreparedStatement ps = conn.prepareStatement(sqlBuilder.toString());
                int index = 1;
                
                if (idStr != null && !idStr.trim().isEmpty()) {
                    ps.setInt(index++, Integer.parseInt(idStr));
                }
                if (dataIniStr != null && !dataIniStr.trim().isEmpty()) {
                    ps.setDate(index++, Date.valueOf(dataIniStr));
                }
                if (cpfStr != null && !cpfStr.trim().isEmpty()) {
                    ps.setString(index++, "%" + cpfStr + "%");
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
                    json.append("\"dataInicio\":\"").append(rs.getString("dataInicio")).append("\",");
                    json.append("\"dataFim\":\"").append(rs.getString("datafim")).append("\",");
                    json.append("\"cpf\":\"").append(rs.getString("Cpf")).append("\",");
                    json.append("\"numero\":").append(rs.getInt("Numero"));
                    json.append("}");
                }
                
                json.append("]");
                out.print(json.toString());

            /* ================= ALTERAR ================= */
            } else if ("alterar".equals(acao)) {
                int id = Integer.parseInt(request.getParameter("id"));
                Date dataInicio = Date.valueOf(request.getParameter("dataInicio"));
                Date dataFim = Date.valueOf(request.getParameter("dataFim"));
                int numero = Integer.parseInt(request.getParameter("numero"));
                String cpf = request.getParameter("cpf");

                // Validação de disponibilidade ignorando a própria reserva que está a ser editada
                String sqlCheck = "SELECT COUNT(*) FROM Reserva WHERE Numero = ? AND dataInicio <= ? AND datafim >= ? AND ID <> ?";
                PreparedStatement psCheck = conn.prepareStatement(sqlCheck);
                psCheck.setInt(1, numero);
                psCheck.setDate(2, dataFim);
                psCheck.setDate(3, dataInicio);
                psCheck.setInt(4, id);
                ResultSet rsCheck = psCheck.executeQuery();
                rsCheck.next();
                
                if (rsCheck.getInt(1) > 0) {
                    out.print("{\"erro\":\"Conflito de datas: O quarto " + numero + " já está ocupado nestas datas por outra reserva.\"}");
                    return;
                }

                String sql = "UPDATE Reserva SET dataInicio = ?, datafim = ?, Cpf = ?, Numero = ? WHERE ID = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setDate(1, dataInicio);
                ps.setDate(2, dataFim);
                ps.setString(3, cpf);
                ps.setInt(4, numero);
                ps.setInt(5, id); 
                
                int rows = ps.executeUpdate();
                if(rows > 0) out.print("{\"ok\":true}");
                else out.print("{\"erro\":\"Reserva não encontrada (ID inválido).\"}");
                    
            /* ================= REMOVER ================= */
            } else if ("remover".equals(acao)) {
                String sql = "DELETE FROM Reserva WHERE ID = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(request.getParameter("id")));
                
                int rows = ps.executeUpdate();
                if(rows > 0) out.print("{\"ok\":true}");
                else out.print("{\"erro\":\"Reserva não encontrada.\"}");
            }
            
        } catch (Exception e) {
            String msgErro = e.getMessage();
            if (msgErro != null) {
                if (msgErro.contains("foreign key")) {
                    msgErro = "Operação negada: Verifique se o CPF do Hóspede e o Número do Quarto existem.";
                } else if (msgErro.contains("Duplicate entry") || msgErro.contains("PRIMARY")) {
                    msgErro = "Erro de duplicidade: Já existe uma reserva com este ID ou combinação de CPF/Quarto.";
                }
                msgErro = msgErro.replace("\"", "'");
            }
            out.print("{\"erro\":\"" + msgErro + "\"}");
            e.printStackTrace();
        }
    }
}