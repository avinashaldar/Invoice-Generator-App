package com.invoiceapp.servlet;

import com.invoiceapp.dao.UserDAO;
import com.invoiceapp.model.User;
import java.io.IOException;

import javax.servlet.annotation.WebServlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");

        User user = new User(name, email, phone, password);
        UserDAO dao = new UserDAO();
        boolean registered = dao.registerUser(user);

        if (registered) {
            response.sendRedirect("login.jsp?success=registered");
        } else {
            response.sendRedirect("register.jsp?error=failed");
        }
    }
}
