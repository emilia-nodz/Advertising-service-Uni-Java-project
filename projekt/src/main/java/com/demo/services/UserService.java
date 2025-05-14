package com.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.demo.dao.UserDAO;
import com.demo.models.User;

@Service
public class UserService {
    @Autowired
    private UserDAO userDAO;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Rejestracja użytkownika
    public void register(User user) throws Exception {
        if (userDAO.findByUsername(user.getUsername()) != null) {
            throw new Exception("Username already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDAO.create(user);
    }

    // Logowanie użytkownika
    public boolean login(String username, String password) {
        User user = userDAO.findByUsername(username);
        if (user != null) {
            return passwordEncoder.matches(password, user.getPassword());
        }
        return false;
    }
}
