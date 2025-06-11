package com.demo.controllers;

import com.demo.bean.UserBean;
import com.demo.models.Category;
import com.demo.models.Notice;
import com.demo.models.User;
import com.demo.services.MessageSender;
import com.demo.services.NoticeService;
import com.demo.services.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Named
@ViewScoped
public class NoticeController implements Serializable {

    @Inject
    private NoticeService noticeService;

    @Inject
    private UserService userService;

    @Inject
    private UserBean userBean;

    @EJB
    private MessageSender messageSender;

    private Notice newNotice = new Notice();
    private Notice selectedNotice;
    private List<Notice> notices;
    private List<Notice> moderatedNotices;
    private List<Notice> notModeratedNotices;
    private List<Notice> moderatedNoticesByUser;
    private List<Notice> notModeratedNoticesByUser;
    private List<Notice> searchResults;

    private Category searchCategory;

    private Long selectedNoticeId;

    public void loadModeratedNotices() {
        this.moderatedNotices = noticeService.findModerated();
        searchResults = null;
    }

    public void loadAllNotices() {
        notices = noticeService.findAll();
        searchResults = null;
    }

    public void loadNotModeratedNotices() {
        notModeratedNotices = noticeService.findNotModerated();
        searchResults = null;
    }

    public void loadModeratedNoticesByUser() {
        if (userBean.getUser() != null) {
            User loggedUser = userService.findByLogin(userBean.getUser().getUsername());
            this.moderatedNoticesByUser = noticeService.findModeratedByUser(loggedUser);
        } else {
            this.moderatedNoticesByUser = Collections.emptyList();
        }
    }

    public void loadNotModeratedNoticesByUser() {
        if (userBean.getUser() != null) {
            User loggedUser = userService.findByLogin(userBean.getUser().getUsername());
            this.notModeratedNoticesByUser = noticeService.findNotModeratedByUser(loggedUser);
        } else {
            this.notModeratedNoticesByUser = Collections.emptyList();
        }
    }

    @PostConstruct
    public void init() {
        loadModeratedNotices();
        loadNotModeratedNotices();
        loadNotModeratedNoticesByUser();
        loadModeratedNoticesByUser();
    }

    public Notice getNewNotice() {
        return newNotice;
    }

    public void setNewNotice(Notice newNotice) {
        this.newNotice = newNotice;
    }

    public void addNotice() {
        try {
            User loggedUser = userService.findByLogin(userBean.getUser().getUsername());
            if (loggedUser == null) {
                throw new IllegalStateException("Brak zalogowanego użytkownika");
            }
            newNotice.setAuthor(loggedUser);
            noticeService.save(newNotice);
            notices = noticeService.findAll();
            newNotice = new Notice();

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Dodano ogłoszenie."));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Błąd", e.getMessage()));
        }
    }

    public void delete(Notice notice) {
        noticeService.delete(notice.getId());
        loadNotModeratedNotices();
        loadModeratedNotices();
    }

    public void search() {
        FacesContext context = FacesContext.getCurrentInstance();

        if (searchCategory != null) {
            moderatedNotices = noticeService.findModeratedByCategory(searchCategory);
            context.addMessage(null, new FacesMessage(
                    "Znaleziono " + moderatedNotices.size() +
                            " ogłoszeń w kategorii: " + searchCategory.getName()));
        } else {
            loadModeratedNotices();
            context.addMessage(null, new FacesMessage(
                    "Wyświetlam wszystkie ogłoszenia: " + moderatedNotices.size()));
        }
    }

    public void clearFilter() {
        searchCategory = null;
        loadModeratedNotices();
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage("Filtry wyczyszczone. Wyświetlam wszystkie ogłoszenia."));
    }

    public void update() {
        if (selectedNotice != null) {
            noticeService.update(selectedNotice);
        }
    }

    // Gettery i Settery
    public Notice getSelectedNotice() {
        return selectedNotice;
    }

    public void setSelectedNotice(Notice selectedNotice) {
        this.selectedNotice = selectedNotice;
    }

    public List<Notice> getNotices() {
        return notices;
    }

    public List<Notice> getModeratedNotices() {
        return moderatedNotices;
    }

    public List<Notice> getNotModeratedNotices() {
        return notModeratedNotices;
    }

    public List<Notice> getModeratedNoticesByUser() {
        return moderatedNoticesByUser;
    }

    public List<Notice> getNotModeratedNoticesByUser() {
        return notModeratedNoticesByUser;
    }

    public List<Notice> getSearchResults() {
        return searchResults;
    }

    public Category getSearchCategory() {
        return searchCategory;
    }

    public void setSearchCategory(Category searchCategory) {
        this.searchCategory = searchCategory;
    }

    public Long getSelectedNoticeId() {
        return selectedNoticeId;
    }

    public void setSelectedNoticeId(Long selectedNoticeId) {
        this.selectedNoticeId = selectedNoticeId;
    }

}