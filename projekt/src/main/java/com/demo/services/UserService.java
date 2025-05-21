package com.demo.services;

import com.demo.models.*;

public interface UserService {
    public User findByLogin(String login);
    public boolean verify(String login, String password);
}
