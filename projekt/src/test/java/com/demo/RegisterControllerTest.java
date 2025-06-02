package com.demo;

import com.demo.bean.UserBean;
import com.demo.controllers.RegisterController;
import com.demo.models.User;
import com.demo.models.UserRole;
import com.demo.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.verify;

public class RegisterControllerTest {
    @InjectMocks
    private RegisterController registerController;

    @Mock
    private UserService userService;

    @Mock
    private UserBean userBean;

    @BeforeEach // Tworzenie potrzebnych mocków
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test // Test sprawdzający poprawną rejestrację
    void test_successful_registration() {
        String username = "newuser";
        String password = "newpass";
        String email = "new@user.com";
        String name = "Jan";
        String surname = "Kowalski";

        boolean result = registerController.registerUser(username, password, email, name, surname);
        assertTrue(result);
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userService).save(captor.capture());

        User savedUser = captor.getValue();
        assertEquals(username, savedUser.getUsername());
        assertEquals(password, savedUser.getPassword());
        assertEquals(email, savedUser.getEmail());
        assertEquals(name, savedUser.getName());
        assertEquals(surname, savedUser.getSurname());
        assertEquals(UserRole.USER, savedUser.getRole());
    }
}
