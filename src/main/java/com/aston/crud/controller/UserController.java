package com.aston.crud.controller;

import com.aston.crud.dao.UserDAO;
import com.aston.crud.dao.UserDAOImpl;
import com.aston.crud.dto.UserDTO;
import com.aston.crud.entities.User;
import com.aston.crud.util.DBConnection;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class UserController extends AbstractHandler {

    private final ObjectMapper objectMapper;
    private final UserDAO userDAO;

    public UserController() throws SQLException {
        this.objectMapper = new ObjectMapper();
        this.userDAO = new UserDAOImpl();
    }

    @Override
    public void handle(String s,
                       Request request,
                       HttpServletRequest httpServletRequest,
                       jakarta.servlet.http.HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.setContentType("application/json;charset=utf-8");
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        request.setHandled(true);

        String method = request.getMethod();

        switch (method) {
            case "GET":
                if (s.endsWith("/users")) {
                    handleGetUsers(request, httpServletResponse);
                } else if (s.endsWith("/user")) {
                    handleGetUser(request, httpServletResponse);
                }
                break;
            case "POST":
                handleAddUser(request, httpServletResponse);
                break;
            case "PUT":
                handleUpdateUser(httpServletRequest, httpServletResponse);
                break;
            case "DELETE":
                handleDeleteUser(httpServletRequest, httpServletResponse);
                break;
            default:
                httpServletResponse.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                httpServletResponse.getWriter().println("Method not allowed");
        }
    }

    private void handleGetUser(Request request, jakarta.servlet.http.HttpServletResponse httpServletResponse) throws IOException {
        try {
            User user = userDAO.getUserById(Integer.parseInt(request.getParameter("id")));
            String jsonResponse = objectMapper.writeValueAsString(user);
            httpServletResponse.getWriter().println(jsonResponse);
        } catch (SQLException e) {
            httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            httpServletResponse.getWriter().println("Error retrieving users: " + e.getMessage());
        } catch (JsonProcessingException e) {
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleGetUsers(HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response) throws IOException {
        try {
            List<User> users = userDAO.getAllUsers();
            String jsonResponse = objectMapper.writeValueAsString(users);
            response.getWriter().println(jsonResponse);
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error retrieving users: " + e.getMessage());
        }
    }

    private void handleAddUser(HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response) throws IOException {
        String body = getRequestBody(request);
        UserDTO userDTO = objectMapper.readValue(body, UserDTO.class);

        String username = userDTO.getUsername();
        String email = userDTO.getEmail();

        if (username == null || email == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Username and email parameters are required");
            return;
        }

        try {
            User newUser = new User(0, username, email);
            userDAO.addUser(newUser);
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().println("User added successfully");
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error adding user: " + e.getMessage());
        }
    }

    private void handleUpdateUser(HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response) throws IOException {
        int userId = Integer.parseInt(request.getParameter("id"));
        String username = request.getParameter("username");
        String email = request.getParameter("email");

        if (username == null || email == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Username and email parameters are required");
            return;
        }

        try {
            User existingUser = userDAO.getUserById(userId);
            if (existingUser == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().println("User with ID " + userId + " not found");
                return;
            }

            existingUser.setUsername(username);
            existingUser.setEmail(email);
            userDAO.updateUser(existingUser);

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("User updated successfully");
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error updating user: " + e.getMessage());
        }
    }

    private void handleDeleteUser(HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response) throws IOException {
        int userId = Integer.parseInt(request.getParameter("id"));

        try {
            User existingUser = userDAO.getUserById(userId);
            if (existingUser == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().println("User with ID " + userId + " not found");
                return;
            }

            userDAO.deleteUserById(userId);

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("User deleted successfully");
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error deleting user: " + e.getMessage());
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

    private String getRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder requestBody = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            requestBody.append(line);
        }
        reader.close();
        return requestBody.toString();
    }
}
