package com.demo;

import com.demo.bean.UserBean;
import com.demo.controllers.AdminController;
import com.demo.models.Category;
import com.demo.models.User;
import com.demo.models.UserRole;
import com.demo.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

public class AdminControllerTest {
    @InjectMocks
    private AdminController adminController;

    @Mock
    private UserService userService;

    @Mock
    private UserBean userBean;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test // Test sprawdzający, czy użytkownik jest adminem
    void test_is_admin_user_true() {
        User admin = new User();
        admin.setRole(UserRole.ADMIN);
        when(userBean.getUser()).thenReturn(admin);
        when(userBean.isAdmin()).thenReturn(true);
        assertTrue(adminController.isUserAdmin());
    }

    @Test // Test, gdy użytkownik jest null
    void test_is_admin_user_false() {
        when(userBean.getUser()).thenReturn(null);
        assertFalse(adminController.isUserAdmin());
    }

    @Test // Test sprawdzający, czy zmiana roli powiodła się
    void test_change_role_successful() {
        UserRole newRole = UserRole.ADMIN;
        boolean result = adminController.changeUserRole(1L, newRole);
        assertTrue(result);
        verify(userService).updateUserRole(1L, newRole);
    }

    @Test // Test sprawdzający, czy użytkownik zostanie poprawnie usunięty i nie będzie go na liście
    void test_delete_user_and_refresh_users() {
        User user = new User();
        when(userService.findAll()).thenReturn(Collections.emptyList());
        adminController.deleteU(user);
        verify(userService).deleteUser(user);
        verify(userService).findAll();
        assertTrue(adminController.getAllUsers().isEmpty());
    }
}
