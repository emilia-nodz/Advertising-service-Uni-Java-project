<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="UTF-8" />
    <title>Logowanie</title>
</head>
<body>
    <h1>Zaloguj się</h1>
    <% String errorMessage = (String) request.getAttribute("errorMessage");
       if (errorMessage != null) { %>
        <p style="color:red;"><%= errorMessage %></p>
    <% } %>
    <form action="login" method="post">
        <label for="username">Nazwa użytkownika:</label>
        <input type="text" id="username" name="username" required /><br/>

        <label for="password">Hasło:</label>
        <input type="password" id="password" name="password" required /><br/>

        <button type="submit">Zaloguj</button>
    </form>
</body>
</html>
