package com.demo.controllers;

import com.demo.bean.UserBean;
import com.demo.models.Notice;
import com.demo.services.NoticeService;
import com.demo.util.JSF;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

@Named("moderatorController")
@SessionScoped
public class ModeratorController implements Serializable {

    @EJB
    private NoticeService noticeService;

    @Inject
    private UserBean userBean;

    private List<Notice> allNotices;

    @PostConstruct
    public void init() {
        loadNotices();
    }

    public void loadNotices() {
        allNotices = noticeService.findAll();
    }

    public List<Notice> getAllNotices() {
        return allNotices;
    }

    public void checkAccess() throws IOException {
        if (userBean.getUser() == null || !userBean.isModerator()) {
            JSF.redirect("login.xhtml");
            FacesContext.getCurrentInstance().responseComplete();
        }
    }

    public void toggleVerification(Notice notice) {
        notice.setWasModerated(!notice.getWasModerated());
        noticeService.update(notice);
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Zmieniono status weryfikacji",
                        null));
    }
}