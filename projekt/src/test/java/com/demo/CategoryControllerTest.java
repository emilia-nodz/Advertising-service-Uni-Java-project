package com.demo;
import com.demo.controllers.CategoryController;
import com.demo.models.Category;
import com.demo.services.CategoryService;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static junit.framework.Assert.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class CategoryControllerTest {
    @InjectMocks
    private CategoryController controller;

    @Mock
    private CategoryService categoryService;

    @BeforeEach // Tworzenie potrzebnych mocków
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test // Test sprawdzający poprawność załadowania kategorii
    void test_loading_categories() {
        List<Category> mockCategories = Arrays.asList(new Category(1L, "Dom"), new Category(2L, "Ceramika"));
        when(categoryService.findAll()).thenReturn(mockCategories);
        controller.init();
        assertEquals(2, controller.getCategories().size());
        verify(categoryService).findAll();
    }

    @Test // Test sprawdzający czy nowa kategoria zostanie dodana poprawnie
    void test_add_new_category() {
        Category newCategory = new Category(3L, "Sprzęt sportowy");
        controller.setNewCategory(newCategory);
        when(categoryService.findAll()).thenReturn(Collections.singletonList(newCategory));
        controller.addCategory();
        verify(categoryService).save(newCategory);
        verify(categoryService).findAll();
        assertNotNull(controller.getNewCategory());
        assertNotEquals("Sprzęt sportowy", controller.getNewCategory().getName());
    }

    @Test // Test sprawdzający, czy nazwa kategorii zostanie poprawnie zaktualizowana
    void test_update_category() {
        Category category = new Category(4L, "Akcesoria kuchenne");
        controller.setSelectedCategory(category);
        category.setName("Akcesoria ogrodowe");
        controller.updateCategory();
        verify(categoryService).save(category);
    }

    @Test // Test sprawdzający, czy kategoria zostanie poprawnie usunięta i nie będzie jej na liście
    void test_delete_category_and_refresh_categories() {
        Category category = new Category();
        when(categoryService.findAll()).thenReturn(Collections.emptyList());
        controller.deleteCategory(category);
        verify(categoryService).delete(category);
        verify(categoryService).findAll();
        assertTrue(controller.getCategories().isEmpty());
    }
}
