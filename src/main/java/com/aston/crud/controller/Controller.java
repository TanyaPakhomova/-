package com.aston.crud.controller;

import com.aston.crud.dao.UserDAO;
import com.aston.crud.dao.UserDAOImpl;
import com.aston.crud.dto.UserDTO;
import com.aston.crud.entities.User;
import com.aston.crud.util.DBConnection;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class Controller extends AbstractHandler {

    private final UserController userController;
    private final ProductController productController;
    private final CategoryController categoryController;
    private final AddressController addressController;

    public Controller() throws SQLException {
        userController = new UserController();
        productController = new ProductController();
        categoryController = new CategoryController();
        addressController = new AddressController();
    }

    @Override
    public void handle(String s,
                       Request request,
                       HttpServletRequest httpServletRequest,
                       jakarta.servlet.http.HttpServletResponse httpServletResponse) throws IOException {
        if (s.endsWith("/users") || s.endsWith("/user")) {
            userController.handle(s, request, httpServletRequest, httpServletResponse);
        }
        if (s.endsWith("/products") || s.endsWith("/product")) {
            productController.handle(s, request, httpServletRequest, httpServletResponse);
        }
        if (s.endsWith("/categories") || s.endsWith("/category")) {
            categoryController.handle(s, request, httpServletRequest, httpServletResponse);
        }
        if (s.endsWith("/addresses") || s.endsWith("/address")) {
            addressController.handle(s, request, httpServletRequest, httpServletResponse);
        }
        else {
            httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            request.setHandled(true);
        }

    }

    private static void createTables() throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement()) {
            // Create users table
            statement.executeUpdate("CREATE TABLE if not exists users (id SERIAL PRIMARY KEY, username VARCHAR(50) NOT NULL, email VARCHAR(100) NOT NULL)");

            // Create categories table
            statement.executeUpdate("CREATE TABLE if not exists categories (id SERIAL PRIMARY KEY, name VARCHAR(100) NOT NULL)");

            // Create products table with foreign key constraint
            statement.executeUpdate("CREATE TABLE if not exists products (id SERIAL PRIMARY KEY, name VARCHAR(100) NOT NULL, " +
                    "price DECIMAL(10, 2) NOT NULL, category_id INT NOT NULL, " +
                    "FOREIGN KEY (category_id) REFERENCES categories(id))");

            // Create addresses table with foreign key constraint
            statement.executeUpdate("CREATE TABLE if not exists addresses (id SERIAL PRIMARY KEY, " +
                    "street VARCHAR(255) NOT NULL, city VARCHAR(100) NOT NULL, " +
                    "state VARCHAR(100), postal_code VARCHAR(20) NOT NULL, " +
                    "user_id INT NOT NULL, FOREIGN KEY (user_id) REFERENCES users(id))");

            System.out.println("Tables created successfully.");
        }
    }

    public static void main(String[] args) throws Exception {
        createTables();
        Server server = new Server(8080);
        server.setHandler(new Controller());
        server.start();
        server.join();
    }
}
