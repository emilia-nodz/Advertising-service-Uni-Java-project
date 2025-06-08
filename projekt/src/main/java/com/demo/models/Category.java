package com.demo.models;
import jakarta.persistence.*;

@Entity
@Table(name = "CATEGORY")
public class Category extends AbstractModel {

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
