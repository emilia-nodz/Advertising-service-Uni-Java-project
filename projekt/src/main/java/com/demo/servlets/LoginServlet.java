package com.demo.servlets;

import com.demo.dao.UserDAO;
import com.demo.models.User;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Inject
    private UserDAO userDAO;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            User user = userDAO.findByUsername(username);
            if (user != null && user.getPassword().equals(password)) {
                // Zalogowano pomyślnie, ustaw sesję
                request.getSession().setAttribute("user", user);
                response.sendRedirect("index.jsp");
            } else {
                // Niepoprawne dane logowania
                request.setAttribute("errorMessage", "Niepoprawna nazwa użytkownika lub hasło");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            throw new ServletException("Błąd podczas logowania", e);
        }
    }
}
