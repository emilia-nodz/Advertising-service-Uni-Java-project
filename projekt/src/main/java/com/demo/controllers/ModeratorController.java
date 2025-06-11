package com.demo.controllers;

import com.demo.bean.UserBean;
import com.demo.models.Notice;
import com.demo.services.NoticeService;
import com.demo.util.JSF;
import jakarta.annotation.PostConstruct;
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

    @Inject
    private NoticeService noticeService;

    @Inject
    private UserBean userBean;

    private List<Notice> allNotices;

    @PostConstruct
    public void init() {
        loadAllNotices();
    }

    public void loadAllNotices() {
        allNotices = noticeService.findAll();
    }

    public void checkAccess() throws IOException {
        if (userBean.getUser() == null || !userBean.isModerator()) {
            JSF.redirect("login.xhtml");
            FacesContext.getCurrentInstance().responseComplete();
        }
    }

    public void updateNotice(Notice notice) {
        try {
            noticeService.update(notice);
            loadAllNotices();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Ogłoszenie zostało zaktualizowane", null));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Błąd podczas aktualizacji ogłoszenia", e.getMessage()));
        }
    }

    public void deleteNotice(Notice notice) {
        try {
            noticeService.delete(notice.getId());
            loadAllNotices();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Ogłoszenie zostało usunięte", null));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Błąd podczas usuwania ogłoszenia", e.getMessage()));
        }
    }

    // Gettery
    public List<Notice> getAllNotices() {
        return allNotices;
    }

    // Metody do testów (ze względu na problemy z mockowaniem JSF/FacesContext)
    public boolean isUserModerator() {
        return userBean.getUser() != null && userBean.isModerator();
    }

    public boolean updateNoticeTest(Notice notice) {
        try {
            noticeService.update(notice);
            loadAllNotices();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean deleteNoticeTest(Notice notice) {
        try {
            noticeService.delete(notice.getId());
            loadAllNotices();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}