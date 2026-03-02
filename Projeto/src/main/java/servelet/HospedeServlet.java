package servelet;

import db.Conector;
import model.Hospede;
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

@WebServlet("/HospedeServlet")
public class HospedeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String buscaCpf = request.getParameter("buscaCpf");
        List<Hospede> lista = new ArrayList<>();
        String erro = null;
        String aviso = null;

        try (Connection conn = new Conector().getConexao()) {
            String sql;
            PreparedStatement ps;
            
            if (buscaCpf != null && !buscaCpf.trim().isEmpty()) {
                // ALTERAÇÃO: Traz apenas se estiver ativo
                sql = "SELECT * FROM Hospede WHERE Cpf = ? AND Ativo = true";
                ps = conn.prepareStatement(sql);
                ps.setString(1, buscaCpf);
                
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Hospede h = new Hospede();
                    h.setCpf(rs.getString("Cpf"));
                    h.setNome(rs.getString("Nome"));
                    h.setEmail(rs.getString("Email"));
                    h.setTelefone(rs.getString("Telefone"));
                    h.setDataNascimento(rs.getDate("Data_nascimento"));
                    h.setAtivo(rs.getBoolean("Ativo")); // Mapeando o novo campo
                    lista.add(h);
                }
                if (lista.isEmpty()) {
                    request.setAttribute("erro", "Nenhum hóspede ativo encontrado com o CPF: " + buscaCpf);
                    request.getRequestDispatcher("erro.jsp").forward(request, response);
                    return; 
                }
            } else {
                // ALTERAÇÃO: Traz apenas se estiver ativo
                sql = "SELECT * FROM Hospede WHERE Ativo = true";
                ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Hospede h = new Hospede();
                    h.setCpf(rs.getString("Cpf"));
                    h.setNome(rs.getString("Nome"));
                    h.setEmail(rs.getString("Email"));
                    h.setTelefone(rs.getString("Telefone"));
                    h.setDataNascimento(rs.getDate("Data_nascimento"));
                    h.setAtivo(rs.getBoolean("Ativo")); // Mapeando o novo campo
                    lista.add(h);
                }
            }
        } catch (Exception e) {
            request.setAttribute("erro", "Erro de conexão: " + e.getMessage());
            request.getRequestDispatcher("erro.jsp").forward(request, response);
            return;
        }
        
        request.setAttribute("listaHospedes", lista);
        request.setAttribute("msgErro", erro);
        request.setAttribute("msgAviso", aviso);
        
        RequestDispatcher rd = request.getRequestDispatcher("/Hospede.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        
        String msgSucesso = null;
        String msgErro = null;

        try {
            String acao = request.getParameter("acao");
            String cpf = request.getParameter("cpf");
            String nome = request.getParameter("nome");
            String email = request.getParameter("email");
            String telefone = request.getParameter("telefone");
            String dataStr = request.getParameter("dataNascimento");
            String cpfOriginal = request.getParameter("cpfOriginal");

            try (Connection conn = new Conector().getConexao()) {
                
                if ("remover".equals(acao)) {
                    // ALTERAÇÃO: Soft Delete (Update Ativo = false)
                    String sql = "UPDATE Hospede SET Ativo = false WHERE Cpf=?";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, cpf);
                    ps.executeUpdate();
                    
                    msgSucesso = "Hóspede removido com sucesso!";
                    
                } else {
                    if (cpf == null || cpf.isEmpty()) {
                        throw new Exception("CPF é obrigatório.");
                    }

                    Date dataNascimento = null;
                    if(dataStr != null && !dataStr.isEmpty()) {
                        dataNascimento = Date.valueOf(dataStr);
                    }

                    if ("cadastrar".equals(acao)) {
                        // ALTERAÇÃO: Insert inclui Ativo = true
                        String sql = "INSERT INTO Hospede (Cpf, Nome, Email, Telefone, Data_nascimento, Ativo) VALUES (?, ?, ?, ?, ?, ?)";
                        PreparedStatement ps = conn.prepareStatement(sql);
                        ps.setString(1, cpf);
                        ps.setString(2, nome);
                        ps.setString(3, email);
                        ps.setString(4, telefone);
                        ps.setDate(5, dataNascimento);
                        ps.setBoolean(6, true); // Define como ativo por padrão
                        ps.executeUpdate();
                        
                        msgSucesso = "Hóspede cadastrado com sucesso!";
                        
                    } else if ("alterar".equals(acao)) {
                        String cpfAlvo = (cpfOriginal != null && !cpfOriginal.isEmpty()) ? cpfOriginal : cpf;
                        String sql = "UPDATE Hospede SET Nome=?, Email=?, Telefone=?, Data_nascimento=? WHERE Cpf=?";
                        PreparedStatement ps = conn.prepareStatement(sql);
                        ps.setString(1, nome);
                        ps.setString(2, email);
                        ps.setString(3, telefone);
                        ps.setDate(4, dataNascimento);
                        ps.setString(5, cpfAlvo);
                        ps.executeUpdate();
                        
                        msgSucesso = "Hóspede atualizado com sucesso!";
                    }
                }
            }
        } catch (Exception e) {
            request.setAttribute("erro", "Ocorreu um erro: " + e.getMessage());
            request.getRequestDispatcher("erro.jsp").forward(request, response);
            return;
        }

        request.setAttribute("msgSucesso", msgSucesso);
        request.setAttribute("msgErro", msgErro);
        
        doGet(request, response);
    }
}