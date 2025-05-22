package com.demo.models;
import jakarta.persistence.*;

@Entity
@Table(name = "CATEGORY")
public class Category {
    // Pola w tabeli:
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    // Konstruktor domyślny:
    public Category() {}

    public Category(String name) {
        this.name = name;
    }

    // Gettery i settery
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Category{id=" + id + ", name='" + name + "'}";
    }


}
