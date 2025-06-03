
package com.demo.controllers;

import com.demo.models.Category;
import com.demo.models.Notice;
import com.demo.models.User;
import com.demo.services.NoticeService;
import com.demo.services.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
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

    private Notice currentNotice;
    private List<Notice> notices;
    private List<Notice> searchResults;

    // Search parameters
    private String searchTitle;
    private Date searchPublishedDate;
    private User searchAuthor;
    private Category searchCategory;

    // Automatically get logged-in user from session
    private User getCurrentUser() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
        return (User) sessionMap.get("currentUser");
    }

    @PostConstruct
    public void init() {
        loadAllNotices();
        prepareCreate(); // Initialize with new notice
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
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Błąd", "Nie jesteś zalogowany!"));
            return;
        }

        try {
            // REATTACH USER HERE (CRITICAL FIX)
            if (currentNotice.getId() == null) { // Only for new notices
                User managedUser = userService.findById(currentUser.getId());
                currentNotice.setAuthor(managedUser); // Reattach user!
            }

            if (currentNotice.getId() == null) {
                noticeService.save(currentNotice);
                // ... success
            } else {
                noticeService.update(currentNotice);
                // ... success
            }
            prepareCreate();
            loadAllNotices();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Błąd", e.getMessage()));
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

}
