package com.aston.crud.controller;

import com.aston.crud.dao.ProductDAO;
import com.aston.crud.entities.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.server.Server;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class ProductApiTest {
    private ProductDAO productDAO;
    private Server server;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws Exception {
        productDAO = mock(ProductDAO.class);
        objectMapper = new ObjectMapper();

        server = new Server(8080);
        server.setHandler(new Controller(
                new UserController(),
                new ProductController(productDAO),
                new CategoryController(),
                new AddressController()
        ));
        server.start();
    }

    @AfterEach
    void tearDown() throws Exception {
        server.stop();
    }

    @Test
    void testGetCategoryById() throws IOException, InterruptedException, URISyntaxException, SQLException {
        when(productDAO.getProductById(1)).thenReturn(new Product(1, "MacBook", 599,1));

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(HttpRequest.newBuilder()
                                .uri(new URI("http://localhost:8080/product?id=1"))
                                .GET()
                                .build(),
                        HttpResponse.BodyHandlers.ofString()
                );

        assertEquals(200, response.statusCode());
        String responseBody = response.body();
        assertTrue(responseBody.contains("MacBook"));
        assertTrue(responseBody.contains("599"));
    }

    @Test
    void testGetAllProducts() throws IOException, InterruptedException, URISyntaxException, SQLException {
        List<Product> products = Arrays.asList(
                new Product(1, "MacBook", 599,1),
                new Product(2, "Book", 59,2)
        );

        when(productDAO.getAllProducts()).thenReturn(products);

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(HttpRequest.newBuilder()
                                .uri(new URI("http://localhost:8080/products"))
                                .GET()
                                .build(),
                        HttpResponse.BodyHandlers.ofString()
                );

        assertEquals(200, response.statusCode());
        String responseBody = response.body();
        assertTrue(responseBody.contains("MacBook"));
        assertTrue(responseBody.contains("599"));
        assertTrue(responseBody.contains("Book"));
        assertTrue(responseBody.contains("59"));
    }

    @Test
    void testAddProduct() throws IOException, InterruptedException, URISyntaxException, SQLException {
        Product product = new Product(0,"Book", 59, 2);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/product"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(product)))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        String responseBody = response.body();
        assertTrue(responseBody.contains("Product added successfully"));

        verify(productDAO).addProduct(product);
    }

    @Test
    void testUpdateProduct() throws IOException, InterruptedException, URISyntaxException, SQLException {
        Product updatedProduct = new Product(1,"BookUpdated", 49, 1);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/product?id=1"))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(updatedProduct)))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }
}
