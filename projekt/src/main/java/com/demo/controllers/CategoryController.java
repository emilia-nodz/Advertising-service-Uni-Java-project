package com.demo.controllers;

import com.demo.models.Category;
import com.demo.services.CategoryService;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

@Named("categoryController")
@ViewScoped
public class CategoryController implements Serializable {

    @EJB
    private CategoryService categoryService;

    private List<Category> categories;

    private Category newCategory = new Category();
    private Category selectedCategory;

    public List<Category> getCategories() {
        return categories;
    }

    public Category getNewCategory() {
        return newCategory;
    }

    public void setNewCategory(Category newCategory) {
        this.newCategory = newCategory;
    }

    public Category getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(Category selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    @PostConstruct
    public void init() {
        categories = categoryService.findAll();
    }

    public void addCategory() {
        categoryService.save(newCategory);
        categories = categoryService.findAll();
        newCategory = new Category();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Dodano kategorię: " + newCategory.getName()));
    }

    public void updateCategory() {
        if (selectedCategory != null) {
            categoryService.save(selectedCategory);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Zaktualizowano kategorię: " + selectedCategory.getName()));
        }
    }

    public void deleteCategory(Category category) {
        categoryService.delete(category);
        categories = categoryService.findAll();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Usunięto kategorię"));
    }
}
