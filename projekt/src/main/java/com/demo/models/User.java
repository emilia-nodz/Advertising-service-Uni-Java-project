package com.demo.models;

import java.io.Serializable;

import jakarta.persistence.*;

@NamedQuery(name = "User.findByLogin",
        query = "SELECT u FROM User u WHERE u.username = :login")
@NamedQuery(name = "User.findByEmail",
        query = "SELECT u FROM User u WHERE u.email = :email")
@NamedQuery(name = "User.findByRole",
        query = "SELECT u FROM User u WHERE u.role = :role")
@Entity
@Table(name = "users")
public class User extends AbstractModel {
    // Pola w tabeli:
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 50)
    private String surname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    // Konstruktor domyślny (pusty, wymagany przez JPA):
    public User() {}

    // Konstruktor z parametrami:
    public User(Long id, String username, String email, String password, String name, String surname, UserRole role) {
        super(id);
        this.username = username;
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.role = role;
    }

    // Gettery i Settery
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }

    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Category other = (Category) obj;
        return getId() != null && getId().equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
