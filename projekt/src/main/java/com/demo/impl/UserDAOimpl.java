package com.demo.impl;

import java.util.List;

import com.demo.dao.UserDAO;
import com.demo.models.User;
import com.demo.models.UserRole;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@ApplicationScoped
public class UserDAOImpl implements UserDAO {
    
    @PersistenceContext(unitName = "myPU")
    private EntityManager em;

    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    @Override
    public void create(User user) {
        em.persist(user);
    }

    @Override
    public void update(User user) {
        em.merge(user);
    }

    @Override
    public void delete(Long id) {
        User user = em.find(User.class, id);
        if (user != null) {
            em.remove(user);
        }
    }

    @Override
    public User findById(Long id) {
        return em.find(User.class, id);
    }

    @Override
    public User findByUsername(String username) {
        return em.createQuery(
                "SELECT u FROM User u WHERE u.username = :username", User.class)
                .setParameter("username", username)
                .getSingleResult();
    }

    @Override
    public List<User> findByRole(UserRole role) {
        String jpql = "SELECT u FROM User u WHERE u.role = :role"; 
        return em.createQuery(jpql, User.class)
                .setParameter("role", role)
                .getResultList();
    }

    @Override
    public List<User> findAll() {
        return em.createQuery("SELECT u FROM User u", User.class).getResultList();
    }
}
