<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<nav>
    <div>
        <a href="#">Hotel Trend</a>
        <a href="Index.jsp">Início</a>
    </div>

    <div class="dropdown">
        <a href="#">Cadastros</a>
        <div class="dropdown-content">
            <!-- Apontando para os Servlets ou JSPs conforme arquitetura -->
            <a href="Hospede.jsp">Hóspede</a>
            <a href="QuartoServlet?acao=listar">Quarto</a>
            <a href="Funcionario.jsp">Funcionário</a>
            <a href="Categoria.jsp">Categoria</a>
            <a href="Reserva.jsp">Reserva</a>
            <a href="Limpa.jsp">Limpeza</a>
        </div>
    </div>
</nav>