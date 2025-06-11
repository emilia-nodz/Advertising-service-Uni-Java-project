package com.demo.controllers;

import com.demo.bean.UserBean;
import com.demo.models.Notice;
import com.demo.services.EmailService;
import com.demo.services.MessageSender;
import com.demo.services.NoticeService;
import com.demo.util.JSF;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

@Named("moderatorController")
@ViewScoped
public class ModeratorController implements Serializable {

    @EJB
    private NoticeService noticeService;

    @EJB
    private MessageSender messageSender;

    @Inject
    private UserBean userBean;

    private List<Notice> filteredNotices;
    private String filterStatus = "all";

    @PostConstruct
    public void init() {
        loadNotices();
    }

    public void loadNotices() {
        switch (filterStatus) {
            case "verified":
                filteredNotices = noticeService.findModerated();
                break;
            case "unverified":
                filteredNotices = noticeService.findNotModerated();
                break;
            default:
                filteredNotices = noticeService.findAll();
        }
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
        loadNotices();
        if (notice.getWasModerated()) {
            String recipient = notice.getAuthor().getEmail();
            String subject = "Twoje ogłoszenie zostało zaakceptowane";
            String content = "Ogłoszenie '" + notice.getTitle() + "' zostało zaakceptowane przez moderatora";
            messageSender.send(recipient, subject, content);
        }
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage("Status weryfikacji zmieniony"));
    }

    public void rejectNotice(Notice notice) {
        String recipient = notice.getAuthor().getEmail();
        String title = notice.getTitle();

        noticeService.delete(notice.getId());
        loadNotices();

        String subject = "Twoje ogłoszenie zostało odrzucone";
        String content = "Ogłoszenie '" + title + "' zostało odrzucone przez moderatora";
        messageSender.send(recipient, subject, content);

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage("Ogłoszenie zostało odrzucone i usunięte"));
    }


    public List<Notice> getFilteredNotices() {
        return filteredNotices;
    }

    public String getFilterStatus() {
        return filterStatus;
    }

    public void setFilterStatus(String filterStatus) {
        this.filterStatus = filterStatus;
        loadNotices();
    }
}