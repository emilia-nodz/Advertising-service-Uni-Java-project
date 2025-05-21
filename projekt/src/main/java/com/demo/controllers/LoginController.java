package com.demo.controllers;

import com.demo.bean.UserBean;
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

import static jakarta.security.enterprise.AuthenticationStatus.SEND_CONTINUE;

@Named
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
        switch (continueAuthentication()) {
            case SEND_CONTINUE:
                facesContext.responseComplete();
                break;
            case SEND_FAILURE:
                JSF.addErrorMessage("Niepoprawne dane");
                break;
            case SUCCESS:
                JSF.redirect("index.xhtml");
                break;
            case NOT_DONE:
        }
    }

    public void onLogout() throws IOException {
        JSF.invalidateSession();
        JSF.redirect("index.xhtml");;
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
}
