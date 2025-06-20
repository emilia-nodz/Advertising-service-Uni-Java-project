package com.demo.controllers;

import com.demo.bean.UserBean;
import com.demo.models.User;
import com.demo.models.UserRole;
import com.demo.services.MessageSender;
import com.demo.services.UserService;
import com.demo.util.JSF;

import jakarta.ejb.EJB;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.IOException;
import java.io.Serializable;

@Named("registerController")
@ViewScoped
public class RegisterController implements Serializable {

    private String username;
    private String password;
    private String email;
    private String name;
    private String surname;

    @EJB
    private UserService userService;

    @EJB
    private MessageSender messageSender;

    @Inject
    private UserBean userBean;

    // Gettery i settery:
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }

    public void register() throws IOException {
        try {
            // Sprawdzenie, czy użytkownik z podanym mailem istnieje:
            if (userService.findByEmail(email) != null) {
                JSF.addErrorMessage("Użytkownik z takim adresem e-mail już istnieje!");
                return;
            }

            // Sprawdzenie, czy użytkownik z podanym username istnieje:
            if (userService.findByLogin(username) != null) {
                JSF.addErrorMessage("Użytkownik z taką nazwą użytkownika już istnieje!");
                return;
            }

            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setEmail(email);
            user.setName(name);
            user.setSurname(surname);
            user.setRole(UserRole.USER);
            userService.save(user);

            String email = user.getEmail();
            String subject = "Rejestracja zakończona sukcesem";
            String body = "Witaj " + user.getName() + " na naszym serwisie!";
            messageSender.send(email, subject, body);

            JSF.addInfoMessage("Rejestracja zakończona sukcesem!");
            JSF.redirect("login.xhtml");
        } catch (Exception e) {
            JSF.addErrorMessage("Błąd rejestracji: " + e.getMessage());
        }
    }

    // Metody do testów: (ze względu na problemy z mockowaniem JSF/FacesContext)
    public boolean registerUser(String username, String password, String email, String name, String surname) {
        try {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setEmail(email);
            user.setName(name);
            user.setSurname(surname);
            user.setRole(UserRole.USER);

            userService.save(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
