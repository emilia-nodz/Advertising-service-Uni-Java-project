package com.demo.services;

import com.demo.models.Category;

import java.util.List;

public interface CategoryService {
    public List<Category> findAll();
    public void save(Category category);
    public void delete(Category category);
    public Category findById(Long id);
}
