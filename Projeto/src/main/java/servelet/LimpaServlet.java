package servelet;

import db.Conector;
import model.Limpa;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
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

@WebServlet("/LimpaServlet")
public class LimpaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
<<<<<<< HEAD
=======
        // Filtros opcionais conforme HTML original
>>>>>>> 462dee34ddf02cabdd4d76dbd8a9eed41cac3b14
        String buscaData = request.getParameter("buscaData");
        String buscaQuarto = request.getParameter("buscaQuarto");
        
        List<Limpa> lista = new ArrayList<>();
        
        try (Connection conn = new Conector().getConexao()) {
            StringBuilder sql = new StringBuilder("SELECT * FROM Limpa WHERE 1=1");
<<<<<<< HEAD
            List<Object> params = new ArrayList<>();
            
            // CORREÇÃO: Utilizando PreparedStatement para garantir que a data seja interpretada corretamente pelo banco
            if (buscaData != null && !buscaData.isEmpty()) {
                sql.append(" AND data = ?");
                params.add(Date.valueOf(buscaData));
=======
            
            if (buscaData != null && !buscaData.isEmpty()) {
                sql.append(" AND data = '").append(buscaData).append("'");
>>>>>>> 462dee34ddf02cabdd4d76dbd8a9eed41cac3b14
            }
            if (buscaQuarto != null && !buscaQuarto.isEmpty()) {
                sql.append(" AND Numero = ").append(buscaQuarto);
            }

            PreparedStatement ps = conn.prepareStatement(sql.toString());
            ResultSet rs = ps.executeQuery();
            
<<<<<<< HEAD
            if (buscaQuarto != null && !buscaQuarto.isEmpty()) {
                sql.append(" AND Numero = ?");
                params.add(Integer.parseInt(buscaQuarto));
            }

            PreparedStatement ps = conn.prepareStatement(sql.toString());
            
            // Define os valores dos ?
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();
            
=======
>>>>>>> 462dee34ddf02cabdd4d76dbd8a9eed41cac3b14
            while (rs.next()) {
                Limpa l = new Limpa();
                l.setId(rs.getInt("ID"));
                l.setData(rs.getDate("data"));
                l.setObs(rs.getString("obs"));
                l.setNumeroQuarto(rs.getInt("Numero"));
                l.setIdFuncionario(rs.getInt("IDF"));
                lista.add(l);
            }
        } catch (Exception e) {
            request.setAttribute("erro", "Erro: " + e.getMessage());
            request.getRequestDispatcher("erro.jsp").forward(request, response);
            return;
        }
        
        request.setAttribute("listaLimpeza", lista);
        RequestDispatcher rd = request.getRequestDispatcher("/Limpa.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String acao = request.getParameter("acao");
        String idStr = request.getParameter("id");
        
        try (Connection conn = new Conector().getConexao()) {
            if ("remover".equals(acao)) {
                PreparedStatement ps = conn.prepareStatement("DELETE FROM Limpa WHERE ID = ?");
                ps.setInt(1, Integer.parseInt(idStr));
                ps.executeUpdate();
                request.setAttribute("msgSucesso", "Registro de limpeza removido!");
            } else {
                int id = Integer.parseInt(idStr);
                Date data = Date.valueOf(request.getParameter("data"));
                String obs = request.getParameter("obs");
                int numero = Integer.parseInt(request.getParameter("numero"));
                int idFunc = Integer.parseInt(request.getParameter("idFuncionario"));

                if ("cadastrar".equals(acao)) {
                    String sql = "INSERT INTO Limpa (ID, data, obs, Numero, IDF) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setInt(1, id);
                    ps.setDate(2, data);
                    ps.setString(3, obs);
                    ps.setInt(4, numero);
                    ps.setInt(5, idFunc);
                    ps.executeUpdate();
                    request.setAttribute("msgSucesso", "Limpeza registrada!");
                } else if ("alterar".equals(acao)) {
                    String sql = "UPDATE Limpa SET data=?, obs=?, Numero=?, IDF=? WHERE ID=?";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setDate(1, data);
                    ps.setString(2, obs);
                    ps.setInt(3, numero);
                    ps.setInt(4, idFunc);
                    ps.setInt(5, id);
                    ps.executeUpdate();
                    request.setAttribute("msgSucesso", "Registro atualizado!");
                }
            }
        } catch (Exception e) {
            request.setAttribute("erro", "Erro: " + e.getMessage());
            request.getRequestDispatcher("erro.jsp").forward(request, response);
            return;
        }
        doGet(request, response);
    }
}