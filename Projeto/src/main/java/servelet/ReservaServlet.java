package servelet;

import db.Conector;
import model.Reserva;
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

@WebServlet("/ReservaServlet")
public class ReservaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
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
        request.setAttribute("msgSucesso", msgSucesso);
        doGet(request, response);
    }
}