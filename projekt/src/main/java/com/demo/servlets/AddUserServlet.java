package com.demo.servlets;

import com.demo.dao.UserDAO;
import com.demo.models.*;

import jakarta.inject.Inject;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.transaction.Transactional;

import java.io.IOException;

@WebServlet("/addUser")
public class AddUserServlet extends HttpServlet {
    @Inject
    private UserDAO userDAO;

    @Override
    @Transactional
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        UserRole role = UserRole.valueOf(request.getParameter("role"));

        try {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setEmail(email);
            user.setName(name);
            user.setSurname(surname);
            user.setRole(role);

            userDAO.save(user);

            response.sendRedirect("addUser.html");

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}