package com.aston.crud.dao;


import com.aston.crud.entities.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAOImpl implements CategoryDAO {

    @Override
    public Category getCategoryById(int id) {
        // Implement logic to fetch category by ID from the database
        // Similar to UserDAOImpl.getUserById method
        return null;
    }

    @Override
    public List<Category> getAllCategories() {
        // Implement logic to fetch all categories from the database
        // Similar to UserDAOImpl.getAllUsers method
        return null;
    }

    @Override
    public void addCategory(Category category) {
        // Implement logic to add a category to the database
        // Similar to UserDAOImpl.addUser method
    }

    @Override
    public void updateCategory(Category category) {
        // Implement logic to update a category in the database
        // Similar to UserDAOImpl.updateUser method
    }

    @Override
    public void deleteCategory(int id) {
        // Implement logic to delete a category from the database
        // Similar to UserDAOImpl.deleteUser method
    }

    private Category extractCategoryFromResultSet(ResultSet resultSet) throws SQLException {
        // Implement method to extract Category object from ResultSet
        return null;
    }
}