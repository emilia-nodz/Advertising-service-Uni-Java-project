package com.demo.impl;

import java.util.List;

import com.demo.dao.UserDAO;
import com.demo.models.User;
import com.demo.models.UserRole;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ApplicationScoped
@Transactional
public class UserDAOImpl implements UserDAO {
    
    private static final Logger logger = LogManager.getLogger(UserDAOImpl.class);

    @PersistenceContext(unitName = "myPU")
    private EntityManager em;

    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    @Override
    public void create(User user) {
        logger.info("Utworzenie użytkownika: {}", user.getUsername());
        em.persist(user);
    }

    @Override
    public void update(User user) {
        logger.info("Zaktualizowano użytkownika: {}", user.getUsername());
        em.merge(user);
    }

    @Override
    public void delete(Long id) {
        User user = em.find(User.class, id);
        if (user != null) {
            logger.info("Usunięto użytkownika z ID: {}", id);
            em.remove(user);
        }
        else {
            logger.warn("Nie udało się usunąć użytkownika o podanym ID");
        }
    }

    @Override
    public User findById(Long id) {
        logger.debug("Szukanie użytkownika z ID: {}", id);
        return em.find(User.class, id);
    }

    @Override
    public User findByUsername(String username) {
        logger.debug("Szukanie użytkownika o nazwie: {}", username);
        return em.createQuery(
                "SELECT u FROM User u WHERE u.username = :username", User.class)
                .setParameter("username", username)
                .getSingleResult();
    }

    @Override
    public List<User> findByRole(UserRole role) {
        logger.debug("Szukanie użytkowników z rolą: {}", role);
        String jpql = "SELECT u FROM User u WHERE u.role = :role"; 
        return em.createQuery(jpql, User.class)
                .setParameter("role", role)
                .getResultList();
    }

    @Override
    public List<User> findAll() {
        logger.debug("Pobieranie wszystkich użytkowników");
        return em.createQuery("SELECT u FROM User u", User.class).getResultList();
    }
}
