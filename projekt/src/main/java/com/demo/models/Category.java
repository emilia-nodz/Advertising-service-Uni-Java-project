package com.demo.models;
import jakarta.persistence.*;

@Entity
@Table(name = "CATEGORY")
public class Category extends AbstractModel{
      @Column(nullable = false, length = 100)
    private String name;

    // Konstruktor domyślny:
    public Category() {}

    public Category(Long id, String name) {
        super(id);
        this.name = name;
    }

    // Gettery i settery
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Category name = " + name;
    }


}
