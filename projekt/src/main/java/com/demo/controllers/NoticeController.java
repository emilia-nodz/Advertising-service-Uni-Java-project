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
import java.util.Date;
import java.util.List;

@Named
@ViewScoped
public class NoticeController implements Serializable {

    @Inject
    private NoticeService noticeService;

    @Inject
    private UserService userService;

    @Inject
    private UserBean userBean;

    private Notice newNotice = new Notice();

    @EJB
    private MessageSender messageSender;

    private Notice currentNotice;
    private List<Notice> notices;
    private List<Notice> searchResults;

    // Search parameters
    private String searchTitle;
    private Date searchPublishedDate;
    private User searchAuthor;
    private Category searchCategory;

    private Long selectedNoticeId;

    public void loadAllNotices() {
        notices = noticeService.findAll();
        searchResults = null;
    }

    @PostConstruct
    public void init() {
        loadAllNotices();
        newNotice = new Notice();
        currentNotice = new Notice();
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

    public void deleteNotice(Long id) {
        try {
            noticeService.delete(id);
            loadAllNotices();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Ogłoszenie usunięto"));
        } catch (IllegalArgumentException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
        }
    }

    public void search() {
        if (searchTitle != null && !searchTitle.isEmpty()) {
            searchResults = noticeService.findByTitle(searchTitle);
        } else if (searchPublishedDate != null) {
            searchResults = noticeService.findByPublishedDate(searchPublishedDate);
        } else if (searchAuthor != null) {
            searchResults = noticeService.findByAuthor(searchAuthor);
        } else if (searchCategory != null) {
            searchResults = noticeService.findByCategory(searchCategory);
        } else {
            searchResults = noticeService.findAll();
        }
    }

    public void clearSearch() {
        searchTitle = null;
        searchPublishedDate = null;
        searchAuthor = null;
        searchCategory = null;
        searchResults = null;
    }

    // Getters and Setters
    public Notice getCurrentNotice() {
        return currentNotice;
    }

    public void setCurrentNotice(Notice currentNotice) {
        this.currentNotice = currentNotice;
    }

    public List<Notice> getNotices() {
        return notices;
    }

    public List<Notice> getSearchResults() {
        return searchResults;
    }

    public String getSearchTitle() {
        return searchTitle;
    }

    public void setSearchTitle(String searchTitle) {
        this.searchTitle = searchTitle;
    }

    public Date getSearchPublishedDate() {
        return searchPublishedDate;
    }

    public void setSearchPublishedDate(Date searchPublishedDate) {
        this.searchPublishedDate = searchPublishedDate;
    }

    public User getSearchAuthor() {
        return searchAuthor;
    }

    public void setSearchAuthor(User searchAuthor) {
        this.searchAuthor = searchAuthor;
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