<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Object user = session.getAttribute("user");
%>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="UTF-8" />
    <title>Strona główna</title>
</head>
<body>
<% if (user != null) { %>
    <h1>Witaj, <%= ((com.demo.models.User) user).getUsername() %>!</h1>
    <p>Jesteś zalogowany.</p>
    <form action="logout" method="post">
        <button type="submit">Wyloguj się</button>
    </form>
<% } else { %>
    <h1>Witaj na stronie!</h1>
    <p>Nie jesteś zalogowany.</p>
    <a href="login.jsp">Zaloguj się</a>
<% } %>
</body>
</html>
