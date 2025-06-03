package com.demo.services;

import com.demo.models.*;

import java.util.List;

public interface UserService {
    public User findById(Long id);
    public User findByLogin(String login);
    public List<User> findAll();
    public boolean verify(String login, String password);
    public void save(User user);
    public void updateUserRole(Long userId, UserRole newRole);
    public User getReferenceById(Long id);
}
