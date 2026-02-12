<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Quarto" %>
<%@ page import="model.Categoria" %>
<!DOCTYPE html>
<html lang="pt-BR">
<jsp:include page="componentes/header.jsp" />
<body>
<jsp:include page="componentes/menu.jsp" />

<div class="container" style="padding: 20px;">
    <h1>Gerenciamento de Quartos</h1>

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
        <form action="QuartoServlet" method="get">
            <input type="hidden" name="acao" value="listar">
            <label>Pesquisar por Número:</label>
            <input type="number" name="buscaNumero" value="<%= request.getParameter("buscaNumero") != null ? request.getParameter("buscaNumero") : "" %>">
            <button type="submit">Pesquisar</button>
            <a href="QuartoServlet?acao=listar"><button type="button">Limpar Filtro</button></a>
        </form>
    </div>

    <table>
        <thead>
            <tr>
                <th>Número</th>
                <th>Andar</th>
                <th>Status</th>
                <th>Categoria</th>
                <th>Ações</th>
            </tr>
        </thead>
        <tbody>
            <% 
                List<Quarto> lista = (List<Quarto>) request.getAttribute("listaQuartos");
                if (lista != null) {
                    for (Quarto q : lista) {
            %>
            <tr>
                <td><%= q.getNumero() %></td>
                <td><%= q.getAndar() %></td>
                <td><%= q.getStatus() %></td>
                <td><%= q.getNomeCategoria() != null ? q.getNomeCategoria() : "N/A" %></td>
                <td>
                    <button type="button" 
                        onclick="prepararEdicao('<%= q.getNumero() %>', '<%= q.getAndar() %>', '<%= q.getStatus() %>', '<%= q.getIdCategoria() %>')">
                        Editar
                    </button>
                    
                    <form action="QuartoServlet" method="post" style="display:inline;" onsubmit="return confirm('Deseja realmente excluir o quarto <%= q.getNumero() %>?');">
                        <input type="hidden" name="acao" value="remover">
                        <input type="hidden" name="numero" value="<%= q.getNumero() %>">
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

    <h2 id="tituloForm">Novo Quarto</h2>
    
    <form action="QuartoServlet" method="post" id="formQuarto">
        
        <input type="hidden" name="acao" id="acao" value="cadastrar">
        <div style="display: flex; gap: 30px;">
            <div style="flex: 1;">
                <label>Número (ID):</label><br>
                <input type="number" name="numero" id="numero" required><br><br>

                <label>Andar:</label><br>
                <input type="number" name="andar" id="andar" required><br><br>

                <label>Status:</label><br>
                <input type="text" name="status" id="status" required placeholder="Ex: Disponível"><br><br>

                <label>Categoria:</label><br>
                <select name="idCategoria" id="idCategoria" required style="padding: 5px; min-width: 200px;">
                    <option value="">Selecione uma Categoria...</option>
                    <% 
                        List<Categoria> categorias = (List<Categoria>) request.getAttribute("listaCategorias");
                        if (categorias != null) {
                            for (Categoria cat : categorias) {
                    %>
                        <option value="<%= cat.getId() %>"><%= cat.getNome() %></option>
                    <% 
                            }
                        }
                    %>
                </select>
                <br><br>
            </div>
        </div>

        <div style="margin-top: 20px;">
            <button type="submit" id="btnSalvar">Salvar</button>
            <button type="button" onclick="limparFormulario()">Limpar / Novo</button>
        </div>
    </form>
</div>

<script>
    function prepararEdicao(numero, andar, status, idCategoria) {
        document.getElementById('tituloForm').innerText = 'Editando Quarto: ' + numero;
        document.getElementById('numero').value = numero;
        document.getElementById('andar').value = andar;
        document.getElementById('status').value = status;
        
        // O select será ajustado automaticamente se houver um option com esse value (ID)
        document.getElementById('idCategoria').value = idCategoria;
        
        // O número é a chave primária, não deve mudar na edição
        document.getElementById('numero').readOnly = true;
        document.getElementById('acao').value = 'alterar';
        document.getElementById('btnSalvar').innerText = 'Atualizar';
    }

    function limparFormulario() {
        document.getElementById('tituloForm').innerText = 'Novo Quarto';
        document.getElementById('formQuarto').reset();
        
        document.getElementById('numero').readOnly = false;
        
        document.getElementById('acao').value = 'cadastrar';
        document.getElementById('btnSalvar').innerText = 'Salvar';
    }
</script>
</body>
<jsp:include page="componentes/footer.jsp" />
</html>