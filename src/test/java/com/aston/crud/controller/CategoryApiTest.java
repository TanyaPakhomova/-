package com.aston.crud.controller;

import com.aston.crud.dao.CategoryDAO;
import com.aston.crud.entities.Category;
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

public class CategoryApiTest {
    private CategoryDAO categoryDAO;
    private Server server;
    private ObjectMapper objectMapper;


    @BeforeEach
    void setUp() throws Exception {
        categoryDAO = mock(CategoryDAO.class);
        objectMapper = new ObjectMapper();

        server = new Server(8080);
        server.setHandler(new Controller(categoryDAO));
        server.start();
    }

    @AfterEach
    void tearDown() throws Exception {
        server.stop();
    }

    @Test
    void testGetCategoryById() throws IOException, InterruptedException, URISyntaxException, SQLException {
        when(categoryDAO.getCategoryById(1)).thenReturn(new Category(1, "Electronic"));

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(HttpRequest.newBuilder()
                                .uri(new URI("http://localhost:8080/category?id=1"))
                                .GET()
                                .build(),
                        HttpResponse.BodyHandlers.ofString()
                );

        assertEquals(200, response.statusCode());
        String responseBody = response.body();
        assertTrue(responseBody.contains("Electronic"));
        assertTrue(responseBody.contains("1"));
    }

    @Test
    void testGetAllCategory() throws IOException, InterruptedException, URISyntaxException, SQLException {
        List<Category> categories = Arrays.asList(
                new Category(1, "Electronic"),
                new Category(2, "Book")
        );

        when(categoryDAO.getAllCategories()).thenReturn(categories);

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(HttpRequest.newBuilder()
                                .uri(new URI("http://localhost:8080/categories"))
                                .GET()
                                .build(),
                        HttpResponse.BodyHandlers.ofString()
                );

        assertEquals(200, response.statusCode());
        String responseBody = response.body();
        assertTrue(responseBody.contains("Electronic"));
        assertTrue(responseBody.contains("Book"));
    }

    @Test
    void testAddCategory() throws IOException, InterruptedException, URISyntaxException, SQLException {
        Category category = new Category(0, "Electronic");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/category"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(category)))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        String responseBody = response.body();
        assertTrue(responseBody.contains("Category added successfully"));

        verify(categoryDAO).addCategory(category);
    }

    @Test
    void testUpdateCategory() throws IOException, InterruptedException, URISyntaxException, SQLException {
        Category updatedcCategory = new Category(0, "Devises");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/category?id=0"))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(updatedcCategory)))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        verify(categoryDAO).updateCategoryById(updatedcCategory);
    }

    @Test
    void testDeleteCategory() throws IOException, InterruptedException, URISyntaxException, SQLException {
        int categoryIdToDelete = 1;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/category?id=" + categoryIdToDelete))
                .DELETE()
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

}
