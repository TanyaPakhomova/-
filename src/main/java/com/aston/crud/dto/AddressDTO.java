package com.aston.crud.dto;

import java.util.Objects;

public class AddressDTO {
    private final int id;
    private final String street;
    private final String city;
    private final String state;
    private final String postalCode;
    private final int userId;

    public AddressDTO(int id, String street, String city, String state, String postalCode, int userId) {
        this.id = id;
        this.street = street;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public int getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "AddressDTO{" +
                "id=" + id +
                ", street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", userId=" + userId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddressDTO that = (AddressDTO) o;
        return id == that.id && userId == that.userId && Objects.equals(street, that.street) && Objects.equals(city, that.city) && Objects.equals(state, that.state) && Objects.equals(postalCode, that.postalCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, street, city, state, postalCode, userId);
    }
}
