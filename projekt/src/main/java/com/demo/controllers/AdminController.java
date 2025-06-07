package com.demo.controllers;

import com.demo.bean.UserBean;
import com.demo.services.*;
import com.demo.models.*;
import com.demo.util.JSF;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Named("adminController")
@SessionScoped
public class AdminController implements Serializable {

    @EJB
    private UserService userService;

    @Inject
    private UserBean userBean;

    private List<User> allUsers;

    @PostConstruct
    public void init() {
        allUsers = userService.findAll();
    }

    public void loadUsers() {
        allUsers = userService.findAll();
    }

    public List<User> getAllUsers() {
        return allUsers;
    }

    public List<UserRole> getAvailableRoles() {
        return Arrays.asList(UserRole.values());
    }

    public void checkAccess() throws IOException {
        if (userBean.getUser() == null || !userBean.isAdmin()) {
            JSF.redirect("login.xhtml");
            FacesContext.getCurrentInstance().responseComplete();
        }
    }

    public void updateUserRole(User user) {
        userService.updateUserRole(user.getId(), user.getRole());
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Zmieniono rolę użytkownika", null));
    }

    public void deleteUser(User user) {
        userService.deleteUser(user);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Usunięto użytkownika", null));
        loadUsers();
    }

    // Metody do testów: (ze względu na problemy z mockowaniem JSF/FacesContext)
    public boolean isUserAdmin() {
        return userBean.getUser() != null && userBean.isAdmin();
    }

    public boolean changeUserRole(Long userId, UserRole newRole) {
        try {
            userService.updateUserRole(userId, newRole);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void deleteU(User user) {
        userService.deleteUser(user);
        loadUsers();
    }
}
