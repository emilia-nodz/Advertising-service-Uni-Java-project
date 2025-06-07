package com.demo.controllers;

import com.demo.bean.UserBean;
import com.demo.models.User;
import com.demo.services.MessageSender;
import com.demo.services.UserService;
import com.demo.util.JSF;
import jakarta.ejb.EJB;
import jakarta.faces.annotation.ManagedProperty;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.security.enterprise.AuthenticationStatus;
import jakarta.security.enterprise.SecurityContext;
import jakarta.security.enterprise.authentication.mechanism.http.AuthenticationParameters;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

@Named("loginController")
@ViewScoped
public class LoginController implements Serializable {

    @Inject
    private UserBean userBean;

    @EJB
    private UserService userService;

    @Inject
    private SecurityContext securityContext;

    @Inject
    private ExternalContext externalContext;

    @Inject
    private FacesContext facesContext;

    @Inject @ManagedProperty("#{param.new}")
    private boolean isNew;

    private String login;
    private String password;

    // Gettery i settery:
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void onLogin() throws IOException {
        try {
            if (userService.verify(login, password)) {
                User user = userService.findByLogin(login);
                userBean.setUser(user);
                JSF.redirect("index.xhtml");
            } else {
                JSF.addErrorMessage("Niepoprawna nazwa użytkownika lub hasło");
            }
        } catch (Exception e) {
            JSF.addErrorMessage("Błąd podczas logowania: " + e.getMessage());
        }
    }

    public void onLogout() throws IOException {
        userBean.setUser(null);
        JSF.invalidateSession();
        JSF.redirect("index.xhtml");
    }

    private AuthenticationStatus continueAuthentication() {
        return securityContext.authenticate(
                (HttpServletRequest) externalContext.getRequest(),
                (HttpServletResponse) externalContext.getResponse(),
                AuthenticationParameters.withParams()
                        .newAuthentication(isNew).credential(
                                new UsernamePasswordCredential(login, password))
        );
    }

    // Metody do testów: (ze względu na problemy z mockowaniem JSF/FacesContext)
    public boolean authenticate(String login, String password) {
        return userService.verify(login, password);
    }

    public void logoutUser() {
        userBean.setUser(null);
    }
}
