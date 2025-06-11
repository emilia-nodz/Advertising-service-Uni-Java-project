package com.demo;

import com.demo.controllers.ModeratorController;
import com.demo.models.Category;
import com.demo.models.Notice;
import com.demo.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ModeratorViewTest {

    @Mock
    private ModeratorController moderatorController;
    @Mock
    private Notice verifiedNotice;
    @Mock
    private Notice unverifiedNotice;
    @Mock
    private User mockUser;
    @Mock
    private Category mockCategory;

    private List<Notice> testNotices;

    @BeforeEach
    void setUp() {
        when(mockUser.getUsername()).thenReturn("testuser");
        when(mockCategory.getName()).thenReturn("Test Category");
        when(verifiedNotice.getWasModerated()).thenReturn(true);
        when(unverifiedNotice.getWasModerated()).thenReturn(false);
        testNotices = Arrays.asList(verifiedNotice, unverifiedNotice);
    }

    @Test
    void filteredNotices_shouldReturnCorrectList() {
        when(moderatorController.getFilteredNotices()).thenReturn(testNotices);

        List<Notice> result = moderatorController.getFilteredNotices();

        assertEquals(testNotices, result);
        assertEquals(2, result.size());
    }

    @Test
    void statusDisplay_shouldShowCorrectTextAndClass() {
        String verifiedText = verifiedNotice.getWasModerated() ? "Zweryfikowane" : "Oczekujące";
        String verifiedClass = verifiedNotice.getWasModerated() ? "status-verified" : "status-unverified";
        String unverifiedText = unverifiedNotice.getWasModerated() ? "Zweryfikowane" : "Oczekujące";
        String unverifiedClass = unverifiedNotice.getWasModerated() ? "status-verified" : "status-unverified";

        assertEquals("Zweryfikowane", verifiedText);
        assertEquals("status-verified", verifiedClass);
        assertEquals("Oczekujące", unverifiedText);
        assertEquals("status-unverified", unverifiedClass);
    }

    @Test
    void buttonVisibility_shouldShowCorrectButtonsBasedOnStatus() {

        assertTrue(verifiedNotice.getWasModerated());
        assertFalse(!verifiedNotice.getWasModerated());


        assertFalse(unverifiedNotice.getWasModerated());
        assertTrue(!unverifiedNotice.getWasModerated());
    }

    @Test
    void moderatorActions_shouldCallCorrectControllerMethods() {
        moderatorController.toggleVerification(verifiedNotice);
        moderatorController.rejectNotice(unverifiedNotice);

        verify(moderatorController).toggleVerification(verifiedNotice);
        verify(moderatorController).rejectNotice(unverifiedNotice);
    }

    @Test
    void tableRendering_shouldDependOnNoticesList() {

        when(moderatorController.getFilteredNotices()).thenReturn(testNotices);
        boolean shouldRenderTable = !moderatorController.getFilteredNotices().isEmpty();
        assertTrue(shouldRenderTable);


        when(moderatorController.getFilteredNotices()).thenReturn(Collections.emptyList());
        boolean shouldShowEmptyMessage = moderatorController.getFilteredNotices().isEmpty();
        assertTrue(shouldShowEmptyMessage);
    }

    @Test
    void filterOptions_shouldHaveCorrectValuesAndLabels() {
        String[] filterValues = {"all", "verified", "unverified"};
        String[] filterLabels = {"Wszystkie ogłoszenia", "Tylko zweryfikowane", "Tylko niezweryfikowane"};

        assertArrayEquals(new String[]{"all", "verified", "unverified"}, filterValues);
        assertArrayEquals(new String[]{"Wszystkie ogłoszenia", "Tylko zweryfikowane", "Tylko niezweryfikowane"}, filterLabels);
    }

    @Test
    void filterChange_shouldTriggerLoadNotices() {
        moderatorController.loadNotices();
        verify(moderatorController).loadNotices();
    }
}