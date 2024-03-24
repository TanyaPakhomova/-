package com.aston.crud.dao;

import com.aston.crud.entities.Address;
import com.aston.crud.entities.Category;
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
    public List<Address> getAllAddresses() {
        return null;
    }

    @Override
    public List<Address> getAddressesByUserId(int userId) {
        return null;
    }

    @Override
    public void addAddress(Address address) throws SQLException {
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
    public void deleteAddressById(int id) {
    }

    private Address extractAddressFromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String street = resultSet.getString("street");
        String city = resultSet.getString("city");
        String state = resultSet.getString("state");
        String postalCode = resultSet.getString("postal_code");
        int userId = resultSet.getInt("userId");

        return new Address(id, street, city, state, postalCode, userId);
    }
}
