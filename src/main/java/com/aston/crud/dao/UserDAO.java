package com.aston.crud.dao;

import com.aston.crud.entities.User;

import java.util.List;

public interface UserDAO {
    User getUserById(int id);
    List<User> getAllUsers();
    void addUser(User user);
    void updateUserById(User user);
    void deleteUserById(int id);
}
