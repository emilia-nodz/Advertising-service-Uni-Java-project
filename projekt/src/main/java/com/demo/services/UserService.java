package com.demo.services;

import com.demo.dao.UserDAO;
import com.demo.models.User;
import jakarta.inject.Inject;

import java.util.Objects;


public class UserService {
    @Inject
    private UserDAO userDAO;

    // Rejestracja użytkownika
    public void register(User user) throws Exception {
        if (userDAO.findByUsername(user.getUsername()) != null) {
            throw new Exception("Username already exists");
        }
        userDAO.create(user);
    }

    // Logowanie użytkownika
    public boolean login(String username, String password) {
        User user = userDAO.findByUsername(username);
        if (user != null && Objects.equals(password, user.getPassword())) {
            return true;
        }
        return false;
    }
}