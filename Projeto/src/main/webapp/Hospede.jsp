<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Hospede" %>
<!DOCTYPE html>
<html lang="pt-BR">
<jsp:include page="componentes/header.jsp" />
<body>
<jsp:include page="componentes/menu.jsp" />

<div class="container" style="padding: 20px;">
    <h1>Gerenciamento de Hóspedes</h1>

    <% String sucesso = (String) request.getAttribute("msgSucesso"); %>
    <% String erro = (String) request.getAttribute("msgErro"); %>
    
    <% if (sucesso != null) { %>
        <div style="background-color: #d4edda; color: #155724; padding: 10px; margin-bottom: 15px;">
            <%= sucesso %>
        </div>
    <% } %>
    
    <% if (erro != null) { %>
        <div style="background-color: #f8d7da; color: #721c24; padding: 10px; margin-bottom: 15px;">
            <%= erro %>
        </div>
    <% } %>
    
    <div class="search-box">
        <form action="HospedeServlet" method="get">
            <input type="hidden" name="acao" value="listar">
            <label>Pesquisar por CPF:</label>
            <input type="text" name="buscaCpf" value="<%= request.getParameter("buscaCpf") != null ? request.getParameter("buscaCpf") : "" %>">
            <button type="submit">Pesquisar</button>
            <a href="HospedeServlet?acao=listar"><button type="button">Limpar Filtro</button></a>
        </form>
    </div>

    <table>
        <thead>
            <tr>
                <th>CPF</th>
                <th>Nome</th>
                <th>Email</th>
                <th>Ações</th>
            </tr>
        </thead>
        <tbody>
            <% 
                List<Hospede> lista = (List<Hospede>) request.getAttribute("listaHospedes");
                if (lista != null) {
                    for (Hospede h : lista) {
            %>
            <tr>
                <td><%= h.getCpf() %></td>
                <td><%= h.getNome() %></td>
                <td><%= h.getEmail() %></td>
                <td>
                    <button type="button" 
                        onclick="prepararEdicao('<%= h.getCpf() %>', '<%= h.getNome() %>', '<%= h.getEmail() %>', '<%= h.getTelefone() %>', '<%= h.getDataNascimento() %>')">
                        Editar
                    </button>
                    
                    <form action="HospedeServlet" method="post" style="display:inline;" onsubmit="return confirm('Excluir?');">
                        <input type="hidden" name="acao" value="remover">
                        <input type="hidden" name="cpf" value="<%= h.getCpf() %>">
                        <button type="submit" style="color:red;">Excluir</button>
                    </form>
                </td>
            </tr>
            <% 
                    }
                } 
            %>
        </tbody>
    </table>
    
    <hr>

    <h2 id="tituloForm">Novo Cadastro</h2>
    
    <form action="HospedeServlet" method="post" id="formHospede">
        
        <input type="hidden" name="acao" id="acao" value="cadastrar">
        <input type="hidden" name="cpfOriginal" id="cpfOriginal">

        <div style="max-width: 600px;">
            <label>CPF:</label><br>
            <input type="text" name="cpf" id="cpf" required style="width: 100%;"><br><br>

            <label>Nome:</label><br>
            <input type="text" name="nome" id="nome" required style="width: 100%;"><br><br>

            <label>Email:</label><br>
            <input type="email" name="email" id="email" required style="width: 100%;"><br><br>

            <label>Telefone:</label><br>
            <input type="text" name="telefone" id="telefone" required style="width: 100%;"><br><br>

            <label>Data Nascimento:</label><br>
            <input type="date" name="dataNascimento" id="dataNascimento" required><br><br>
        </div>

        <div style="margin-top: 20px;">
            <button type="submit" id="btnSalvar">Salvar</button>
            <button type="button" onclick="limparFormulario()">Limpar / Novo</button>
        </div>
    </form>
</div>

<script>
    function prepararEdicao(cpf, nome, email, telefone, data) {
        document.getElementById('tituloForm').innerText = 'Editando: ' + nome;
        document.getElementById('cpf').value = cpf;
        document.getElementById('nome').value = nome;
        document.getElementById('email').value = email;
        document.getElementById('telefone').value = telefone;
        document.getElementById('dataNascimento').value = data;

        document.getElementById('cpf').readOnly = true;
        document.getElementById('cpfOriginal').value = cpf;
        document.getElementById('acao').value = 'alterar';
        document.getElementById('btnSalvar').innerText = 'Atualizar';
    }

    function limparFormulario() {
        document.getElementById('tituloForm').innerText = 'Novo Cadastro';
        document.getElementById('formHospede').reset();
        document.getElementById('cpf').readOnly = false;
        document.getElementById('cpfOriginal').value = "";
        document.getElementById('acao').value = 'cadastrar';
        document.getElementById('btnSalvar').innerText = 'Salvar';
    }
</script>
</body>
<jsp:include page="componentes/footer.jsp" />
</html>