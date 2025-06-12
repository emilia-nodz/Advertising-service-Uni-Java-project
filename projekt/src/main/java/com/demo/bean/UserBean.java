package com.demo.bean;

import com.demo.models.User;
import com.demo.models.UserRole;
import com.demo.services.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.security.enterprise.SecurityContext;
import java.io.Serializable;
import java.security.Principal;

@Named
@SessionScoped
public class UserBean implements Serializable {

    @EJB
    private UserService userService;

    @Inject
    private SecurityContext securityContext;

    private User user;

    @PostConstruct
    public void init() {
        Principal principal = securityContext.getCallerPrincipal();
        if (principal != null) {
            user = userService.findByLogin(principal.getName());
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isLogged() {
        return user != null;
    }

    public boolean isAdmin() {
        return user != null && user.getRole() == UserRole.ADMIN;
    }

    public boolean isModerator() {
        return user != null && user.getRole() == UserRole.MODERATOR;
    }

    public String getUsername() {
        return user != null ? user.getUsername() : null;
    }
}
