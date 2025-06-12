package com.demo.controllers;

import com.demo.bean.UserBean;
import com.demo.models.Notice;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

@Named("moderatorController")
@ViewScoped
public class ModeratorController implements Serializable {

    private static final Logger logger = LogManager.getLogger(ModeratorController.class);

    @EJB
    public NoticeService noticeService;

    @EJB
    public MessageSender messageSender;


    @Inject
    public UserBean userBean;

    public List<Notice> filteredNotices;
    private String filterStatus = "all";

    @PostConstruct
    public void init() {
        logger.info("ModeratorController initialized");
        loadNotices();
    }

    public void loadNotices() {
        logger.info("Loading notices with filterStatus = {}", filterStatus);
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
        logger.info("Loaded {} notices", filteredNotices.size());
    }

    public void checkAccess() throws IOException {
        if (userBean.getUser() == null || !userBean.isModerator()) {
            logger.warn("Access denied for user: {}", userBean.getUser());
            JSF.redirect("login.xhtml");
            FacesContext.getCurrentInstance().responseComplete();
        } else {
            logger.info("Access granted for moderator: {}", userBean.getUser());
        }
    }

    public void toggleVerification(Notice notice) {
        try {
            boolean newStatus = !notice.getWasModerated();
            notice.setWasModerated(newStatus);
            noticeService.update(notice);
            loadNotices();
            logger.info("Notice ID {} verification toggled to {}", notice.getId(), newStatus);

            if (newStatus) {
                String recipient = notice.getAuthor().getEmail();
                String subject = "Twoje ogłoszenie zostało zaakceptowane";
                String content = "Ogłoszenie '" + notice.getTitle() + "' zostało zaakceptowane przez moderatora";
                messageSender.send(recipient, subject, content);
                logger.info("Acceptance email sent to {}", recipient);
            }
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Status weryfikacji zmieniony"));
        } catch (Exception e) {
            logger.error("Error toggling verification status for notice ID " + notice.getId(), e);
        }
    }

    public void rejectNotice(Notice notice) {
        try {
            String recipient = notice.getAuthor().getEmail();
            String title = notice.getTitle();

            noticeService.delete(notice.getId());
            loadNotices();

            String subject = "Twoje ogłoszenie zostało odrzucone";
            String content = "Ogłoszenie '" + title + "' zostało odrzucone przez moderatora";
            messageSender.send(recipient, subject, content);
            logger.info("Notice ID {} rejected and email sent to {}", notice.getId(), recipient);

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Ogłoszenie zostało odrzucone i usunięte"));
        } catch (Exception e) {
            logger.error("Error rejecting notice ID " + notice.getId(), e);
        }
    }

    public List<Notice> getFilteredNotices() {
        return filteredNotices;
    }

    public String getFilterStatus() {
        return filterStatus;
    }

    public void setFilterStatus(String filterStatus) {
        logger.info("Filter status changed from {} to {}", this.filterStatus, filterStatus);
        this.filterStatus = filterStatus;
        loadNotices();
    }

}
