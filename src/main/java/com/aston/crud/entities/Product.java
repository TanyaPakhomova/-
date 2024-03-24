package com.aston.crud.entities;

public class Product {

    private int id;
    private String name;
    private double price;
    private int categoryId;

    public Product(String name, double price, int categoryId){
        this.name = name;
        this.price = price;
        this.categoryId =categoryId;
    }


    // Constructors, getters, and setters
}
