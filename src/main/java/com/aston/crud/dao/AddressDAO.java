package com.aston.crud.dao;

import com.aston.crud.entities.Address;

import java.util.List;

public interface AddressDAO {
    Address getAddressById(int id);
    List<Address> getAllAddresses();
    List<Address> getAddressesByUserId(int userId);
    void addAddress(Address address);
    void deleteAddressById(int id);
}
