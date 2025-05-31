package com.demo.services;

import com.demo.models.*;
import com.demo.dao.*;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Stateless
public class UserServiceImpl implements UserService {

    @EJB
    private UserDAO userDao;

    @Override
    public User findByLogin(String login) {
        return userDao.findByLogin(login).orElse(null);
    }

    @Override
    public List<User> findAll() { return userDao.findAll(); }

    @Override
    public boolean verify(String login, String password) {
        User u = userDao.findByLogin(login).orElse(null);
        if (u == null) return false;

        String hashedInputPassword = hashPassword(password);
        return hashedInputPassword.equals(u.getPassword());
    }

    @Override
    public void save(User user) {
        if (user.getId() == null) {
            String hashed = hashPassword(user.getPassword());
            user.setPassword(hashed);
            userDao.save(user);
        }
    }

    @Override
    public void updateUserRole(Long userId, UserRole newRole) {
        Optional<User> optional = userDao.findById(userId);
        if (optional.isPresent()) {
            User user = optional.get();
            user.setRole(newRole);
            userDao.update(user);
        } else {
            throw new IllegalArgumentException("Nie znaleziono użytkownika o id: " + userId);
        }
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(encodedHash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not supported", e);
        }
    }
}
