<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Menu Principal do Hotel</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0; /* Removi a margem para o header encostar no topo */
            background-color: #f4f4f4;
            padding-bottom: 60px; /* Espaço para o footer não cobrir conteúdo */
        }
        .conteudo {
            padding: 20px;
        }
        h1 { color: #333; }
        ul { list-style-type: none; padding: 0; }
        li { margin-bottom: 10px; }
        a.botao-menu {
            text-decoration: none;
            color: #0066cc;
            font-size: 18px;
            padding: 5px 10px;
            border: 1px solid #ccc;
            display: inline-block;
            border-radius: 4px;
            background-color: #fff;
        }
        a.botao-menu:hover {
            background-color: #e9e9e9;
            color: #004499;
        }
    </style>
</head>
<body>

    <jsp:include page="componentes/header.jsp" />

    <div class="conteudo">
        <h1>Bem-vindo! Selecione a Página Desejada</h1>
        <p>Use o menu acima ou os links abaixo para navegar:</p>

        <ul>
            <li><a class="botao-menu" href="Categoria.html">Cadastro de Categoria</a></li>
            <li><a class="botao-menu" href="Funcionario.html">Cadastro de Funcionário</a></li>
            <li><a class="botao-menu" href="HospedeServlet?acao=listar">Cadastro de Hóspede</a></li>
            <li><a class="botao-menu" href="Limpa.html">Gerenciamento de Limpeza</a></li>
            <li><a class="botao-menu" href="Quarto.html">Cadastro de Quarto</a></li>
            <li><a class="botao-menu" href="Reserva.html">Gerenciamento de Reserva</a></li>
        </ul>
    </div>


</body>
</html>