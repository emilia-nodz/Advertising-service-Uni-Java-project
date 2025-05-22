package com.demo.dao;

import java.util.List;
import java.util.Optional;

import com.demo.models.User;
import com.demo.models.UserRole;

public interface UserDAO extends AbstractDAO<User> {
    User findByUsername(String username);
    List<User> findByRole(UserRole role);
    public Optional<User> findByLogin(String username);
}
