package com.aston.crud.dao;

import com.aston.crud.entities.Category;

import java.sql.SQLException;
import java.util.List;

public interface CategoryDAO {
    Category getCategoryById(int id) throws SQLException;
    List<Category> getAllCategories() throws SQLException;
    void addCategory(Category category) throws SQLException;
    void updateCategoryById(Category category);
    void deleteCategoryById(int id) throws SQLException;
}
