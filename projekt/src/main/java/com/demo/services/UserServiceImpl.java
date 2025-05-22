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

@Stateless
public class UserServiceImpl implements UserService {

    @EJB
    private UserDAO userDao;

    @Override
    public User findByLogin(String login) {
        return userDao.findByLogin(login).orElse(null);
    }

    @Override
    public boolean verify(String login, String password) {
        User u = userDao.findByLogin(login).orElse(null);
        if (u == null) return false;

        String hashedInputPassword = hashPassword(password);
        return hashedInputPassword.equals(u.getPassword());
    }

    @Override
    public void save(User user) {
        String hashed = hashPassword(user.getPassword());
        user.setPassword(hashed);
        userDao.save(user);
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
