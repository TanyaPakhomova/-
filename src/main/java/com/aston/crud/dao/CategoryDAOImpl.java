package com.aston.crud.dao;

import com.aston.crud.entities.Category;
import com.aston.crud.entities.Product;
import com.aston.crud.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAOImpl implements CategoryDAO {

    @Override
    public Category getCategoryById(int id) {
        // Implement logic to fetch user by ID from the database
        Connection connection = DBConnection.getConnection();
        String query = "SELECT * FROM categories WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return extractCategoryFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Category> getAllCategories() {
        Connection connection = DBConnection.getConnection();
        List<Category> categories = new ArrayList<>();
        String query = "SELECT * FROM categories";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                categories.add(extractCategoryFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    @Override
    public void addCategory(Category category) {
        Connection connection = DBConnection.getConnection();
        String query = "INSERT INTO categories (name) VALUES (?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, category.getName());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateCategoryById(Category category) {
        // Implement logic to update a category in the database
        // Similar to UserDAOImpl.updateUser method
    }

    @Override
    public void deleteCategoryById(int id) {
        Connection connection = DBConnection.getConnection();
        String query = "DELETE FROM categories WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private Category extractCategoryFromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String categoryName = resultSet.getString("name");
        return new Category(id, categoryName);
    }
}