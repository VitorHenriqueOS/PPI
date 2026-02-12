<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, model.Reserva" %>
<!DOCTYPE html>
<html lang="pt-BR">
<jsp:include page="componentes/header.jsp" />
<body>
<jsp:include page="componentes/menu.jsp" />

<div class="container" style="padding: 20px;">
    <h1>Gerenciamento de Reservas</h1>
    <% String sucesso = (String) request.getAttribute("msgSucesso"); 
       if(sucesso!=null){ %> <div style="background:#d4edda;padding:10px;"><%=sucesso%></div> <% } %>

    <div class="search-box">
        <form action="ReservaServlet" method="get">
            <label>ID Reserva:</label>
            <input type="number" name="buscaId" value="<%= request.getParameter("buscaId")!=null?request.getParameter("buscaId"):"" %>">
            <button type="submit">Filtrar</button>
            <a href="ReservaServlet"><button type="button">Limpar</button></a>
        </form>
    </div>

    <table>
        <thead><tr><th>ID</th><th>Início</th><th>Fim</th><th>CPF</th><th>Quarto</th><th>Ações</th></tr></thead>
        <tbody>
        <% List<Reserva> lista = (List<Reserva>) request.getAttribute("listaReservas");
           if(lista != null) { for(Reserva r : lista) { %>
            <tr>
                <td><%= r.getId() %></td>
                <td><%= r.getDataInicio() %></td>
                <td><%= r.getDataFim() %></td>
                <td><%= r.getCpf() %></td>
                <td><%= r.getNumero() %></td>
                <td>
                    <button type="button" onclick="prepararEdicao('<%= r.getId() %>', '<%= r.getDataInicio() %>', '<%= r.getDataFim() %>', '<%= r.getCpf() %>', '<%= r.getNumero() %>')">Editar</button>
                    <form action="ReservaServlet" method="post" style="display:inline;" onsubmit="return confirm('Cancelar reserva <%= r.getId() %>?');">
                        <input type="hidden" name="acao" value="remover">
                        <input type="hidden" name="id" value="<%= r.getId() %>">
                        <button type="submit" style="color:red;">Excluir</button>
                    </form>
                </td>
            </tr>
        <% }} %>
        </tbody>
    </table>
    <hr>
    <h2 id="tituloForm">Nova Reserva</h2>
    <form action="ReservaServlet" method="post" id="formReserva">
        <input type="hidden" name="acao" id="acao" value="cadastrar">
        <label>ID:</label><br><input type="number" name="id" id="id" required><br>
        <label>Início:</label><br><input type="date" name="dataInicio" id="dataInicio" required><br>
        <label>Fim:</label><br><input type="date" name="dataFim" id="dataFim" required><br>
        <label>CPF:</label><br><input type="text" name="cpf" id="cpf" required><br>
        <label>Quarto:</label><br><input type="number" name="numero" id="numero" required><br><br>
        <button type="submit" id="btnSalvar">Salvar</button>
        <button type="button" onclick="limpar()">Limpar</button>
    </form>
</div>
<script>
function prepararEdicao(id, ini, fim, cpf, num) {
    document.getElementById('tituloForm').innerText = 'Editando Reserva ' + id;
    document.getElementById('id').value = id;
    document.getElementById('dataInicio').value = ini;
    document.getElementById('dataFim').value = fim;
    document.getElementById('cpf').value = cpf;
    document.getElementById('numero').value = num;
    document.getElementById('id').readOnly = true;
    document.getElementById('acao').value = 'alterar';
    document.getElementById('btnSalvar').innerText = 'Atualizar';
}
function limpar() {
    document.getElementById('formReserva').reset();
    document.getElementById('id').readOnly = false;
    document.getElementById('acao').value = 'cadastrar';
    document.getElementById('btnSalvar').innerText = 'Salvar';
}
</script>
</body>
<jsp:include page="componentes/footer.jsp" />
</html>