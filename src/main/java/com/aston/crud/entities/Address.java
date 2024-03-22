package com.aston.crud.entities;

public class Address {

    private final int id;
    private final String street;
    private final String city;
    private final User user;

    public Address(int id, String street, String city, User user){
        this.id = id;
        this.street = street;
        this.city = city;
        this.user = user;
    }

    public int getId(){
        return id;
    }

    public String getStreet(){
        return street;
    }

    public String getCity() {
        return city;
    }

    public User getUser() {
        return user;
    }

    // Constructors, getters, and setters
}

