package com.aston.crud.dao;

import com.aston.crud.entities.Address;

import java.sql.SQLException;
import java.util.List;

public interface AddressDAO {
    Address getAddressById(int id) throws SQLException;
    List<Address> getAllAddresses() throws SQLException;
    List<Address> getAddressesByUserId(int userId) throws SQLException;
    void addAddress(Address address) throws SQLException;
    void deleteAddressById(int id) throws SQLException;
}
