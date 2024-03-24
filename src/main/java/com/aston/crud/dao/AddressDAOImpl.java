package com.aston.crud.dao;

import com.aston.crud.entities.Address;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AddressDAOImpl implements AddressDAO {

    @Override
    public Address getAddressById(int id) {
        // Implement logic to fetch address by ID from the database
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
        // Implement logic to add an address to the database
    }

    @Override
    public void deleteAddress(int id) {
        // Implement logic to delete an address from the database
    }

    private Address extractAddressFromResultSet(ResultSet resultSet) throws SQLException {
        // Implement method to extract Address object from ResultSet
        return null;
    }
}
