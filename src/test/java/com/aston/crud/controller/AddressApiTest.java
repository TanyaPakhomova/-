package com.aston.crud.controller;

import com.aston.crud.dao.AddressDAO;
import com.aston.crud.entities.Address;
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

public class AddressApiTest {

    private AddressDAO addressDAO;
    private Server server;
    private ObjectMapper objectMapper;


    @BeforeEach
    void setUp() throws Exception {
        addressDAO = mock(AddressDAO.class);
        objectMapper = new ObjectMapper();

        server = new Server(8080);
        server.setHandler(new Controller(
                new UserController(),
                new ProductController(),
                new CategoryController(),
                new AddressController(addressDAO)
        ));
        server.start();
    }

    @AfterEach
    void tearDown() throws Exception {
        server.stop();
    }

    @Test
    void testGetAddressById() throws IOException, InterruptedException, URISyntaxException, SQLException {
        when(addressDAO.getAddressById(8)).thenReturn(new Address(8, "123 Main St", "New York", "NY", "10001", 1));

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(HttpRequest.newBuilder()
                                .uri(new URI("http://localhost:8080/address?id=8"))
                                .GET()
                                .build(),
                        HttpResponse.BodyHandlers.ofString()
                );

        assertEquals(200, response.statusCode());
        String responseBody = response.body();
        assertTrue(responseBody.contains("123 Main St"));
        assertTrue(responseBody.contains("New York"));
        assertTrue(responseBody.contains("NY"));
        assertTrue(responseBody.contains("10001"));
    }

    @Test
    void testGetAllAddresses() throws IOException, InterruptedException, URISyntaxException, SQLException {
        List<Address> addresses = Arrays.asList(
                new Address(1,"123 Main St", "New York", "NY", "10001",  1),
                new Address(2, "456 Elm St", "Los Angeles", "CA", "CA",  2)
        );

        when(addressDAO.getAllAddresses()).thenReturn(addresses);

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(HttpRequest.newBuilder()
                                .uri(new URI("http://localhost:8080/addresses"))
                                .GET()
                                .build(),
                        HttpResponse.BodyHandlers.ofString()
                );

        assertEquals(200, response.statusCode());
        String responseBody = response.body();
        assertTrue(responseBody.contains("123 Main St"));
        assertTrue(responseBody.contains("New York"));
        assertTrue(responseBody.contains("NY"));
        assertTrue(responseBody.contains("10001"));
        assertTrue(responseBody.contains("456 Elm St"));
        assertTrue(responseBody.contains("Los Angeles"));
        assertTrue(responseBody.contains("1"));
        assertTrue(responseBody.contains("2"));
    }

    @Test
    void testAddAddresses() throws IOException, InterruptedException, URISyntaxException, SQLException {
        Address address = new Address(1,"123 Main St", "New York", "NY", "10001",  1);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/address"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(address)))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        String responseBody = response.body();
        assertTrue(responseBody.contains("Address added successfully"));

        verify(addressDAO).addAddress(address);
    }

    @Test
    void testDeleteAddress() throws IOException, InterruptedException, URISyntaxException, SQLException {
        int addressIdToDelete = 1;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/user?id=" + addressIdToDelete))
                .DELETE()
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }


}
