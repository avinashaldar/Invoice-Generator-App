<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login - InvoiceApp</title>
    <link rel="stylesheet" href="css/styles.css">
</head>
<body>
<div class="auth-container">
    <h2>Login</h2>

    <% if (request.getAttribute("error") != null) { %>
        <div class="error"><%= request.getAttribute("error") %></div>
    <% } %>

    <form method="post" action="<%=request.getContextPath()%>/login">
        <label>Email</label>
        <input type="email" name="email" required>

        <label>Password</label>
        <input type="password" name="password" required>

        <button type="submit">Login</button>
        <p>Don’t have an account? <a href="register.jsp">Register</a></p>
    </form>
</div>
</body>
</html>
