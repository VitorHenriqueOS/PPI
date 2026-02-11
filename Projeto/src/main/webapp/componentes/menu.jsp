<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<nav>
    <div>
        <a href="#">Hotel Trend</a>
        <a href="Index.html">Início</a>
    </div>

    <div class="dropdown">
        <a href="#">Cadastros</a>
        <div class="dropdown-content">
            <!-- Apontando para os Servlets ou JSPs conforme arquitetura -->
            <a href="HospedeServlet?acao=listar">Hóspede</a>
            <a href="Quarto.html">Quarto</a>
            <a href="Funcionario.html">Funcionário</a>
            <a href="Categoria.html">Categoria</a>
            <a href="Reserva.html">Reserva</a>
            <a href="Limpa.html">Limpeza</a>
        </div>
    </div>
</nav>