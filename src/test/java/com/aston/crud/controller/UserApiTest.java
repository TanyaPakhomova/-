package com.aston.crud.controller;

import com.aston.crud.dao.UserDAO;
import com.aston.crud.entities.User;
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

public class UserApiTest {
    private UserDAO userDAO;
    private Server server;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws Exception {
        userDAO = mock(UserDAO.class);
        objectMapper = new ObjectMapper();

        server = new Server(8080);
        server.setHandler(new Controller(userDAO));
        server.start();
    }

    @AfterEach
    void tearDown() throws Exception {
        server.stop();
    }

    @Test
    void testGetUserById() throws IOException, InterruptedException, URISyntaxException, SQLException {
        when(userDAO.getUserById(8)).thenReturn(new User(8, "tanya", "tt@mail.com"));

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(HttpRequest.newBuilder()
                        .uri(new URI("http://localhost:8080/user?id=8"))
                        .GET()
                        .build(),
                        HttpResponse.BodyHandlers.ofString()
                );

        assertEquals(200, response.statusCode());
        String responseBody = response.body();
        assertTrue(responseBody.contains("tanya"));
        assertTrue(responseBody.contains("tt@mail.com"));
    }

    @Test
    void testGetAllUsers() throws IOException, InterruptedException, URISyntaxException, SQLException {
        List<User> users = Arrays.asList(
                new User(1, "user1", "user1@example.com"),
                new User(2, "user2", "user2@example.com")
        );

        when(userDAO.getAllUsers()).thenReturn(users);

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(HttpRequest.newBuilder()
                                .uri(new URI("http://localhost:8080/users"))
                                .GET()
                                .build(),
                        HttpResponse.BodyHandlers.ofString()
                );

        assertEquals(200, response.statusCode());
        String responseBody = response.body();
        assertTrue(responseBody.contains("user1"));
        assertTrue(responseBody.contains("user1@example.com"));
        assertTrue(responseBody.contains("user2"));
        assertTrue(responseBody.contains("user2@example.com"));
    }

    @Test
    void testAddUser() throws IOException, InterruptedException, URISyntaxException, SQLException {
        User user = new User(0, "newUser", "newUser@example.com");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/user"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(user)))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        String responseBody = response.body();
        assertTrue(responseBody.contains("User added successfully"));

        verify(userDAO).addUser(user);
    }

    @Test
    void testUpdateUser() throws IOException, InterruptedException, URISyntaxException, SQLException {
        User updatedUser = new User(1, "updatedUser", "updatedUser@example.com");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/user?id=1"))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(updatedUser)))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }

    @Test
    void testDeleteUser() throws IOException, InterruptedException, URISyntaxException, SQLException {
        int userIdToDelete = 1;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/user?id=" + userIdToDelete))
                .DELETE()
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }
}
