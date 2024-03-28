package com.aston.crud.controller;

import com.aston.crud.dao.ProductDAO;
import com.aston.crud.dao.ProductDAOImpl;
import com.aston.crud.dto.ProductDTO;
import com.aston.crud.entities.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ProductController extends AbstractHandler {

    private final ObjectMapper objectMapper;
    private final ProductDAO productDAO;

    public ProductController() throws SQLException {
        this.objectMapper = new ObjectMapper();
        this.productDAO = new ProductDAOImpl();
    }

    public ProductController(ProductDAO productDAO) {
        this.objectMapper = new ObjectMapper();
        this.productDAO = productDAO;
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
                if (s.endsWith("/products")) {
                    handleGetProducts(request, httpServletResponse);
                } else if (s.endsWith("/product")) {
                    handleGetProduct(request, httpServletResponse);
                }
                break;
            case "POST":
                handleAddProduct(httpServletRequest, httpServletResponse);
                break;
            case "PUT":
                handleUpdateProduct(httpServletRequest, httpServletResponse);
                break;
            case "DELETE":
                handleDeleteProduct(httpServletRequest, httpServletResponse);
                break;
            default:
                httpServletResponse.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                httpServletResponse.getWriter().println("Method not allowed");
        }
    }

    private void handleGetProduct(Request request, jakarta.servlet.http.HttpServletResponse httpServletResponse) throws IOException {
        try {
            Product product = productDAO.getProductById(Integer.parseInt(request.getParameter("id")));
            String jsonResponse = objectMapper.writeValueAsString(product);
            httpServletResponse.getWriter().println(jsonResponse);
        } catch (SQLException e) {
            httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            httpServletResponse.getWriter().println("Error retrieving products: " + e.getMessage());
        } catch (JsonProcessingException e) {
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleGetProducts(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response) throws IOException {
        try {
            List<Product> products = productDAO.getAllProducts();
            String jsonResponse = objectMapper.writeValueAsString(products);
            response.getWriter().println(jsonResponse);
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error retrieving products: " + e.getMessage());
        }
    }

    private void handleAddProduct(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response) throws IOException {
        String body = request.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);
        ProductDTO product = objectMapper.readValue(body, ProductDTO.class);

        String name = product.getName();
        double price = product.getPrice();
        int categoryId = product.getCategoryId();

        try {
            Product newProduct = new Product(0,name,price,categoryId);
            productDAO.addProduct(newProduct);
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().println("Product added successfully");
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error adding product: " + e.getMessage());
        }
    }

    private void handleUpdateProduct(HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response) throws IOException {

        String body = getRequestBody(request);
        ProductDTO productDTO = objectMapper.readValue(body, ProductDTO.class);

        String name = productDTO.getName();
        double price = productDTO.getPrice();
        int categoryId = productDTO.getCategoryId();

        int productId = Integer.parseInt(request.getParameter("id"));
        if (name == null || price <= 0 || categoryId <= 0) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Product name, price, and category ID parameters are required");
            return;
        }

        try {
            Product existingProduct = productDAO.getProductById(productId);
            if (existingProduct == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().println("Product with ID " + productId + " not found");
                return;
            }

            existingProduct.setName(name);
            existingProduct.setPrice(price);
            existingProduct.setCategoryId(categoryId);
            productDAO.updateProduct(existingProduct);

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("Product updated successfully");
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error updating product: " + e.getMessage());
        }
    }

    private void handleDeleteProduct(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response) throws IOException {
        int productId = Integer.parseInt(request.getParameter("id"));

        try {
            productDAO.deleteProductById(productId);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("Product deleted successfully");
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error deleting product: " + e.getMessage());
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
