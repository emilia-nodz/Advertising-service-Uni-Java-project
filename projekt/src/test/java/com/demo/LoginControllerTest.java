package com.demo;

import com.demo.bean.UserBean;
import com.demo.controllers.LoginController;
import com.demo.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoginControllerTest {
    @InjectMocks
    private LoginController loginController;

    @Mock
    private UserService userService;

    @Mock
    private UserBean userBean;

    @BeforeEach // Tworzenie potrzebnych mocków
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test // Test sprawdzający logowanie z poprawnymi danymi
    void test_login_valid_user() {
        when(userService.verify("user", "pass")).thenReturn(true);
        boolean result = loginController.authenticate("user", "pass");
        assertTrue(result);
    }

    @Test // Test sprawdzający logowanie z niepoprawnymi danymi
    void test_login_invalid_user() {
        when(userService.verify("user", "wrong")).thenReturn(false);
        boolean result = loginController.authenticate("user", "wrong");
        assertFalse(result);
    }

    @Test // Test sprawdzający wylogowywanie użytkownika
    void test_logout() {
        loginController.logoutUser();
        verify(userBean).setUser(null);
    }
}
