package servelet;

import db.Conector;
import model.Funcionario;
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

@WebServlet("/FuncionarioServlet")
public class FuncionarioServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String buscaId = request.getParameter("buscaId");
        List<Funcionario> lista = new ArrayList<>();
        
        try (Connection conn = new Conector().getConexao()) {
            String sql;
            PreparedStatement ps;
            if (buscaId != null && !buscaId.trim().isEmpty()) {
                sql = "SELECT * FROM Funcionario WHERE ID = ?";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(buscaId));
            } else {
                sql = "SELECT * FROM Funcionario";
                ps = conn.prepareStatement(sql);
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Funcionario f = new Funcionario();
                f.setId(rs.getInt("ID"));
                f.setNome(rs.getString("Nome"));
                f.setTurno(rs.getString("Turno"));
                lista.add(f);
            }
            if (buscaId != null && !buscaId.isEmpty() && lista.isEmpty()) {
                request.setAttribute("erro", "Nenhum funcionário encontrado com ID: " + buscaId);
                request.getRequestDispatcher("erro.jsp").forward(request, response);
                return;
            }
        } catch (Exception e) {
            request.setAttribute("erro", "Erro: " + e.getMessage());
            request.getRequestDispatcher("erro.jsp").forward(request, response);
            return;
        }
        
        request.setAttribute("listaFuncionarios", lista);
        RequestDispatcher rd = request.getRequestDispatcher("/Funcionario.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String acao = request.getParameter("acao");
        String idStr = request.getParameter("id");
        String nome = request.getParameter("nome");
        String turno = request.getParameter("turno");
        String msgSucesso = null;

        try (Connection conn = new Conector().getConexao()) {
            if ("remover".equals(acao)) {
                String sql = "DELETE FROM Funcionario WHERE ID = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(idStr));
                ps.executeUpdate();
                msgSucesso = "Funcionário removido!";
            } else {
                int id = Integer.parseInt(idStr);
                if ("cadastrar".equals(acao)) {
                    String sql = "INSERT INTO Funcionario (ID, Nome, Turno) VALUES (?, ?, ?)";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setInt(1, id);
                    ps.setString(2, nome);
                    ps.setString(3, turno);
                    ps.executeUpdate();
                    msgSucesso = "Funcionário cadastrado!";
                } else if ("alterar".equals(acao)) {
                    String sql = "UPDATE Funcionario SET Nome = ?, Turno = ? WHERE ID = ?";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, nome);
                    ps.setString(2, turno);
                    ps.setInt(3, id);
                    ps.executeUpdate();
                    msgSucesso = "Funcionário atualizado!";
                }
            }
        } catch (Exception e) {
            request.setAttribute("erro", "Erro: " + e.getMessage());
            request.getRequestDispatcher("erro.jsp").forward(request, response);
            return;
        }
        request.setAttribute("msgSucesso", msgSucesso);
        doGet(request, response);
    }
}