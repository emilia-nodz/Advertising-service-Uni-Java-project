package com.demo.converters;

import com.demo.models.Category;
import com.demo.services.CategoryService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;
import jakarta.inject.Named;
@Named("categoryConverter")
@RequestScoped
public class CategoryConverter implements Converter<Category> {

    @Inject
    private CategoryService categoryService;

    @Override
    public Category getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) return null;
        try {
            Long id = Long.valueOf(value);
            return categoryService.findById(id);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Category category) {
        if (category == null || category.getId() == null) return "";
        return category.getId().toString();
    }
}

