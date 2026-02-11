package servelet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    // Login geralmente usa POST, pois senha é dado sensível
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Pegar os dados do formulário
        String usuario = request.getParameter("usuario");
        String senha = request.getParameter("senha");
        
        // 2. Processar
        boolean loginValido = "admin".equals(usuario) && "123".equals(senha);

        // 3. Encaminhar (Forward) baseado no resultado
        if (loginValido) {
            // SUCESSO: Encaminha para o menu principal
            request.getRequestDispatcher("index.jsp").forward(request, response);
        } else {
            // ERRO: Encaminha para a página de erro
            request.getRequestDispatcher("erro.jsp").forward(request, response);
        }
    }
}