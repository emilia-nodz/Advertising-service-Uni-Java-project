package com.demo.services;

import com.demo.models.*;
import com.demo.dao.*;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

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
        return u != null ? password.equals(u.getPassword()) : false;
    }
}
