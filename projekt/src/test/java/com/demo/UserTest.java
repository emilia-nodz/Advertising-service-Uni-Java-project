package com.demo;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.demo.dao.UserDAOImpl;
import com.demo.models.User;
import com.demo.models.UserRole;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/*public class UserTest {
    private static EntityManagerFactory emf;
    private EntityManager em;
    private UserDAOImpl userDao;

    @BeforeAll
    public static void setupClass() {
        emf = Persistence.createEntityManagerFactory("myPU");
    }

    @AfterAll
    public static void tearDownClass() {
        if (emf != null) {
            emf.close();
        }
    }

    @BeforeEach
    public void setup() {
        em = emf.createEntityManager();
        em.getTransaction().begin();

        userDao = new UserDAOImpl();
    }

    @AfterEach
    public void tearDown() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
        em.close();
    }
    
    @Test
    public void test_create_user() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setEmail("test@test.com");
        user.setName("Jan");
        user.setSurname("Kowalski");
        user.setRole(UserRole.USER);
        
        userDao.save(user);
        
        User found = userDao.findByUsername("testuser");
        assertNotNull(found);
        assertEquals("test@test.com", found.getEmail());
        assertEquals("Kowalski", found.getSurname());
    }

    @Test
    public void test_findByRole_Admin() {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("admin");
        user.setEmail("admin@admin.com");
        user.setName("Admin");
        user.setSurname("Admin");
        user.setRole(UserRole.ADMIN);

        userDao.save(user);

        List<User> admins = userDao.findByRole(UserRole.ADMIN);
        assertEquals(1, admins.size());
        assertEquals("admin", admins.get(0).getUsername());
    }

    @Test 
    public void test_delete_user() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setEmail("anna@nowak.com");
        user.setName("Anna");
        user.setSurname("Nowak");
        user.setRole(UserRole.USER);
        
        userDao.save(user);

        Long id = user.getId();
        userDao.delete(id);
        Optional<User> deleted = userDao.findById(id);
        assertNull(deleted);
    }
}*/
