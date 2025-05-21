package com.demo.bean;

import com.demo.models.User;
import com.demo.services.UserService;
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

    public boolean isLogged() {
        return getUsername() != null;
    }

    public String getUsername() {
        if (user != null) {
            return user.getUsername();
        }

        Principal principal = securityContext.getCallerPrincipal();
        if (principal != null) {
            user = userService.findByLogin(principal.getName());
        }

        return user != null ? user.getUsername() : null;
    }
}
