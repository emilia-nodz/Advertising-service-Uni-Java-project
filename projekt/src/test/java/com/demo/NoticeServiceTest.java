package com.demo;

import com.demo.dao.NoticeDAO;
import com.demo.models.Notice;
import com.demo.models.User;
import com.demo.services.MessageSender;
import com.demo.services.NoticeService;
import com.demo.services.NoticeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class NoticeServiceTest {
    @Mock
    private NoticeDAO noticeDao;

    @Mock
    private MessageSender messageSender;

    @InjectMocks
    private NoticeServiceImpl noticeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test sprawdzający czy są poprawnie usuwane ogłoszenia po przekroczeniu daty
    @Test
    void test_remove_expired_notices() {
        noticeService.removeExpiredNotices();

        verify(noticeDao).deleteByTerminationDate();
        verifyNoMoreInteractions(noticeDao);
        verifyNoInteractions(messageSender);
    }

    // Test sprawdzający czy poprawnie wysyłane są powiadomienia
    @Test
    void test_send_expiration_notifications() {
        Date tomorrow = Date.from(LocalDate.now()
                .plusDays(1)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant());

        User user1 = new User();
        user1.setEmail("user1@example.com");

        User user2 = new User();
        user2.setEmail("user2@example.com");

        Notice notice1 = new Notice();
        notice1.setTitle("Notice 1");
        notice1.setTerminationDate(tomorrow);
        notice1.setAuthor(user1);

        Notice notice2 = new Notice();
        notice2.setTitle("Notice 2");
        notice2.setTerminationDate(tomorrow);
        notice2.setAuthor(user2);

        when(noticeDao.findByTerminationDate(any(Date.class))).thenReturn(List.of(notice1, notice2));

        noticeService.sendExpirationNotifications();

        ArgumentCaptor<Date> dateCaptor = ArgumentCaptor.forClass(Date.class);
        verify(noticeDao).findByTerminationDate(dateCaptor.capture());

        LocalDate capturedDate = dateCaptor.getValue().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        assertEquals(LocalDate.now().plusDays(1), capturedDate);

        verify(messageSender, times(2)).send(anyString(), anyString(), anyString());
    }
}
