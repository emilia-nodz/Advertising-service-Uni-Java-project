package com.demo;

import com.demo.impl.UserDAOImpl;
import com.demo.models.User;
import com.demo.models.UserRole;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("myPU");
        EntityManager em = emf.createEntityManager();

        UserDAOImpl userDao = new UserDAOImpl();
        userDao.setEntityManager(em);

        em.getTransaction().begin();
        User user = new User();
        user.setUsername("admin4");
        user.setPassword("admin");
        user.setEmail("admin@admin.com");
        user.setName("admin");
        user.setSurname("admin");
        user.setRole(UserRole.ADMIN);

        userDao.create(user);
        em.getTransaction().commit();

        em.close();
        emf.close();
    }
}
