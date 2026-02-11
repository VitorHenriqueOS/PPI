package servelet;

import db.Conector;
import model.Hospede;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Importações do Apache Commons FileUpload (presentes no seu classpath)
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

@WebServlet("/HospedeServlet")
public class HospedeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    // Configurações de Upload
    private static final int MEMORY_THRESHOLD = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 50; // 50MB
    private static final String UPLOAD_DIRECTORY = "uploads";

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
                sql = "SELECT * FROM Hospede WHERE Cpf = ?";
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
                    lista.add(h);
                }
                if (lista.isEmpty()) {
                    aviso = "Nenhum hóspede encontrado com o CPF: " + buscaCpf;
                }
            } else {
                sql = "SELECT * FROM Hospede";
                ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Hospede h = new Hospede();
                    h.setCpf(rs.getString("Cpf"));
                    h.setNome(rs.getString("Nome"));
                    h.setEmail(rs.getString("Email"));
                    h.setTelefone(rs.getString("Telefone"));
                    h.setDataNascimento(rs.getDate("Data_nascimento"));
                    lista.add(h);
                }
            }
        } catch (Exception e) {
            erro = "Erro de conexão: " + e.getMessage();
            e.printStackTrace();
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
        
        // Verifica se é multipart (upload)
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        
        // Variáveis para armazenar os campos do formulário
        Map<String, String> formFields = new HashMap<>();
        List<FileItem> fileItems = new ArrayList<>();
        
        String msgSucesso = null;
        String msgErro = null;

        try {
            // Se for multipart, precisamos processar os campos manualmente
            if (isMultipart) {
                DiskFileItemFactory factory = new DiskFileItemFactory();
                factory.setSizeThreshold(MEMORY_THRESHOLD);
                factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
                
                ServletFileUpload upload = new ServletFileUpload(factory);
                upload.setFileSizeMax(MAX_FILE_SIZE);
                upload.setSizeMax(MAX_REQUEST_SIZE);
                upload.setHeaderEncoding("UTF-8");
                
                // Analisa a requisição
                List<FileItem> items = upload.parseRequest(request);
                
                for (FileItem item : items) {
                    if (item.isFormField()) {
                        // Campos de texto (CPF, Nome, etc)
                        formFields.put(item.getFieldName(), item.getString("UTF-8"));
                    } else {
                        // Arquivos
                        fileItems.add(item);
                    }
                }
            } else {
                // Se não for multipart (ex: delete via form simples se houvesse), lê normal
                // Mas como o JSP foi alterado para multipart, vamos focar na lógica acima.
                // Para manter compatibilidade com outras chamadas, poderíamos ler request.getParameter aqui.
                // Mas vamos assumir o fluxo multipart.
            }

            // Recupera os dados do Map
            String acao = formFields.get("acao");
            String cpf = formFields.get("cpf");
            String nome = formFields.get("nome");
            String email = formFields.get("email");
            String telefone = formFields.get("telefone");
            String dataStr = formFields.get("dataNascimento");
            String cpfOriginal = formFields.get("cpfOriginal");

            try (Connection conn = new Conector().getConexao()) {
                
                if ("remover".equals(acao)) {
                    String sql = "DELETE FROM Hospede WHERE Cpf=?";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, cpf);
                    ps.executeUpdate();
                    
                    // Opcional: Deletar fotos associadas ao remover
                    deletarFotos(request, cpf);
                    
                    msgSucesso = "Hóspede removido com sucesso!";
                    
                } else {
                    if (cpf == null || cpf.isEmpty()) {
                        throw new Exception("CPF é obrigatório.");
                    }

                    // Conversão de Data
                    Date dataNascimento = null;
                    if(dataStr != null && !dataStr.isEmpty()) {
                        dataNascimento = Date.valueOf(dataStr);
                    }

                    if ("cadastrar".equals(acao)) {
                        String sql = "INSERT INTO Hospede (Cpf, Nome, Email, Telefone, Data_nascimento) VALUES (?, ?, ?, ?, ?)";
                        PreparedStatement ps = conn.prepareStatement(sql);
                        ps.setString(1, cpf);
                        ps.setString(2, nome);
                        ps.setString(3, email);
                        ps.setString(4, telefone);
                        ps.setDate(5, dataNascimento);
                        ps.executeUpdate();
                        
                        // Salvar Fotos após cadastro bem sucedido
                        salvarFotos(request, fileItems, cpf);
                        
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
                        
                        // Salvar Fotos (sobrescreve se enviar novas)
                        salvarFotos(request, fileItems, cpfAlvo);
                        
                        msgSucesso = "Hóspede atualizado com sucesso!";
                    }
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            msgErro = "Ocorreu um erro: " + e.getMessage();
        }

        request.setAttribute("msgSucesso", msgSucesso);
        request.setAttribute("msgErro", msgErro);
        
        // Recarrega a lista
        doGet(request, response);
    }
    
    /**
     * Método auxiliar para salvar as fotos no diretório da aplicação
     */
    private void salvarFotos(HttpServletRequest request, List<FileItem> items, String cpf) throws Exception {
        // Define o caminho: .../webapp/uploads
        String uploadPath = request.getServletContext().getRealPath("") + File.separator + UPLOAD_DIRECTORY;
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        for (FileItem item : items) {
            if (!item.isFormField() && item.getSize() > 0) {
                String fieldName = item.getFieldName();
                String fileName = "";
                
                // Definir nome padrão baseado no CPF e no campo (foto1 ou foto2)
                // Salvamos sempre como .jpg para simplificar o exemplo, 
                // ou poderíamos manter a extensão original.
                // Para simplificar a exibição no JSP, vou forçar .jpg ou simplesmente salvar com nome fixo.
                
                if ("foto1".equals(fieldName)) {
                    fileName = cpf + "_1.jpg";
                } else if ("foto2".equals(fieldName)) {
                    fileName = cpf + "_2.jpg";
                }
                
                if (!fileName.isEmpty()) {
                    String filePath = uploadPath + File.separator + fileName;
                    File storeFile = new File(filePath);
                    item.write(storeFile); // Salva o arquivo no disco
                    System.out.println("Foto salva em: " + filePath);
                }
            }
        }
    }
    
    private void deletarFotos(HttpServletRequest request, String cpf) {
        try {
            String uploadPath = request.getServletContext().getRealPath("") + File.separator + UPLOAD_DIRECTORY;
            File f1 = new File(uploadPath + File.separator + cpf + "_1.jpg");
            File f2 = new File(uploadPath + File.separator + cpf + "_2.jpg");
            if(f1.exists()) f1.delete();
            if(f2.exists()) f2.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}