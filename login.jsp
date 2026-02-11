<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login - Hotel Trend</title>
</head>
<body>
    <h2>Acesso ao Sistema</h2>
    
    <form action="LoginServlet" method="post">
        <label>Usuário:</label>
        <input type="text" name="usuario" required><br>
        
        <label>Senha:</label>
        <input type="password" name="senha" required><br>
        
        <button type="submit">Entrar</button>
    </form>
</body>
</html>