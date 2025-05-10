package com.demo.services;

import java.util.List;

import com.demo.impl.UserDAOimpl;
import com.demo.models.User;
import com.demo.models.UserRole;

import jakarta.inject.Inject;

public class UserService {
    @Inject
    private UserDAOimpl userDao;

    public void addUsers() {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("admin");
        user.setEmail("admin@admin.com");
        user.setName("admin");
        user.setSurname("admin");
        user.setRole(UserRole.ADMIN);

        userDao.create(user);
    }

    // Tymczasowa metoda do wypisania wszystkich adminów:
    public void printAdmins() {
        List<User> admins = userDao.findByRole(UserRole.ADMIN);
        admins.forEach(user -> System.out.println(user.getUsername()));
    }
}
