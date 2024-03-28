package com.aston.crud.controller;

import com.aston.crud.dao.AddressDAO;
import com.aston.crud.dao.AddressDAOImpl;
import com.aston.crud.dto.AddressDTO;
import com.aston.crud.entities.Address;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AddressController extends AbstractHandler {

    private final ObjectMapper objectMapper;
    private final AddressDAO addressDAO;

    public AddressController() throws SQLException {
        this.objectMapper = new ObjectMapper();
        this.addressDAO = new AddressDAOImpl();
    }

    public AddressController(AddressDAO addressDAO) {
        this.addressDAO = addressDAO;
        this.objectMapper =new ObjectMapper();
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
                if (s.endsWith("/addresses")) {
                    handleGetAddresses(httpServletResponse);
                } else if (s.endsWith("/address")) {
                    handleGetAddress(request, httpServletResponse);
                }
                break;
            case "POST":
                handleAddAddress(httpServletRequest, httpServletResponse);
                break;
            case "DELETE":
                handleDeleteAddress(httpServletRequest, httpServletResponse);
                break;
            default:
                httpServletResponse.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                httpServletResponse.getWriter().println("Method not allowed");
        }
    }

    private void handleGetAddresses(jakarta.servlet.http.HttpServletResponse response) throws IOException {
        try {
            List<Address> addresses = addressDAO.getAllAddresses();
            String jsonResponse = objectMapper.writeValueAsString(addresses);
            response.getWriter().println(jsonResponse);
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error retrieving addresses: " + e.getMessage());
        }
    }

    private void handleGetAddress(Request request, jakarta.servlet.http.HttpServletResponse response) throws IOException {
        try {
            int addressId = Integer.parseInt(request.getParameter("id"));
            Address address = addressDAO.getAddressById(addressId);
            String jsonResponse = objectMapper.writeValueAsString(address);
            response.getWriter().println(jsonResponse);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Invalid address ID");
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error retrieving address: " + e.getMessage());
        }
    }

    private void handleAddAddress(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response) throws IOException {
        String body = request.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);
        AddressDTO addressDTO = objectMapper.readValue(body, AddressDTO.class);

        // Extract data from DTO
        int id = addressDTO.getId();
        String street = addressDTO.getStreet();
        String city = addressDTO.getCity();
        String state = addressDTO.getState();
        String postalCode = addressDTO.getPostalCode();
        int userId = addressDTO.getUserId();

        try {
            Address address = new Address(id, street, city, state, postalCode, userId);
            addressDAO.addAddress(address);
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().println("Address added successfully");
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error adding address: " + e.getMessage());
        }
    }

    private void handleDeleteAddress(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response) throws IOException {
        int addressId = Integer.parseInt(request.getParameter("id"));

        try {
            addressDAO.deleteAddressById(addressId);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("Address deleted successfully");
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error deleting address: " + e.getMessage());
        }
    }
}
