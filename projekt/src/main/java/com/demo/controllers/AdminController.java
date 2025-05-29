package com.demo.controllers;

import com.demo.bean.UserBean;
import com.demo.services.*;
import com.demo.models.*;
import com.demo.util.JSF;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.IOException;
import java.io.Serializable;
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

    public List<User> getAllUsers() {
        return allUsers;
    }

    public void checkAccess() throws IOException {
        if (userBean.getUser() == null || !userBean.isAdmin()) {
            JSF.redirect("login.xhtml");
            FacesContext.getCurrentInstance().responseComplete();
        }
    }
}
