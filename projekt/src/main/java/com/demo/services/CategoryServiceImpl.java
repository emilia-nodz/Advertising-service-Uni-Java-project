package com.demo.services;

import com.demo.dao.CategoryDAO;
import com.demo.models.Category;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import java.util.List;

@Stateless
public class CategoryServiceImpl implements CategoryService {
    @EJB
    private CategoryDAO categoryDao;

    public List<Category> findAll() {
        return categoryDao.findAll();
    }

    public void save(Category category) {
        if (category.getId() == null) {
            categoryDao.save(category);
        } else {
            categoryDao.update(category);
        }
    }

    public void delete(Category category) {
        categoryDao.delete(category.getId());
    }

    public Category findById(Long id) {
        return categoryDao.findById(id).orElse(null);
    }
}
