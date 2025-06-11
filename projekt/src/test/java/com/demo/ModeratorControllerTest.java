package com.demo;

import com.demo.bean.UserBean;
import com.demo.controllers.ModeratorController;
import com.demo.models.Notice;
import com.demo.models.User;
import com.demo.services.MessageSender;
import com.demo.services.NoticeService;
import com.demo.util.JSF;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ModeratorControllerTest {

    @Mock
    private NoticeService noticeService;

    @Mock
    private MessageSender messageSender;

    @Mock
    private UserBean userBean;

    @Mock
    private FacesContext facesContext;

    @InjectMocks
    private ModeratorController moderatorController;

    private List<Notice> testNotices;
    private Notice testNotice;
    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = mock(User.class);
        when(testUser.getEmail()).thenReturn("test@example.com");

        testNotice = mock(Notice.class);
        when(testNotice.getId()).thenReturn(1L);
        when(testNotice.getTitle()).thenReturn("Test Notice");
        when(testNotice.getAuthor()).thenReturn(testUser);
        when(testNotice.getWasModerated()).thenReturn(false);

        testNotices = Arrays.asList(testNotice);
    }

    @Test
    void init_shouldLoadNotices() {
        when(noticeService.findAll()).thenReturn(testNotices);

        moderatorController.init();

        verify(noticeService).findAll();
        assertEquals(testNotices, moderatorController.getFilteredNotices());
    }

    @Test
    void loadNotices_withAllFilter_shouldLoadAllNotices() {
        moderatorController.setFilterStatus("all");
        when(noticeService.findAll()).thenReturn(testNotices);

        moderatorController.loadNotices();

        verify(noticeService).findAll();
        assertEquals(testNotices, moderatorController.getFilteredNotices());
    }

    @Test
    void loadNotices_withVerifiedFilter_shouldLoadModeratedNotices() {
        moderatorController.setFilterStatus("verified");
        when(noticeService.findModerated()).thenReturn(testNotices);

        moderatorController.loadNotices();

        verify(noticeService).findModerated();
        assertEquals(testNotices, moderatorController.getFilteredNotices());
    }

    @Test
    void loadNotices_withUnverifiedFilter_shouldLoadNotModeratedNotices() {
        moderatorController.setFilterStatus("unverified");
        when(noticeService.findNotModerated()).thenReturn(testNotices);

        moderatorController.loadNotices();

        verify(noticeService).findNotModerated();
        assertEquals(testNotices, moderatorController.getFilteredNotices());
    }

    @Test
    void checkAccess_withNullUser_shouldRedirect() throws IOException {
        when(userBean.getUser()).thenReturn(null);

        try (MockedStatic<JSF> jsfMock = mockStatic(JSF.class);
             MockedStatic<FacesContext> facesContextMock = mockStatic(FacesContext.class)) {

            facesContextMock.when(FacesContext::getCurrentInstance).thenReturn(facesContext);

            moderatorController.checkAccess();

            jsfMock.verify(() -> JSF.redirect("login.xhtml"));
            verify(facesContext).responseComplete();
        }
    }

    @Test
    void checkAccess_withNonModerator_shouldRedirect() throws IOException {
        when(userBean.getUser()).thenReturn(testUser);
        when(userBean.isModerator()).thenReturn(false);

        try (MockedStatic<JSF> jsfMock = mockStatic(JSF.class);
             MockedStatic<FacesContext> facesContextMock = mockStatic(FacesContext.class)) {

            facesContextMock.when(FacesContext::getCurrentInstance).thenReturn(facesContext);

            moderatorController.checkAccess();

            jsfMock.verify(() -> JSF.redirect("login.xhtml"));
            verify(facesContext).responseComplete();
        }
    }

    @Test
    void checkAccess_withModerator_shouldNotRedirect() throws IOException {
        when(userBean.getUser()).thenReturn(testUser);
        when(userBean.isModerator()).thenReturn(true);

        try (MockedStatic<JSF> jsfMock = mockStatic(JSF.class)) {
            moderatorController.checkAccess();
            jsfMock.verifyNoInteractions();
        }
    }

    @Test
    void toggleVerification_fromUnverifiedToVerified_shouldUpdateAndSendEmail() {
        when(noticeService.findAll()).thenReturn(testNotices);

        try (MockedStatic<FacesContext> facesContextMock = mockStatic(FacesContext.class)) {
            facesContextMock.when(FacesContext::getCurrentInstance).thenReturn(facesContext);

            moderatorController.toggleVerification(testNotice);

            verify(testNotice).setWasModerated(true);
            verify(noticeService).update(testNotice);
            verify(messageSender).send("test@example.com",
                    "Twoje ogłoszenie zostało zaakceptowane",
                    "Ogłoszenie 'Test Notice' zostało zaakceptowane przez moderatora");
            verify(facesContext).addMessage(eq(null), any(FacesMessage.class));
        }
    }

    @Test
    void toggleVerification_fromVerifiedToUnverified_shouldUpdateButNotSendEmail() {
        when(testNotice.getWasModerated()).thenReturn(true);
        when(noticeService.findAll()).thenReturn(testNotices);

        try (MockedStatic<FacesContext> facesContextMock = mockStatic(FacesContext.class)) {
            facesContextMock.when(FacesContext::getCurrentInstance).thenReturn(facesContext);

            moderatorController.toggleVerification(testNotice);

            verify(testNotice).setWasModerated(false);
            verify(noticeService).update(testNotice);
            verify(messageSender, never()).send(anyString(), anyString(), anyString());
            verify(facesContext).addMessage(eq(null), any(FacesMessage.class));
        }
    }

    @Test
    void rejectNotice_shouldDeleteAndSendEmail() {
        when(noticeService.findAll()).thenReturn(testNotices);

        try (MockedStatic<FacesContext> facesContextMock = mockStatic(FacesContext.class)) {
            facesContextMock.when(FacesContext::getCurrentInstance).thenReturn(facesContext);

            moderatorController.rejectNotice(testNotice);

            verify(noticeService).delete(1L);
            verify(messageSender).send("test@example.com",
                    "Twoje ogłoszenie zostało odrzucone",
                    "Ogłoszenie 'Test Notice' zostało odrzucone przez moderatora");
            verify(facesContext).addMessage(eq(null), any(FacesMessage.class));
        }
    }

    @Test
    void setFilterStatus_shouldUpdateFilterAndReloadNotices() {
        when(noticeService.findModerated()).thenReturn(testNotices);

        moderatorController.setFilterStatus("verified");

        assertEquals("verified", moderatorController.getFilterStatus());
        verify(noticeService).findModerated();
        assertEquals(testNotices, moderatorController.getFilteredNotices());
    }

    @Test
    void getFilteredNotices_shouldReturnCurrentList() {
        when(noticeService.findAll()).thenReturn(testNotices);
        moderatorController.loadNotices();

        List<Notice> result = moderatorController.getFilteredNotices();

        assertEquals(testNotices, result);
    }

    @Test
    void getFilterStatus_shouldReturnCurrentStatus() {
        moderatorController.setFilterStatus("verified");

        String result = moderatorController.getFilterStatus();

        assertEquals("verified", result);
    }
}