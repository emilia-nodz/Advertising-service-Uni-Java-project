package com.demo;

import com.demo.controllers.ModeratorController;
import com.demo.models.Notice;
import com.demo.services.NoticeService;
import com.demo.bean.UserBean;
import com.demo.services.MessageSender;
import com.demo.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Collections;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ModeratorControllerTest {
    private static final Logger logger = LogManager.getLogger(ModeratorControllerTest.class);
    @InjectMocks
    private ModeratorController moderatorController;

    @Mock
    private NoticeService noticeService;

    @Mock
    private MessageSender messageSender;

    @Mock
    private UserService userService;

    @Mock
    private UserBean userBean;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void testLoadNotices_All_CallsFindAll() {
        List<Notice> emptyList = Collections.emptyList();
        when(noticeService.findAll()).thenReturn(emptyList);
        moderatorController.setFilterStatus("all");
        List<Notice> result = moderatorController.getFilteredNotices();
        verify(noticeService, times(1)).findAll();
        assertSame(emptyList, result);
    }


    @Test
    void testSetFilterStatus_ShouldUpdateFilterAndCallLoadNotices() {
        List<Notice> mockNotices = Collections.emptyList();
        when(noticeService.findModerated()).thenReturn(mockNotices);
        when(noticeService.findNotModerated()).thenReturn(mockNotices);
        when(noticeService.findAll()).thenReturn(mockNotices);
        moderatorController.setFilterStatus("all");
        moderatorController.setFilterStatus("verified");
        assertEquals("verified", moderatorController.getFilterStatus());
        assertEquals(mockNotices, moderatorController.getFilteredNotices());
    }

    @Test
    public void testLogging() {
        logger.info("JUnit test: INFO log");
        logger.warn("JUnit test: WARN log");
    }
}
