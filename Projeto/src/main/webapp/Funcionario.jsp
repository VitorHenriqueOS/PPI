<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Funcionario" %>
<!DOCTYPE html>
<html lang="pt-BR">
<jsp:include page="componentes/header.jsp" />
<body>
<jsp:include page="componentes/menu.jsp" />

<div class="container" style="padding: 20px;">
    <h1>Gerenciamento de Funcionários</h1>
    
    <% String sucesso = (String) request.getAttribute("msgSucesso"); %>
    <% if (sucesso != null) { %>
        <div style="background-color: #d4edda; color: #155724; padding: 10px; margin-bottom: 15px;"><%= sucesso %></div>
    <% } %>

    <div class="search-box">
        <form action="FuncionarioServlet" method="get">
            <label>Pesquisar por ID:</label>
            <input type="number" name="buscaId" value="<%= request.getParameter("buscaId") != null ? request.getParameter("buscaId") : "" %>">
            <button type="submit">Pesquisar</button>
            <a href="FuncionarioServlet"><button type="button">Limpar</button></a>
        </form>
    </div>

    <table>
        <thead>
            <tr><th>ID</th><th>Nome</th><th>Turno</th><th>Ações</th></tr>
        </thead>
        <tbody>
            <% List<Funcionario> lista = (List<Funcionario>) request.getAttribute("listaFuncionarios");
               if (lista != null) {
                   for (Funcionario f : lista) { %>
            <tr>
                <td><%= f.getId() %></td>
                <td><%= f.getNome() %></td>
                <td><%= f.getTurno() %></td>
                <td>
                    <button type="button" onclick="prepararEdicao('<%= f.getId() %>', '<%= f.getNome() %>', '<%= f.getTurno() %>')">Editar</button>
                    <form action="FuncionarioServlet" method="post" style="display:inline;" onsubmit="return confirm('Excluir <%= f.getNome() %>?');">
                        <input type="hidden" name="acao" value="remover">
                        <input type="hidden" name="id" value="<%= f.getId() %>">
                        <button type="submit" style="color:red;">Excluir</button>
                    </form>
                </td>
            </tr>
            <% }} %>
        </tbody>
    </table>
    <hr>
    <h2 id="tituloForm">Novo Funcionário</h2>
    <form action="FuncionarioServlet" method="post" id="formFunc">
        <input type="hidden" name="acao" id="acao" value="cadastrar">
        <label>ID:</label><br> <input type="number" name="id" id="id" required><br><br>
        <label>Nome:</label><br> <input type="text" name="nome" id="nome" required><br><br>
        <label>Turno:</label><br> <input type="text" name="turno" id="turno" required><br><br>
        <button type="submit" id="btnSalvar">Salvar</button>
        <button type="button" onclick="limparFormulario()">Limpar</button>
    </form>
</div>
<script>
    function prepararEdicao(id, nome, turno) {
        document.getElementById('tituloForm').innerText = 'Editando: ' + nome;
        document.getElementById('id').value = id;
        document.getElementById('nome').value = nome;
        document.getElementById('turno').value = turno;
        document.getElementById('id').readOnly = true;
        document.getElementById('acao').value = 'alterar';
        document.getElementById('btnSalvar').innerText = 'Atualizar';
    }
    function limparFormulario() {
        document.getElementById('tituloForm').innerText = 'Novo Funcionário';
        document.getElementById('formFunc').reset();
        document.getElementById('id').readOnly = false;
        document.getElementById('acao').value = 'cadastrar';
        document.getElementById('btnSalvar').innerText = 'Salvar';
    }
</script>
</body>
<jsp:include page="componentes/footer.jsp" />
</html>