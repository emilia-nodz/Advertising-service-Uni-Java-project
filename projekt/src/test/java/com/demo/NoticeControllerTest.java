package com.demo;

import com.demo.bean.UserBean;
import com.demo.controllers.AdminController;
import com.demo.controllers.NoticeController;
import com.demo.models.Category;
import com.demo.models.Notice;
import com.demo.models.User;
import com.demo.services.NoticeService;
import com.demo.services.UserService;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class NoticeControllerTest {
    @InjectMocks
    private NoticeController noticeController;

    @Mock
    private UserService userService;

    @Mock
    private NoticeService noticeService;

    @Mock
    private UserBean userBean;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test sprawdzający czy ogłoszenia są poprawnie ładowane dla użytkownika
    @Test
    void test_load_moderated_notices_by_user() {
        // Given
        User mockUser = new User();
        mockUser.setUsername("testuser");
        List<Notice> expectedNotices = List.of(
                new Notice(1L, "Notice 1", "Desc 1", 0, mockUser, null),
                new Notice(2L, "Notice 2", "Desc 2", 0, mockUser, null)
        );

        when(userBean.getUser()).thenReturn(mockUser);
        when(userService.findByLogin("testuser")).thenReturn(mockUser);
        when(noticeService.findModeratedByUser(mockUser)).thenReturn(expectedNotices);

        noticeController.loadModeratedNoticesByUser();

        assertNotNull(noticeController.getModeratedNoticesByUser());
        assertEquals(2, noticeController.getModeratedNoticesByUser().size());

        verify(userBean, times(2)).getUser();
        verify(userService).findByLogin("testuser");
        verify(noticeService).findModeratedByUser(mockUser);
    }

    // Test sprawdzający czy ogłoszenie dodaje się gdy użytkownik jest zalogowany
    @Test
    void test_add_notice_when_user_logged_in() {
        User mockUser = new User();
        mockUser.setUsername("testuser");
        Notice testNotice = new Notice(1L, "Notice 1", "Desc 1", 0, mockUser, null);

        when(userBean.getUser()).thenReturn(mockUser);
        when(userService.findByLogin("testuser")).thenReturn(mockUser);

        ArgumentCaptor<Notice> noticeCaptor = ArgumentCaptor.forClass(Notice.class);
        when(noticeService.save(noticeCaptor.capture())).thenReturn(testNotice);

        noticeController.setNewNotice(testNotice);
        noticeController.addNotice();

        Notice savedNotice = noticeCaptor.getValue();
        assertEquals(mockUser, savedNotice.getAuthor());
        verify(noticeService).save(any(Notice.class));
        verify(noticeService).findAll();

        assertNotNull(noticeController.getNewNotice());
        assertNotSame(testNotice, noticeController.getNewNotice());
    }


    // Test sprawdzający czy zostanie wyrzucony błąd, gdy użytkownik nie jest zalogowany a próbuje dodać ogłoszenie
    @Test
    void test_throw_exception_when_adding_and_not_logged_in() {
        Notice testNotice = new Notice(1L, "Notice 1", "Desc 1", 0, null, null);

        when(userBean.getUser()).thenReturn(null);

        noticeController.setNewNotice(testNotice);
        noticeController.addNotice();

        verify(noticeService, never()).save(any());

        assertEquals(testNotice, noticeController.getNewNotice());
    }

    // Test sprawdzający czy ogłoszenie się usuwa i listy się aktualizują
    @Test
    void test_delete_notice() {
        Notice testNotice = new Notice();
        testNotice.setId(1L);

        doNothing().when(noticeService).delete(1L);

        List<Notice> moderatedList = List.of(new Notice());
        List<Notice> unmoderatedList = List.of(new Notice());
        when(noticeService.findModerated()).thenReturn(moderatedList);
        when(noticeService.findNotModerated()).thenReturn(unmoderatedList);

        noticeController.delete(testNotice);

        verify(noticeService).delete(1L);

        verify(noticeService).findModerated();
        verify(noticeService).findNotModerated();

        assertEquals(moderatedList, noticeController.getModeratedNotices());
        assertEquals(unmoderatedList, noticeController.getNotModeratedNotices());

        verifyNoMoreInteractions(noticeService);
    }

    // Test sprawdzający czy wybrane ogłoszenie poprawnie się aktualizuje
    @Test
    void update_notice_when_selected() {
        Notice mockNotice = new Notice(1L, "Notice", "Desc", 0, null, null);
        noticeController.setSelectedNotice(mockNotice);

        mockNotice.setTitle("Updated Title");
        mockNotice.setDescription("Updated Description");
        mockNotice.setAmount(5);

        Notice expectedNotice = new Notice(1L, "Updated Title", "Updated Description", 5, null, null);

        ArgumentCaptor<Notice> noticeCaptor = ArgumentCaptor.forClass(Notice.class);
        when(noticeService.update(noticeCaptor.capture())).thenReturn(expectedNotice);

        noticeController.update();

        verify(noticeService).update(any(Notice.class));

        Notice actualUpdatedNotice = noticeCaptor.getValue();

        assertEquals(expectedNotice.getId(), actualUpdatedNotice.getId());
        assertEquals(expectedNotice.getTitle(), actualUpdatedNotice.getTitle());
        assertEquals(expectedNotice.getDescription(), actualUpdatedNotice.getDescription());
        assertEquals(expectedNotice.getAmount(), actualUpdatedNotice.getAmount());

        assertSame(mockNotice, noticeController.getSelectedNotice());
    }
}
