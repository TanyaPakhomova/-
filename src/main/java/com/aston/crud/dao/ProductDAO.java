package com.aston.crud.dao;

import com.aston.crud.entities.Product;

import java.sql.SQLException;
import java.util.List;

public interface ProductDAO {
    Product getProductById(int id) throws SQLException;
    List<Product> getAllProducts() throws SQLException;
    void addProduct(Product product) throws SQLException;
    void updateProduct(Product product) throws SQLException;
    void deleteProductById(int id) throws SQLException;
}
