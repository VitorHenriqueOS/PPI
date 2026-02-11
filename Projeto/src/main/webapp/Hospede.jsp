<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Hospede" %>
<!DOCTYPE html>
<html lang="pt-BR">
<!-- Importa o Header -->
<jsp:include page="componentes/header.jsp" />
<body>
<!-- Importa o Menu -->
<jsp:include page="componentes/menu.jsp" />

<div class="container" style="padding: 20px;">
    <h1>Gerenciamento de Hóspedes</h1>

    <!-- Área de Mensagens (Mantenha igual ao anterior) -->
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
    <!-- Área de Busca -->
    <div class="search-box">
        <form action="HospedeServlet" method="get">
            <input type="hidden" name="acao" value="listar">
            <label>Pesquisar por CPF:</label>
            <input type="text" name="buscaCpf" value="<%= request.getParameter("buscaCpf") != null ? request.getParameter("buscaCpf") : "" %>">
            <button type="submit">Pesquisar</button>
            <a href="HospedeServlet?acao=listar"><button type="button">Limpar Filtro</button></a>
        </form>
    </div>

    <!-- Tabela de Listagem -->
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
                    <!-- Botão Editar -->
                    <button type="button" 
                        onclick="prepararEdicao('<%= h.getCpf() %>', '<%= h.getNome() %>', '<%= h.getEmail() %>', '<%= h.getTelefone() %>', '<%= h.getDataNascimento() %>')">
                        Editar / Ver Fotos
                    </button>
                    
                    <!-- Botão Excluir -->
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

    <!-- Formulário de Cadastro/Edição -->
    <h2 id="tituloForm">Novo Cadastro</h2>
    
    <!-- ENCTYPE é essencial para upload -->
    <form action="HospedeServlet" method="post" id="formHospede" enctype="multipart/form-data">
        
        <input type="hidden" name="acao" id="acao" value="cadastrar">
        <input type="hidden" name="cpfOriginal" id="cpfOriginal">

        <div style="display: flex; gap: 30px;">
            <!-- Coluna de Dados -->
            <div style="flex: 1;">
                <label>CPF:</label><br>
                <input type="text" name="cpf" id="cpf" required><br><br>

                <label>Nome:</label><br>
                <input type="text" name="nome" id="nome" required><br><br>

                <label>Email:</label><br>
                <input type="email" name="email" id="email" required><br><br>

                <label>Telefone:</label><br>
                <input type="text" name="telefone" id="telefone" required><br><br>

                <label>Data Nascimento:</label><br>
                <input type="date" name="dataNascimento" id="dataNascimento" required>
            </div>

            <!-- Coluna de Fotos (Área separada visualmente) -->
            <div style="flex: 1; border: 1px dashed #999; padding: 15px; background-color: #f9f9f9;">
                <h3>Arquivos / Fotos</h3>
                
                <!-- FOTO 1 -->
                <div style="margin-bottom: 20px;">
                    <label><strong>Arquivo 1 (Documento):</strong></label><br>
                    <input type="file" name="foto1" id="inputFoto1" accept="image/*"><br>
                    
                    <!-- Visualização e Download 1 -->
                    <div style="margin-top: 5px;">
                        <img id="viewFoto1" src="" style="max-width: 100px; display: none; border: 1px solid #ccc;">
                        <br>
                        <!-- Link de Download adicionado aqui -->
                        <a id="linkDownload1" href="#" download class="btn-download" style="display:none; color: blue; text-decoration: underline; cursor: pointer;">
                            Baixar Arquivo 1
                        </a>
                    </div>
                </div>

                <hr>

                <!-- FOTO 2 -->
                <div>
                    <label><strong>Arquivo 2 (Perfil):</strong></label><br>
                    <input type="file" name="foto2" id="inputFoto2" accept="image/*"><br>
                    
                    <!-- Visualização e Download 2 -->
                    <div style="margin-top: 5px;">
                        <img id="viewFoto2" src="" style="max-width: 100px; display: none; border: 1px solid #ccc;">
                        <br>
                        <!-- Link de Download adicionado aqui -->
                        <a id="linkDownload2" href="#" download class="btn-download" style="display:none; color: blue; text-decoration: underline; cursor: pointer;">
                            Baixar Arquivo 2
                        </a>
                    </div>
                </div>
            </div>
        </div>

        <div style="margin-top: 20px;">
            <button type="submit" id="btnSalvar">Salvar</button>
            <button type="button" onclick="limparFormulario()">Limpar / Novo</button>
        </div>
    </form>
</div>

<script>
    function prepararEdicao(cpf, nome, email, telefone, data) {
        // Preenche dados do formulário
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

        // --- Lógica das Imagens e Download ---
        const timestamp = new Date().getTime(); // Evita cache do navegador

        // Configura Foto 1
        const img1 = document.getElementById('viewFoto1');
        const link1 = document.getElementById('linkDownload1');
        const caminhoFoto1 = 'uploads/' + cpf + '_1.jpg'; // Caminho relativo

        img1.src = caminhoFoto1 + '?' + timestamp;
        link1.href = caminhoFoto1; // O link aponta para o mesmo lugar da imagem

        // Evento: Se a imagem carregar, mostra ela e o botão de baixar
        img1.onload = function() {
            this.style.display = 'block';
            link1.style.display = 'inline-block';
        };
        // Evento: Se der erro (não existir imagem), esconde tudo
        img1.onerror = function() {
            this.style.display = 'none';
            link1.style.display = 'none';
        };

        // Configura Foto 2
        const img2 = document.getElementById('viewFoto2');
        const link2 = document.getElementById('linkDownload2');
        const caminhoFoto2 = 'uploads/' + cpf + '_2.jpg';

        img2.src = caminhoFoto2 + '?' + timestamp;
        link2.href = caminhoFoto2;

        img2.onload = function() {
            this.style.display = 'block';
            link2.style.display = 'inline-block';
        };
        img2.onerror = function() {
            this.style.display = 'none';
            link2.style.display = 'none';
        };
    }

    function limparFormulario() {
        document.getElementById('tituloForm').innerText = 'Novo Cadastro';
        document.getElementById('formHospede').reset();
        document.getElementById('cpf').readOnly = false;
        document.getElementById('cpfOriginal').value = "";
        document.getElementById('acao').value = 'cadastrar';
        document.getElementById('btnSalvar').innerText = 'Salvar';
        
        // Esconde imagens e links de download
        document.getElementById('viewFoto1').style.display = 'none';
        document.getElementById('linkDownload1').style.display = 'none';
        
        document.getElementById('viewFoto2').style.display = 'none';
        document.getElementById('linkDownload2').style.display = 'none';
    }
</script>
</body>
</html>