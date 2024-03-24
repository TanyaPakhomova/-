package com.aston.crud.dao;

import com.aston.crud.entities.Product;

import java.sql.*;
import com.aston.crud.util.DBConnection;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOImpl implements ProductDAO {

    @Override
    public Product getProductById(int id) {
        // Implement logic to fetch product by ID from the database
        // Similar to UserDAOImpl.getUserById method
        return null;
    }

    @Override
    public List<Product> getAllProducts() {
        // Implement logic to fetch all products from the database
        // Similar to UserDAOImpl.getAllUsers method
        return null;
    }

    @Override
    public void addProduct(Product product) {
        // Implement logic to add a product to the database
        // Similar to UserDAOImpl.addUser method
    }

    @Override
    public void updateProduct(Product product) {
        // Implement logic to update a product in the database
        // Similar to UserDAOImpl.updateUser method
    }

    @Override
    public void deleteProduct(int id) {
        // Implement logic to delete a product from the database
        // Similar to UserDAOImpl.deleteUser method
    }

    private Product extractProductFromResultSet(ResultSet resultSet) throws SQLException {
        // Implement method to extract Product object from ResultSet
        return null;
    }
}
