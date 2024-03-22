package com.aston.crud.entities;

import java.util.List;

public class User {

    private final int id;
    private final String username;
    private final String email;
    private final List<Product> products;
    private final List<Address> addresses;

    public User(int id, String username,
                String email,
                List<Product> products,
                List<Address> addresses) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.products = products;
        this.addresses = addresses;
    }

    // Constructors, getters, and setters
}
