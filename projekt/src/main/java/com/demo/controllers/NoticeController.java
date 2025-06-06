
package com.demo.controllers;

import com.demo.bean.UserBean;
import com.demo.models.Category;
import com.demo.models.Notice;
import com.demo.models.User;
import com.demo.services.NoticeService;
import com.demo.services.UserService;
import com.demo.util.JSF;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Named
@ViewScoped
public class NoticeController implements Serializable {

    @Inject
    private NoticeService noticeService;

    @Inject
    private UserService userService;

    @Inject
    private UserBean userBean; // Inject UserBean instead of session lookup


    private Notice currentNotice;
    private List<Notice> notices;
    private List<Notice> searchResults;

    // Search parameters
    private String searchTitle;
    private Date searchPublishedDate;
    private User searchAuthor;
    private Category searchCategory;


    @PostConstruct
    public void init() {
        loadAllNotices();
        prepareCreate(); // Initialize with new notice
    }

    public void loadAllNotices() {
        notices = noticeService.findAll();
        searchResults = null;
    }

    public void prepareUpdate(Long id) {
        noticeService.findById(id).ifPresent(notice -> {
            currentNotice = notice;
        });
    }
    public void saveNotice() {
        if (userBean == null || !userBean.isLogged()) {
            addFacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Not logged in");
            return;
        }

        try {
            // Use ID only - no need to fetch the user
            User author = new User();
            author.setId(userBean.getUser().getId()); // Just set the ID

            currentNotice.setAuthor(author);
            noticeService.save(currentNotice);

            addFacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Notice saved successfully");
            prepareCreate();
            loadAllNotices();

        } catch (Exception e) {
            addFacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Save failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Change prepareCreate to not set author
    public void prepareCreate() {
        currentNotice = new Notice();
        currentNotice.setPublishDate(new Date());
    }

    // Helper method for messages
    private void addFacesMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
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

}
