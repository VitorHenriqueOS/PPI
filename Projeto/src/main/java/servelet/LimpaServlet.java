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

@WebServlet("/LimpaServlet")
public class LimpaServlet extends HttpServlet {

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
                String sql = "INSERT INTO Limpa (ID, data, obs, Numero, IDF) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(request.getParameter("idLimpa")));
                ps.setDate(2, Date.valueOf(request.getParameter("data")));
                ps.setString(3, request.getParameter("obs"));
                ps.setInt(4, Integer.parseInt(request.getParameter("numero")));
                ps.setInt(5, Integer.parseInt(request.getParameter("idFuncionario")));
                
                ps.executeUpdate();
                out.print("{\"ok\":true}");
                
            /* ================= CONSULTAR ================= */
            } else if ("consultar".equals(acao)) {
                String dataStr = request.getParameter("data");
                String numStr = request.getParameter("numero");
                String idFuncStr = request.getParameter("idFuncionario");
                
                StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM Limpa WHERE 1=1");
                
                if (dataStr != null && !dataStr.trim().isEmpty()) {
                    sqlBuilder.append(" AND data = ?");
                }
                if (numStr != null && !numStr.trim().isEmpty()) {
                    sqlBuilder.append(" AND Numero = ?");
                }
                if (idFuncStr != null && !idFuncStr.trim().isEmpty()) {
                    sqlBuilder.append(" AND IDF = ?");
                }
                
                PreparedStatement ps = conn.prepareStatement(sqlBuilder.toString());
                int index = 1;
                
                if (dataStr != null && !dataStr.trim().isEmpty()) {
                    ps.setDate(index++, Date.valueOf(dataStr));
                }
                if (numStr != null && !numStr.trim().isEmpty()) {
                    ps.setInt(index++, Integer.parseInt(numStr));
                }
                if (idFuncStr != null && !idFuncStr.trim().isEmpty()) {
                    ps.setInt(index++, Integer.parseInt(idFuncStr));
                }

                ResultSet rs = ps.executeQuery();
                
                StringBuilder json = new StringBuilder();
                json.append("[");
                
                boolean primeiro = true;
                while (rs.next()) {
                    if (!primeiro) json.append(",");
                    primeiro = false;
                    
                    json.append("{");
                    json.append("\"idLimpa\":").append(rs.getInt("ID")).append(",");
                    json.append("\"data\":\"").append(rs.getDate("data")).append("\",");
                    
                    String obs = rs.getString("obs") != null ? rs.getString("obs").replace("\"", "'") : "";
                    json.append("\"obs\":\"").append(obs).append("\",");
                    
                    json.append("\"numero\":").append(rs.getInt("Numero")).append(",");
                    json.append("\"idFuncionario\":").append(rs.getInt("IDF"));
                    json.append("}");
                }
                
                json.append("]");
                out.print(json.toString());

            /* ================= ALTERAR ================= */
            } else if ("alterar".equals(acao)) {
                // CORREÇÃO: Usando ID na cláusula WHERE
                String sql = "UPDATE Limpa SET obs = ?, data = ?, Numero = ?, IDF = ? WHERE ID = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                
                // Obs é o primeiro parâmetro
                ps.setString(1, request.getParameter("obs"));
                
                // Atualiza também os outros campos caso tenham sido liberados no front, 
                // mas o mais importante é o WHERE ID no final
                ps.setDate(2, Date.valueOf(request.getParameter("data")));
                ps.setInt(3, Integer.parseInt(request.getParameter("numero")));
                ps.setInt(4, Integer.parseInt(request.getParameter("idFuncionario")));
                
                // O ID é usado para identificar qual linha alterar
                ps.setInt(5, Integer.parseInt(request.getParameter("idLimpa")));
                
                int rows = ps.executeUpdate();
                if(rows > 0) out.print("{\"ok\":true}");
                else out.print("{\"erro\":\"Registro não encontrado para alteração (ID incorreto).\"}");
                    
            /* ================= REMOVER ================= */
            } else if ("remover".equals(acao)) {
                // CORREÇÃO: Usando ID na cláusula WHERE (muito mais seguro)
                String sql = "DELETE FROM Limpa WHERE ID = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(request.getParameter("idLimpa")));
                
                int rows = ps.executeUpdate();
                if(rows > 0) out.print("{\"ok\":true}");
                else out.print("{\"erro\":\"Registro não encontrado para remoção.\"}");
            }
            
        } catch (Exception e) {
            String msgErro = e.getMessage();
            if (msgErro != null) {
                if (msgErro.contains("foreign key")) {
                    msgErro = "Não foi possível realizar a operação: Verifique se o Quarto e o Funcionário existem.";
                } else if (msgErro.contains("Duplicate entry") || msgErro.contains("PRIMARY")) {
                    msgErro = "Já existe um registro com este ID.";
                }
                msgErro = msgErro.replace("\"", "'");
            }
            out.print("{\"erro\":\"" + (msgErro != null ? msgErro : "Erro interno") + "\"}");
            e.printStackTrace();
        }
    }
    @Override
    public void destroy() {
        System.out.println("Servlet finalizado!");
    }
}