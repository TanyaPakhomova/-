package com.aston.crud.dao;

import com.aston.crud.entities.User;

import java.sql.SQLException;
import java.util.List;

public interface UserDAO {
    User getUserById(int id) throws SQLException;
    List<User> getAllUsers() throws SQLException;
    void addUser(User user) throws SQLException;
    void updateUser(User user) throws SQLException;
    void deleteUserById(int id) throws SQLException;
}
