<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<<<<<<< HEAD
<%@ page import="java.util.List, java.util.Map, model.Reserva, model.Quarto, model.Categoria" %>
<%
    // Redireciona para o Servlet se os dados auxiliares não estiverem carregados
    if (request.getAttribute("listaQuartos") == null || request.getAttribute("mapCategorias") == null) {
        response.sendRedirect("ReservaServlet");
        return;
    }
%>
=======
<%@ page import="java.util.List, model.Reserva" %>
>>>>>>> 462dee34ddf02cabdd4d76dbd8a9eed41cac3b14
<!DOCTYPE html>
<html lang="pt-BR">
<jsp:include page="componentes/header.jsp" />
<body>
<jsp:include page="componentes/menu.jsp" />

<div class="container" style="padding: 20px;">
    <h1>Gerenciamento de Reservas</h1>
    <% String sucesso = (String) request.getAttribute("msgSucesso"); 
       if(sucesso!=null){ %> <div style="background:#d4edda;padding:10px;"><%=sucesso%></div> <% } %>

<<<<<<< HEAD
    <% String erro = (String) request.getAttribute("erro"); 
       if(erro!=null){ %> <div style="background:#f8d7da;color:#721c24;padding:10px;"><%=erro%></div> <% } %>

    <div class="search-box">
        <form action="ReservaServlet" method="get" style="display:flex; gap:10px; align-items: flex-end; flex-wrap: wrap;">
            <!-- Campo de Data Removido -->
            <div>
                <label>CPF:</label><br>
                <input type="text" name="buscaCpf" value="<%= request.getParameter("buscaCpf")!=null?request.getParameter("buscaCpf"):"" %>">
            </div>
            <div>
                <label>Quarto:</label><br>
                <input type="number" name="buscaQuarto" value="<%= request.getParameter("buscaQuarto")!=null?request.getParameter("buscaQuarto"):"" %>">
            </div>
            <div style="margin-bottom: 4px;">
                <button type="submit">Filtrar</button>
                <a href="ReservaServlet"><button type="button">Limpar</button></a>
            </div>
=======
    <div class="search-box">
        <form action="ReservaServlet" method="get">
            <label>ID Reserva:</label>
            <input type="number" name="buscaId" value="<%= request.getParameter("buscaId")!=null?request.getParameter("buscaId"):"" %>">
            <button type="submit">Filtrar</button>
            <a href="ReservaServlet"><button type="button">Limpar</button></a>
>>>>>>> 462dee34ddf02cabdd4d76dbd8a9eed41cac3b14
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
<<<<<<< HEAD
    
    <form action="ReservaServlet" method="post" id="formReserva">
        <input type="hidden" name="acao" id="acao" value="cadastrar">
        
        <div style="display: flex; gap: 30px; align-items: flex-start;">
            <!-- Lado Esquerdo: Inputs -->
            <div style="flex: 1;">
                <label>ID:</label><br>
                <input type="number" name="id" id="id" required><br>

                <label>Início:</label><br>
                <input type="date" name="dataInicio" id="dataInicio" required><br>

                <label>Fim:</label><br>
                <input type="date" name="dataFim" id="dataFim" required><br>

                <label>CPF:</label><br>
                <input type="text" name="cpf" id="cpf" required><br>

                <label>Quarto:</label><br>
                <select name="numero" id="numero" required style="padding: 5px; min-width: 200px;" onchange="atualizarDetalhesQuarto()">
                    <option value="">Selecione um Quarto...</option>
                    <%
                        List<Quarto> listaQ = (List<Quarto>) request.getAttribute("listaQuartos");
                        Map<Integer, Categoria> mapC = (Map<Integer, Categoria>) request.getAttribute("mapCategorias");
                        
                        if (listaQ != null && mapC != null) {
                            for (Quarto q : listaQ) {
                                Categoria c = mapC.get(q.getIdCategoria());
                                String nomeCat = (c != null) ? c.getNome() : "Desconhecida";
                                String preco = (c != null && c.getPreco() != null) ? c.getPreco().toString() : "0.00";
                                String cap = (c != null) ? String.valueOf(c.getCapacidade()) : "0";
                                String cama = (c != null) ? c.getTipoCama() : "-";
                    %>
                        <option value="<%= q.getNumero() %>"
                                data-cat="<%= nomeCat %>"
                                data-preco="<%= preco %>"
                                data-capacidade="<%= cap %>"
                                data-cama="<%= cama %>">
                            Quarto <%= q.getNumero() %> (<%= nomeCat %>)
                        </option>
                    <%
                            }
                        }
                    %>
                </select>
                <br><br>
            </div>

            <!-- Lado Direito: Card de Detalhes -->
            <div id="cardDetalhes" style="flex: 1; border: 1px solid #ccc; padding: 15px; background-color: #f9f9f9; border-radius: 5px; display: none;">
                <h3 style="margin-top: 0;">Detalhes do Quarto</h3>
                <p><strong>Categoria:</strong> <span id="detalheCat">-</span></p>
                <p><strong>Preço:</strong> R$ <span id="detalhePreco">-</span></p>
                <p><strong>Capacidade:</strong> <span id="detalheCapacidade">-</span> pessoas</p>
                <p><strong>Tipo de Cama:</strong> <span id="detalheCama">-</span></p>
            </div>
        </div>

        <div style="margin-top: 20px;">
            <button type="submit" id="btnSalvar">Salvar</button>
            <button type="button" onclick="limpar()">Limpar</button>
        </div>
    </form>
</div>
<script>
// Nova função para atualizar o card com base no quarto selecionado
function atualizarDetalhesQuarto() {
    var select = document.getElementById("numero");
    var card = document.getElementById("cardDetalhes");
    
    var selectedOption = select.options[select.selectedIndex];
    
    if (select.value === "") {
        card.style.display = "none";
        return;
    }

    // Recupera dados
    var cat = selectedOption.getAttribute("data-cat");
    var preco = selectedOption.getAttribute("data-preco");
    var capacidade = selectedOption.getAttribute("data-capacidade");
    var cama = selectedOption.getAttribute("data-cama");

    // Atualiza HTML
    document.getElementById("detalheCat").innerText = cat;
    document.getElementById("detalhePreco").innerText = preco;
    document.getElementById("detalheCapacidade").innerText = capacidade;
    document.getElementById("detalheCama").innerText = cama;

    // Mostra o card
    card.style.display = "block";
}

=======
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
>>>>>>> 462dee34ddf02cabdd4d76dbd8a9eed41cac3b14
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
<<<<<<< HEAD
    
    // Atualiza os detalhes visualmente para o item sendo editado
    atualizarDetalhesQuarto();
}

function limpar() {
    document.getElementById('tituloForm').innerText = 'Nova Reserva';
=======
}
function limpar() {
>>>>>>> 462dee34ddf02cabdd4d76dbd8a9eed41cac3b14
    document.getElementById('formReserva').reset();
    document.getElementById('id').readOnly = false;
    document.getElementById('acao').value = 'cadastrar';
    document.getElementById('btnSalvar').innerText = 'Salvar';
<<<<<<< HEAD
    
    // Esconde o card
    document.getElementById("cardDetalhes").style.display = "none";
=======
>>>>>>> 462dee34ddf02cabdd4d76dbd8a9eed41cac3b14
}
</script>
</body>
<jsp:include page="componentes/footer.jsp" />
</html>