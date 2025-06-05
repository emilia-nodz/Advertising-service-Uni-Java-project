package com.demo.controllers;

import com.demo.models.Category;
import com.demo.models.Notice;
import com.demo.models.User;
import com.demo.services.MessageSender;
import com.demo.services.NoticeService;
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

    @PostConstruct
    public void init() {
        loadAllNotices();
        currentNotice = new Notice();
    }

    public void loadAllNotices() {
        notices = noticeService.findAll();
        searchResults = null;
    }

    public void prepareCreate() {
        currentNotice = new Notice();
        currentNotice.setPublishDate(new Date());
    }

    public void prepareUpdate(Long id) {
        noticeService.findById(id).ifPresent(notice -> {
            currentNotice = notice;
        });
    }

    public void saveNotice() {
        try {
            if (currentNotice.getId() == null) {
                noticeService.save(currentNotice);
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Poprawnie utworzono ogłoszenie"));
            } else {
                noticeService.update(currentNotice);
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Poprawnie utworzono ogłoszenie"));
            }
            loadAllNotices();
        } catch (IllegalArgumentException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
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