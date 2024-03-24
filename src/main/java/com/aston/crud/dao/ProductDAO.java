package com.aston.crud.dao;

import com.aston.crud.entities.Product;
import java.util.List;

public interface ProductDAO {
    Product getProductById(int id);
    List<Product> getAllProducts();
    void addProduct(Product product);
    void updateProductById(Product product);
    void deleteProductById(int id);
}
