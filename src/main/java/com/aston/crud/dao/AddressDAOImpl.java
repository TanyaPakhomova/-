package com.aston.crud.dao;

import com.aston.crud.entities.Address;
import com.aston.crud.entities.Category;
import com.aston.crud.entities.User;
import com.aston.crud.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AddressDAOImpl implements AddressDAO {
    private final Connection connection;

    public AddressDAOImpl() throws SQLException {
        connection = DBConnection.getConnection();
    }

    @Override
    public Address getAddressById(int id) throws SQLException {
        String query = "SELECT * FROM addresses WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return extractAddressFromResultSet(resultSet);
            }
        }
        return null;
    }

    @Override
    public List<Address> getAllAddresses() throws SQLException {
        List<Address> users = new ArrayList<>();
        String query = "SELECT * FROM addresses";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                users.add(extractAddressFromResultSet(resultSet));
            }
        }
        return users;
    }

    @Override
    public List<Address> getAddressesByUserId(int userId) throws SQLException {
        List<Address> addresses = new ArrayList<>();
        String query = "SELECT * FROM addresses WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    addresses.add(extractAddressFromResultSet(resultSet));
                }
            }
        }
        return addresses;
    }

    @Override
    public void addAddress(Address address) throws SQLException {
        if (address.getUserId() <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        String query = "INSERT INTO addresses (street, city, state, postal_code, user_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, address.getStreet());
            statement.setString(2, address.getCity());
            statement.setString(3, address.getState());
            statement.setString(4, address.getPostalCode());
            statement.setInt(5, address.getUserId());
            statement.executeUpdate();
        }
    }

    @Override
    public void deleteAddressById(int id) throws SQLException {
        if (getAddressById(id) == null) {
            throw new IllegalArgumentException("Address with ID " + id + " does not exist");
        }
        String query = "DELETE FROM addresses WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    private Address extractAddressFromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String street = resultSet.getString("street");
        String city = resultSet.getString("city");
        String state = resultSet.getString("state");
        String postalCode = resultSet.getString("postal_code");
        int userId = resultSet.getInt("user_id");

        return new Address(id, street, city, state, postalCode, userId);
    }
}
