package com.aston.crud.dao;

import com.aston.crud.entities.Product;

import java.sql.*;

import com.aston.crud.entities.User;
import com.aston.crud.util.DBConnection;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOImpl implements ProductDAO {

    @Override
    public Product getProductById(int id) {
        // Implement logic to fetch user by ID from the database
        Connection connection = DBConnection.getConnection();
        String query = "SELECT * FROM products WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return extractProductFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Product> getAllProducts() {
        Connection connection = DBConnection.getConnection();
        List<Product> users = new ArrayList<>();
        String query = "SELECT * FROM products";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                users.add(extractProductFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public void addProduct(Product product) {
        Connection connection = DBConnection.getConnection();
        String query = "INSERT INTO products (name, price, category_id) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, product.getName());
            statement.setDouble(2, product.getPrice());
            statement.setInt(3, product.getCategoryId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateProductById(Product product) {
        Connection connection = DBConnection.getConnection();
        String query = "UPDATE products SET username = ?, email = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, product.getName());
            statement.setDouble(2, product.getPrice());
            statement.setInt(3, product.getCategoryId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteProductById(int id) {
        Connection connection = DBConnection.getConnection();
        String query = "DELETE FROM products WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private Product extractProductFromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String productName = resultSet.getString("name");
        double price = resultSet.getDouble("price");
        int categoryId = resultSet.getInt("category_id");
        return new Product(id, productName, price, categoryId);
    }
}
