package com.aston.crud.dao;

import com.aston.crud.entities.Product;

import java.sql.*;

import com.aston.crud.entities.User;
import com.aston.crud.util.DBConnection;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOImpl implements ProductDAO {
    private final Connection connection;

    public ProductDAOImpl() throws SQLException {
        connection = DBConnection.getConnection();
    }

    @Override
    public Product getProductById(int id) throws SQLException {
        String query = "SELECT * FROM products WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return extractProductFromResultSet(resultSet);
            }
        }
        return null;
    }

    @Override
    public List<Product> getAllProducts() throws SQLException {
        List<Product> users = new ArrayList<>();
        String query = "SELECT * FROM products";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                users.add(extractProductFromResultSet(resultSet));
            }
        }
        return users;
    }

    @Override
    public void addProduct(Product product) throws SQLException {
        String query = "INSERT INTO products (name, price, category_id) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, product.getName());
            statement.setDouble(2, product.getPrice());
            statement.setInt(3, product.getCategoryId());
            statement.executeUpdate();
        }
    }

    @Override
    public void updateProduct(Product product) throws SQLException {
        String query = "UPDATE products SET name = ?, price = ? WHERE category_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, product.getName());
            statement.setDouble(2, product.getPrice());
            statement.setInt(3, product.getCategoryId());
            statement.executeUpdate();
        }
    }

    @Override
    public void deleteProductById(int id) throws SQLException {
        String query = "DELETE FROM products WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
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
