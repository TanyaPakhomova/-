package com.aston.crud.dao;

import com.aston.crud.entities.Address;
import com.aston.crud.entities.Category;
import com.aston.crud.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AddressDAOImpl implements AddressDAO {

    @Override
    public Address getAddressById(int id) {
        // Implement logic to fetch user by ID from the database
        Connection connection = DBConnection.getConnection();
        String query = "SELECT * FROM addresses WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return extractAddressFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Address> getAllAddresses() {
        // Implement logic to fetch all addresses from the database
        return null;
    }

    @Override
    public List<Address> getAddressesByUserId(int userId) {
        // Implement logic to fetch addresses by user ID from the database
        return null;
    }

    @Override
    public void addAddress(Address address) {
        Connection connection = DBConnection.getConnection();
        String query = "INSERT INTO categories (street, city, state, postal_code, user_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, address.getStreet());
            statement.setString(2, address.getCity());
            statement.setString(3, address.getState());
            statement.setString(4, address.getPostalCode());
            statement.setInt(5, address.getUserId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteAddressById(int id) {
        // Implement logic to delete an address from the database
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
