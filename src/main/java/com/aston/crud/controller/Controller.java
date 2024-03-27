package com.aston.crud.controller;

import com.aston.crud.dao.*;
import com.aston.crud.dto.UserDTO;
import com.aston.crud.entities.User;
import com.aston.crud.util.DBConnection;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class Controller extends AbstractHandler {

    private final UserController userController;
    private final ProductController productController;
    private final CategoryController categoryController;
    private final AddressController addressController;

    public Controller() throws SQLException {
        userController = new UserController();
        productController = new ProductController();
        categoryController = new CategoryController();
        addressController = new AddressController();
    }

    public Controller(UserDAO userDAO) throws SQLException {
        userController = new UserController(userDAO);
        productController = new ProductController();
        categoryController = new CategoryController();
        addressController = new AddressController();
    }

    public Controller(AddressDAO addressDAO) throws SQLException {
        userController = new UserController();
        productController = new ProductController();
        categoryController = new CategoryController();
        addressController = new AddressController(addressDAO);
    }

    public Controller(CategoryDAO categoryDAO) throws SQLException{
        userController = new UserController();
        productController = new ProductController();
        categoryController = new CategoryController(categoryDAO);
        addressController = new AddressController();
    }

    public Controller(ProductDAO productDAO) throws SQLException{
        userController = new UserController();
        productController = new ProductController(productDAO);
        categoryController = new CategoryController();
        addressController = new AddressController();
    }


    @Override
    public void handle(String s,
                       Request request,
                       HttpServletRequest httpServletRequest,
                       jakarta.servlet.http.HttpServletResponse httpServletResponse) throws IOException {
        if (s.endsWith("/users") || s.endsWith("/user")) {
            userController.handle(s, request, httpServletRequest, httpServletResponse);
            return;
        }
        if (s.endsWith("/products") || s.endsWith("/product")) {
            productController.handle(s, request, httpServletRequest, httpServletResponse);
            return;
        }
        if (s.endsWith("/categories") || s.endsWith("/category")) {
            categoryController.handle(s, request, httpServletRequest, httpServletResponse);
            return;
        }
        if (s.endsWith("/addresses") || s.endsWith("/address")) {
            addressController.handle(s, request, httpServletRequest, httpServletResponse);
        }
        else {
            httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            request.setHandled(true);
        }
    }

}
