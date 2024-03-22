package com.aston.crud.entities;

import java.util.List;

public class Category {

    private final int id;
    private final String name;
    private final List<Product> products;

    public Category(int id, String name, List<Product> products) {
        this.id = id;
        this.name = name;
        this.products = products;
    }

    // Constructors, getters, and setters
}
