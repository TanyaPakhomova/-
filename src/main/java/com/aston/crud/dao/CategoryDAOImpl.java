package com.aston.crud.dao;

import com.aston.crud.entities.Category;
import com.aston.crud.entities.Product;
import com.aston.crud.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAOImpl implements CategoryDAO {
    private final Connection connection;

    public CategoryDAOImpl() throws SQLException {
        connection = DBConnection.getConnection();
    }

    @Override
    public Category getCategoryById(int id) throws SQLException {
        String query = "SELECT * FROM categories WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return extractCategoryFromResultSet(resultSet);
            }
        }
        return null;
    }

    @Override
    public List<Category> getAllCategories() throws SQLException {
        List<Category> categories = new ArrayList<>();
        String query = "SELECT * FROM categories";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                categories.add(extractCategoryFromResultSet(resultSet));
            }
        }
        return categories;
    }

    @Override
    public void addCategory(Category category) throws SQLException {
        String query = "INSERT INTO categories (name) VALUES (?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, category.getName());
            statement.executeUpdate();
        }
    }

    @Override
    public void updateCategoryById(Category category) throws SQLException {
        String query = "UPDATE categories SET name = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, category.getName());
            statement.setInt(2, category.getId());
            statement.executeUpdate();
        }
    }

    @Override
    public void deleteCategoryById(int id) throws SQLException {
        String query = "DELETE FROM categories WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    private Category extractCategoryFromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String categoryName = resultSet.getString("name");
        return new Category(id, categoryName);
    }
}