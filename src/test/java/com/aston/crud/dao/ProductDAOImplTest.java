package com.aston.crud.dao;

import com.aston.crud.entities.Product;
import com.aston.crud.util.DBConnection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductDAOImplTest {
    private ProductDAOImpl productDAO;

    @BeforeEach
    public void setUp() throws SQLException {
        createTables();
        insertDataIntoTables();

        productDAO = new ProductDAOImpl();
    }

    @AfterEach
    public void tearDown() throws SQLException {
        dropTables();
    }

    @Test
    void testGetProductById() throws SQLException {
        Product product1 = new Product(1, "macbook1", 56.0, 1);
        Product product2 = new Product(2, "macbook2", 53.0, 1);
        productDAO.addProduct(product1);
        productDAO.addProduct(product2);

        Product expectedProduct = product1;
        Product actualProduct = productDAO.getProductById(1);

        assertEquals(expectedProduct, actualProduct);
    }

    @Test
    void testGetAllProducts() throws SQLException {
        Product product1 = new Product(1, "macbook1", 56.0, 1);
        Product product2 = new Product(2, "macbook2", 53.0, 1);
        productDAO.addProduct(product1);
        productDAO.addProduct(product2);

        List<Product> expectedProducts = Arrays.asList(product1, product2);
        List<Product> actualProducts = productDAO.getAllProducts();

        assertEquals(expectedProducts, actualProducts);
    }


    @Test
    void testUpdateProductById() throws SQLException {
        Product product1 = new Product(1, "macbook1", 56.0, 1);
        Product product2 = new Product(2, "macbook2", 53.0, 1);
        productDAO.addProduct(product1);
        productDAO.addProduct(product2);
        productDAO.updateProduct(product2);

        List<Product> expectedProducts = Arrays.asList(product1, product2);
        List<Product> actualProducts = productDAO.getAllProducts();

        assertEquals(expectedProducts, actualProducts);
    }

    @Test
    void testDeleteProductById() throws SQLException {
        Product product1 = new Product(1, "macbook1", 56.0, 1);
        Product product2 = new Product(2, "macbook2", 53.0, 1);
        Product product3 = new Product(3, "macbook2", 55.0, 2);
        productDAO.addProduct(product1);
        productDAO.addProduct(product2);
        productDAO.addProduct(product3);

        List<Product> expectedProducts = Arrays.asList(product1,product3);
        productDAO.deleteProductById(2);
        List<Product> actualProducts = productDAO.getAllProducts();

        assertEquals(expectedProducts, actualProducts);
    }

    private void createTables() throws SQLException {
        Connection connection = DBConnection.getConnection();
        try (Statement statement = connection.createStatement()) {
            // Create users table
            statement.executeUpdate("CREATE TABLE users (id SERIAL PRIMARY KEY, username VARCHAR(50) NOT NULL, email VARCHAR(100) NOT NULL)");

            // Create categories table
            statement.executeUpdate("CREATE TABLE categories (id SERIAL PRIMARY KEY, name VARCHAR(100) NOT NULL)");

            // Create products table with foreign key constraint
            statement.executeUpdate("CREATE TABLE products (id SERIAL PRIMARY KEY, name VARCHAR(100) NOT NULL, " +
                    "price DECIMAL(10, 2) NOT NULL, category_id INT NOT NULL, " +
                    "FOREIGN KEY (category_id) REFERENCES categories(id))");

            // Create addresses table with foreign key constraint
            statement.executeUpdate("CREATE TABLE addresses (id SERIAL PRIMARY KEY, " +
                    "street VARCHAR(255) NOT NULL, city VARCHAR(100) NOT NULL, " +
                    "state VARCHAR(100), postal_code VARCHAR(20) NOT NULL, " +
                    "user_id INT NOT NULL, FOREIGN KEY (user_id) REFERENCES users(id))");

            System.out.println("Tables created successfully.");
        }
    }

    private void insertDataIntoTables() throws SQLException {
        Connection connection = DBConnection.getConnection();
        try(Statement statement = connection.createStatement()) {
            statement.executeUpdate("INSERT INTO categories (name) VALUES ('Electronics');\n" +
                    "INSERT INTO categories (name) VALUES ('Clothing');\n" +
                    "INSERT INTO categories (name) VALUES ('Books');");
        }
    }

    private void dropTables() throws SQLException {
        Connection connection = DBConnection.getConnection();
        try (Statement statement = connection.createStatement()) {
            // Drop tables if they exist
            statement.executeUpdate("DROP TABLE IF EXISTS addresses");
            statement.executeUpdate("DROP TABLE IF EXISTS products");
            statement.executeUpdate("DROP TABLE IF EXISTS categories");
            statement.executeUpdate("DROP TABLE IF EXISTS users");

            System.out.println("Tables dropped successfully.");
        }
    }
}