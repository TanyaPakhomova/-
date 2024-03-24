package com.aston.crud.dao;

import com.aston.crud.entities.Category;

import java.util.List;

public interface CategoryDAO {
    Category getCategoryById(int id);
    List<Category> getAllCategories();
    void addCategory(Category category);
    void updateCategory(Category category);
    void deleteCategory(int id);
}
