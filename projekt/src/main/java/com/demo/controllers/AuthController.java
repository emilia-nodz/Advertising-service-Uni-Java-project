package com.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.models.User;
import com.demo.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    // Rejestracja użytkownika
    @PostMapping("/register")
    public String registerUser(@RequestBody @Valid User user) {
        try {
            userService.register(user);
            return "Zarejestrowano użytkownika pomyślnie!";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    // Logowanie użytkownika
    @PostMapping("/login")
    public String loginUser(@RequestBody User user) {
        try {
            boolean isAuthenticated = userService.login(user.getUsername(), user.getPassword());
            if (isAuthenticated) {
                return "Zalogowano pomyślnie";
            } else {
                return "Niepoprawne dane";
            }
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}