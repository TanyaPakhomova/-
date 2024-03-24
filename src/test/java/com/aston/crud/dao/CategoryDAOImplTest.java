package com.aston.crud.dao;

import com.aston.crud.entities.Category;
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

class CategoryDAOImplTest {
    private CategoryDAOImpl categoryDAO;

    @BeforeEach
    public void setUp() throws SQLException {
        createTables();
        categoryDAO = new CategoryDAOImpl();
    }

    @AfterEach
    public void tearDown() throws SQLException {
        dropTables();
    }

    @Test
    void testGetCategoryById() throws SQLException {
        Category category1 = new Category(1, "Electronics");
        Category category2 = new Category(2, "Clothing");
        Category category3 = new Category(3, "Books");

        categoryDAO.addCategory(category1);
        categoryDAO.addCategory(category2);
        categoryDAO.addCategory(category3);

        Category expectedCategory = category2;
        Category actualCategory = categoryDAO.getCategoryById(2);

        assertEquals(expectedCategory, actualCategory);
    }

    @Test
    void testGetAllCategories() throws SQLException {
        Category category1 = new Category(1, "Electronics");
        Category category2 = new Category(2, "Clothing");
        Category category3 = new Category(3, "Books");

        categoryDAO.addCategory(category1);
        categoryDAO.addCategory(category2);
        categoryDAO.addCategory(category3);;

        List<Category> expectedCategories = Arrays.asList(category1, category2,category3);
        List<Category> actualCategories = categoryDAO.getAllCategories();

        assertEquals(expectedCategories, actualCategories);
    }


    @Test
    void updateCategoryById() {
    }

    @Test
    void deleteCategoryById() throws SQLException {
        Category category1 = new Category(1, "Electronics");
        Category category2 = new Category(2, "Clothing");
        Category category3 = new Category(3, "Books");

        categoryDAO.addCategory(category1);
        categoryDAO.addCategory(category2);
        categoryDAO.addCategory(category3);;

        List<Category> expectedCategory = Arrays.asList(category1,category2);
        categoryDAO.deleteCategoryById(3);
        List<Category> actualCategories = categoryDAO.getAllCategories();

        assertEquals(expectedCategory, actualCategories);
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