<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Register - InvoiceApp</title>
    <link rel="stylesheet" href="css/styles.css">
</head>
<body>
<div class="auth-container">
    <h2>Create Account</h2>

    <% if (request.getAttribute("error") != null) { %>
        <div class="error"><%= request.getAttribute("error") %></div>
    <% } %>

	<form method="post" action="<%=request.getContextPath()%>/register">
        <label>Name</label>
        <input type="text" name="name" required>

        <label>Email</label>
        <input type="email" name="email" required>

        <label>Phone</label>
        <input type="text" name="phone" required>

        <label>Password</label>
        <input type="password" name="password" required>

        <button type="submit">Register</button>
        <p>Already registered? <a href="login.jsp">Login</a></p>
    </form>
</div>
</body>
</html>
