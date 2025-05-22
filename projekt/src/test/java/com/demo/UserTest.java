package com.demo;

import java.util.List;
import java.util.Optional;

import com.demo.controllers.LoginController;
import com.demo.controllers.RegisterController;
import com.demo.services.UserService;
import com.demo.services.UserServiceImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.demo.dao.UserDAOImpl;
import com.demo.models.User;
import com.demo.models.UserRole;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

/*public class UserTest {
    private static EntityManagerFactory emf;
    private EntityManager em;
    private UserService userService;
    private RegisterController registerController;

    @BeforeAll
    public static void init() {
        emf = Persistence.createEntityManagerFactory("myPU");
    }

    @AfterAll
    public static void tearDownAll() {
        if (emf != null) {
            emf.close();
        }
    }

    @BeforeEach
    public void setUp() {
        em = emf.createEntityManager();
        em.getTransaction().begin();

        UserServiceImpl userServiceImpl = new UserServiceImpl();
        userServiceImpl.setEntityManager(em);
        this.userService = userServiceImpl;

        registerController = new RegisterController();
        registerController.userService = userService;
        registerController.setUsername("testuser");
        registerController.setPassword("testpass");
        registerController.setEmail("test@example.com");
        registerController.setName("Jan");
        registerController.setSurname("Kowalski");
    }

    @AfterEach
    public void tearDown() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
        em.close();
    }

    @Test
    public void testRegister_persistsUser() throws Exception {
        registerController.register();
        User found = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                .setParameter("username", "testuser")
                .getSingleResult();

        assertNotNull(found);
        assertEquals("testuser", found.getUsername());
        assertEquals("test@example.com", found.getEmail());
        assertEquals(UserRole.USER, found.getRole());
    }
}*/
