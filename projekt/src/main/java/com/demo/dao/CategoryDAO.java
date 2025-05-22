package com.demo.dao;

import com.demo.models.Category;

public interface CategoryDAO extends AbstractDAO<Category> {
    Category findByName(String name);
}
