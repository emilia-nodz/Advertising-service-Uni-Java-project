package com.demo.converters;

import com.demo.models.Category;
import com.demo.services.CategoryService;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;

@FacesConverter(value = "categoryConverter")
public class CategoryConverter implements Converter<Category> {

    private CategoryService categoryService;

    public CategoryConverter() {
        FacesContext context = FacesContext.getCurrentInstance();
        categoryService = CDI.current().select(CategoryService.class).get();
    }

    @Override
    public Category getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            Long id = Long.valueOf(value);
            return categoryService.findById(id);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Category category) {
        if (category == null) {
            return "";
        }
        return category.getId() != null ? category.getId().toString() : "";
    }
}