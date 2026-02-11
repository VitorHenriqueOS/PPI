<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ops! Ocorreu um erro</title>
    <link rel="stylesheet" href="style.css">
    <style>
        /* Estilos inline específicos para a página de erro para garantir visualização
           caso o style.css falhe ou para destacar o erro */
        .error-container {
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            height: 100vh;
            text-align: center;
            background-color: #f8d7da;
            color: #721c24;
            font-family: Arial, sans-serif;
        }

        .error-box {
            background: white;
            padding: 40px;
            border-radius: 8px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
            border: 1px solid #f5c6cb;
            max-width: 600px;
            width: 90%;
        }

        .error-icon {
            font-size: 50px;
            margin-bottom: 20px;
        }

        .btn-voltar {
            margin-top: 20px;
            padding: 10px 20px;
            background-color: #dc3545;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            font-weight: bold;
            display: inline-block;
            transition: background 0.3s;
        }

        .btn-voltar:hover {
            background-color: #a71d2a;
        }
    </style>
</head>
<body>
    
    <!-- Incluindo o menu se necessário, mas em erro crítico as vezes é melhor isolar -->
    <%-- <jsp:include page="componentes/menu.jsp" /> --%>

    <div class="error-container">
        <div class="error-box">
            <div class="error-icon">⚠️</div>
            <h1>Ocorreu um Erro</h1>
            
            <p style="font-size: 1.2em; margin: 20px 0;">
                <% 
                    // Recupera a mensagem de erro enviada pelo Servlet
                    String mensagemErro = (String) request.getAttribute("erro");
                    if (mensagemErro == null || mensagemErro.isEmpty()) {
                        out.print("Um erro inesperado ocorreu, mas nenhuma mensagem detalhada foi fornecida.");
                    } else {
                        out.print(mensagemErro);
                    }
                %>
            </p>

            <a href="javascript:history.back()" class="btn-voltar">Voltar e Tentar Novamente</a>
            <br><br>
            <a href="Index.jsp" style="color: #721c24; font-size: 0.9em;">Ir para a Página Inicial</a>
        </div>
    </div>

</body>
</html>