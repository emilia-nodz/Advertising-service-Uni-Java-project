package com.demo.dao;

import java.util.List;

import com.demo.models.User;
import com.demo.models.UserRole;

public interface UserDAO {
    void create(User user);
    void update(User user);
    void delete(Long id);
    User findById(Long id);
    User findByUsername(String username);
    List<User> findByRole(UserRole role);
    List<User> findAll();
}
