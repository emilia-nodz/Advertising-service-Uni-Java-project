package com.demo.dao;

import java.util.List;
import java.util.Optional;

import com.demo.models.User;
import com.demo.models.UserRole;

import jakarta.ejb.Stateless;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Stateless
public class UserDAOImpl extends AbstractDAOImpl<User> implements UserDAO {

    private static final Logger logger = LogManager.getLogger(UserDAOImpl.class);

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
        return findList("User.findByRole", "role", role);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        logger.debug("Wyszukiwanie użytkowników po nazwie użytkownika: {}", login);
        return findSingle("User.findByLogin","login",login);
    }

    public User getReference(Long id) {
        return em.getReference(User.class, id);
    }
}
