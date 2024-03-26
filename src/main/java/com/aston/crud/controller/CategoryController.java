package com.aston.crud.controller;

import com.aston.crud.dao.CategoryDAO;
import com.aston.crud.dao.CategoryDAOImpl;
import com.aston.crud.dto.CategoryDTO;
import com.aston.crud.entities.Category;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class CategoryController extends AbstractHandler {

    private final ObjectMapper objectMapper;
    private final CategoryDAO categoryDAO;

    public CategoryController() throws SQLException {
        this.objectMapper = new ObjectMapper();
        this.categoryDAO = new CategoryDAOImpl();
    }

    @Override
    public void handle(String s,
                       Request request,
                       jakarta.servlet.http.HttpServletRequest httpServletRequest,
                       jakarta.servlet.http.HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.setContentType("application/json;charset=utf-8");
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        request.setHandled(true);

        String method = request.getMethod();

        switch (method) {
            case "GET":
                if (s.endsWith("/categories")) {
                    handleGetCategories(httpServletResponse);
                } else if (s.endsWith("/category")) {
                    handleGetCategory(request, httpServletResponse);
                }
                break;
            case "POST":
                handleAddCategory(httpServletRequest, httpServletResponse);
                break;
            case "PUT":
                handleUpdateCategory(httpServletRequest, httpServletResponse);
                break;
            case "DELETE":
                handleDeleteCategory(httpServletRequest, httpServletResponse);
                break;
            default:
                httpServletResponse.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                httpServletResponse.getWriter().println("Method not allowed");
        }
    }

    private void handleGetCategories(jakarta.servlet.http.HttpServletResponse response) throws IOException {
        try {
            List<Category> categories = categoryDAO.getAllCategories();
            String jsonResponse = objectMapper.writeValueAsString(categories);
            response.getWriter().println(jsonResponse);
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error retrieving categories: " + e.getMessage());
        }
    }

    private void handleGetCategory(Request request, jakarta.servlet.http.HttpServletResponse response) throws IOException {
        try {
            int categoryId = Integer.parseInt(request.getParameter("id"));
            Category category = categoryDAO.getCategoryById(categoryId);
            String jsonResponse = objectMapper.writeValueAsString(category);
            response.getWriter().println(jsonResponse);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Invalid category ID");
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error retrieving category: " + e.getMessage());
        }
    }

    private void handleAddCategory(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response) throws IOException {
        String body = request.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);
        CategoryDTO category = objectMapper.readValue(body, CategoryDTO.class);

        String name = category.getName();

        try {
            Category newCategory = new Category(0,name);
            categoryDAO.addCategory(newCategory);
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().println("Category added successfully");
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error adding category: " + e.getMessage());
        }
    }

    private void handleUpdateCategory(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response) throws IOException {
        int categoryId = Integer.parseInt(request.getParameter("id"));
        String body = request.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);
        CategoryDTO category = objectMapper.readValue(body, CategoryDTO.class);
        category.setId(categoryId);

        String name = category.getName();

        try {
            Category newCategory = new Category(0,name);
            categoryDAO.updateCategoryById(newCategory);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("Category updated successfully");
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error updating category: " + e.getMessage());
        }
    }

    private void handleDeleteCategory(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response) throws IOException {
        int categoryId = Integer.parseInt(request.getParameter("id"));

        try {
            categoryDAO.deleteCategoryById(categoryId);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("Category deleted successfully");
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error deleting category: " + e.getMessage());
        }
    }
}
