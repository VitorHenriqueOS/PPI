<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, model.Limpa" %>
<!DOCTYPE html>
<html lang="pt-BR">
<jsp:include page="componentes/header.jsp" />
<body>
<jsp:include page="componentes/menu.jsp" />

<div class="container" style="padding: 20px;">
    <h1>Gerenciamento de Limpeza</h1>
    <% String sucesso = (String) request.getAttribute("msgSucesso"); 
       if(sucesso!=null){ %> <div style="background:#d4edda;padding:10px;"><%=sucesso%></div> <% } %>

    <div class="search-box">
        <form action="LimpaServlet" method="get" style="display:flex; gap:10px;">
            <div><label>Data:</label><input type="date" name="buscaData"></div>
            <div><label>Quarto:</label><input type="number" name="buscaQuarto"></div>
            <button type="submit">Filtrar</button>
            <a href="LimpaServlet"><button type="button">Limpar</button></a>
        </form>
    </div>

    <table>
        <thead><tr><th>ID</th><th>Data</th><th>Quarto</th><th>Func. ID</th><th>Obs</th><th>Ações</th></tr></thead>
        <tbody>
        <% List<Limpa> lista = (List<Limpa>) request.getAttribute("listaLimpeza");
           if(lista != null) { for(Limpa l : lista) { %>
            <tr>
                <td><%= l.getId() %></td>
                <td><%= l.getData() %></td>
                <td><%= l.getNumeroQuarto() %></td>
                <td><%= l.getIdFuncionario() %></td>
                <td><%= l.getObs() %></td>
                <td>
                    <button type="button" onclick="prepararEdicao('<%= l.getId() %>', '<%= l.getData() %>', '<%= l.getNumeroQuarto() %>', '<%= l.getIdFuncionario() %>', '<%= l.getObs() %>')">Editar</button>
                    <form action="LimpaServlet" method="post" style="display:inline;" onsubmit="return confirm('Excluir registro?');">
                        <input type="hidden" name="acao" value="remover">
                        <input type="hidden" name="id" value="<%= l.getId() %>">
                        <button type="submit" style="color:red;">Excluir</button>
                    </form>
                </td>
            </tr>
        <% }} %>
        </tbody>
    </table>
    <hr>
    <h2 id="tituloForm">Registrar Limpeza</h2>
    <form action="LimpaServlet" method="post" id="formLimpa">
        <input type="hidden" name="acao" id="acao" value="cadastrar">
        <label>ID:</label><br><input type="number" name="id" id="id" required><br>
        <label>Data:</label><br><input type="date" name="data" id="data" required><br>
        <label>Quarto:</label><br><input type="number" name="numero" id="numero" required><br>
        <label>Funcionario ID:</label><br><input type="number" name="idFuncionario" id="idFuncionario" required><br>
        <label>Obs:</label><br><input type="text" name="obs" id="obs" required><br><br>
        <button type="submit" id="btnSalvar">Salvar</button>
        <button type="button" onclick="limpar()">Limpar</button>
    </form>
</div>
<script>
function prepararEdicao(id, data, quarto, func, obs) {
    document.getElementById('tituloForm').innerText = 'Editando Limpeza ' + id;
    document.getElementById('id').value = id;
    document.getElementById('data').value = data;
    document.getElementById('numero').value = quarto;
    document.getElementById('idFuncionario').value = func;
    document.getElementById('obs').value = obs;
    document.getElementById('id').readOnly = true;
    document.getElementById('acao').value = 'alterar';
    document.getElementById('btnSalvar').innerText = 'Atualizar';
}
function limpar() {
    document.getElementById('formLimpa').reset();
    document.getElementById('id').readOnly = false;
    document.getElementById('acao').value = 'cadastrar';
    document.getElementById('btnSalvar').innerText = 'Salvar';
}
</script>
</body>
<jsp:include page="componentes/footer.jsp" />
</html>