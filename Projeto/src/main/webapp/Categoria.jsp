<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Categoria" %>
<!DOCTYPE html>
<html lang="pt-BR">
<jsp:include page="componentes/header.jsp" />
<body>
<jsp:include page="componentes/menu.jsp" />

<div class="container" style="padding: 20px;">
    <h1>Gerenciamento de Categorias</h1>

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
        <form action="CategoriaServlet" method="get">
            <input type="hidden" name="acao" value="listar">
            <label>Pesquisar por ID:</label>
            <input type="number" name="buscaId" value="<%= request.getParameter("buscaId") != null ? request.getParameter("buscaId") : "" %>">
            <button type="submit">Pesquisar</button>
            <a href="CategoriaServlet?acao=listar"><button type="button">Limpar Filtro</button></a>
        </form>
    </div>

    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Nome</th>
                <th>Preço</th>
                <th>Capacidade</th>
                <th>Tipo Cama</th>
                <th>Ações</th>
            </tr>
        </thead>
        <tbody>
            <% 
                List<Categoria> lista = (List<Categoria>) request.getAttribute("listaCategorias");
                if (lista != null) {
                    for (Categoria c : lista) {
            %>
            <tr>
                <td><%= c.getId() %></td>
                <td><%= c.getNome() %></td>
                <td><%= c.getPreco() %></td>
                <td><%= c.getCapacidade() %></td>
                <td><%= c.getTipoCama() %></td>
                <td>
                    <button type="button" 
                        onclick="prepararEdicao('<%= c.getId() %>', '<%= c.getNome() %>', '<%= c.getPreco() %>', '<%= c.getCapacidade() %>', '<%= c.getTipoCama() %>')">
                        Editar
                    </button>
                    
                    <form action="CategoriaServlet" method="post" style="display:inline;"
                        onsubmit="return confirm('Deseja realmente excluir a categoria <%= c.getNome() %>?');">
                        <input type="hidden" name="acao" value="remover">
                        <input type="hidden" name="id" value="<%= c.getId() %>">
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

    <h2 id="tituloForm">Nova Categoria</h2>
    
<<<<<<< HEAD
    <%
            String msg = (String) request.getAttribute("mensagem");
            if (msg != null) {
        %>
            <div class="mensagem"><%= msg %></div>
        <%
            }
            Categoria c = (Categoria) request.getAttribute("categoria");
        %>
        
=======
>>>>>>> 462dee34ddf02cabdd4d76dbd8a9eed41cac3b14
    <form action="CategoriaServlet" method="post" id="formCategoria">
        <input type="hidden" name="acao" id="acao" value="cadastrar">
        <div style="display: flex; gap: 30px;">
            <div style="flex: 1;">
                <label>ID:</label><br>
                <input type="number" name="id" id="id" required><br><br>

                <label>Nome:</label><br>
                <input type="text" name="nome" id="nome" required placeholder="Ex: Luxo"><br><br>

                <label>Preço:</label><br>
                <input type="number" step="0.01" name="preco" id="preco" required><br><br>

                <label>Capacidade:</label><br>
                <input type="number" name="capacidade" id="capacidade" required><br><br>

<<<<<<< HEAD
                <label for="tipoCama">Tipo de Cama:</label>
            	<select id="tipoCama" name="tipoCama" required class="input-padrao">
	                <option value="" disabled <%= (c == null || c.getTipoCama() == null) ? "selected" : "" %>>Selecione o tipo</option>
	                <option value="Solteiro" <%= (c != null && "Solteiro".equals(c.getTipoCama())) ? "selected" : "" %>>Solteiro</option>
	                <option value="Casal" <%= (c != null && "Casal".equals(c.getTipoCama())) ? "selected" : "" %>>Casal</option>
	                <option value="Queen Size" <%= (c != null && "Queen Size".equals(c.getTipoCama())) ? "selected" : "" %>>Queen Size</option>
	                <option value="King Size" <%= (c != null && "King Size".equals(c.getTipoCama())) ? "selected" : "" %>>King Size</option>
            	</select> <br><br>
=======
                <label>Tipo de Cama:</label><br>
                <input type="text" name="tipoCama" id="tipoCama" required placeholder="Ex: Casal"><br><br>
>>>>>>> 462dee34ddf02cabdd4d76dbd8a9eed41cac3b14
            </div>
        </div>

        <div style="margin-top: 20px;">
            <button type="submit" id="btnSalvar">Salvar</button>
            <button type="button" onclick="limparFormulario()">Limpar / Novo</button>
        </div>
    </form>
</div>

<script>
    function prepararEdicao(id, nome, preco, capacidade, tipoCama) {
        document.getElementById('tituloForm').innerText = 'Editando Categoria: ' + id;
        document.getElementById('id').value = id;
        document.getElementById('nome').value = nome;
        document.getElementById('preco').value = preco;
        document.getElementById('capacidade').value = capacidade;
        document.getElementById('tipoCama').value = tipoCama;
        
        document.getElementById('id').readOnly = true;
        document.getElementById('acao').value = 'alterar';
        document.getElementById('btnSalvar').innerText = 'Atualizar';
    }

    function limparFormulario() {
        document.getElementById('tituloForm').innerText = 'Nova Categoria';
        document.getElementById('formCategoria').reset();
        document.getElementById('id').readOnly = false;
        document.getElementById('acao').value = 'cadastrar';
        document.getElementById('btnSalvar').innerText = 'Salvar';
    }
</script>
</body>
<jsp:include page="componentes/footer.jsp" />
</html>