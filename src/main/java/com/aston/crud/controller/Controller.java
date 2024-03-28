package com.aston.crud.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class Controller extends AbstractHandler {

    private final UserController userController;
    private final ProductController productController;
    private final CategoryController categoryController;
    private final AddressController addressController;

    public Controller(
            UserController userController,
            ProductController productController,
            CategoryController categoryController,
            AddressController addressController
    ) {
        this.userController = userController;
        this.productController = productController;
        this.categoryController = categoryController;
        this.addressController = addressController;
    }

    public Controller() throws SQLException {
        userController = new UserController();
        productController = new ProductController();
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
