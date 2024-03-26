package com.aston.crud.dao;

import com.aston.crud.entities.Address;
import com.aston.crud.entities.Category;
import com.aston.crud.entities.User;
import com.aston.crud.util.DBConnection;
import net.bytebuddy.utility.dispatcher.JavaDispatcher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class AddressDAOImplTest {
    private AddressDAOImpl addressDAO;
    @Container
    static PostgreSQLContainer postgresqlContainer = new PostgreSQLContainer<>()
            .withDatabaseName("postgres")
            .withUsername("postgres")
            .withPassword("password");


    @BeforeEach
    public void setUp() throws SQLException {
        DBConnection.setDbUrl(postgresqlContainer.getJdbcUrl());
        createTables();
        insertDataIntoTables();

        addressDAO = new AddressDAOImpl();
    }

    @AfterEach
    public void tearDown() throws SQLException {
        dropTables();
    }

    @Test
    void testGetAddressById() throws SQLException {
        Address address1 = new Address(1,"123 Main St", "New York", "NY", "10001", 1);
        Address address2 = new Address(2,"456 Elm St'", "Los Angeles", "CA", "90001'", 2);
        Address address3 = new Address(3,"789 Oak St", "Chicago", "IL", "60001", 3);

        addressDAO.addAddress(address1);
        addressDAO.addAddress(address2);
        addressDAO.addAddress(address3);

        Address expectedAddress = address2;
        Address actualAddress = addressDAO.getAddressById(2);

        assertEquals(expectedAddress, actualAddress);
    }

    @Test
    void testGetAllAddresses() throws SQLException {
        Address address1 = new Address(1,"123 Main St", "New York", "NY", "10001", 1);
        Address address2 = new Address(2,"456 Elm St'", "Los Angeles", "CA", "90001'", 2);
        Address address3 = new Address(3,"789 Oak St", "Chicago", "IL", "60001", 3);

        addressDAO.addAddress(address1);
        addressDAO.addAddress(address2);
        addressDAO.addAddress(address3);

        List<Address> expectedAddress = Arrays.asList(address1, address2, address3);
        List<Address> actualAddress= addressDAO.getAllAddresses();

        assertEquals(expectedAddress, actualAddress);
    }

    @Test
    void testGetAddressesByUserId() throws SQLException {
        Address address1 = new Address(1,"123 Main St", "New York", "NY", "10001", 1);
        Address address2 = new Address(2,"456 Elm St'", "Los Angeles", "CA", "90001'", 2);
        Address address3 = new Address(3,"789 Oak St", "Chicago", "IL", "60001", 3);

        addressDAO.addAddress(address1);
        addressDAO.addAddress(address2);
        addressDAO.addAddress(address3);

        List<Address> expectedAddress = Arrays.asList(address2);
        List<Address> actualAddress = addressDAO.getAddressesByUserId(2);

        assertEquals(expectedAddress, actualAddress);
    }

    @Test
    void testDeleteNonExistingAddress() {
        int nonExistingAddressId = 9999;
        assertThrows(IllegalArgumentException.class, () -> addressDAO.deleteAddressById(nonExistingAddressId));
    }


    @Test
    void testDeleteAddressById() throws SQLException {
        Address address1 = new Address(1,"123 Main St", "New York", "NY", "10001", 1);
        Address address2 = new Address(2,"456 Elm St'", "Los Angeles", "CA", "90001'", 2);
        Address address3 = new Address(3,"789 Oak St", "Chicago", "IL", "60001", 3);

        addressDAO.addAddress(address1);
        addressDAO.addAddress(address2);
        addressDAO.addAddress(address3);

        List<Address> expectedAddress = Arrays.asList(address1, address2);
        addressDAO.deleteAddressById(3);
        List<Address> actualAddress= addressDAO.getAllAddresses();

        assertEquals(expectedAddress, actualAddress);
    }
    private void createTables() throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement()) {
            // Create users table
            statement.executeUpdate("CREATE TABLE users (id SERIAL PRIMARY KEY, username VARCHAR(50) NOT NULL, email VARCHAR(100) NOT NULL)");

            // Create categories table
            statement.executeUpdate("CREATE TABLE categories (id SERIAL PRIMARY KEY, name VARCHAR(100) NOT NULL)");

            // Create products table with foreign key constraint
            statement.executeUpdate("CREATE TABLE products (id SERIAL PRIMARY KEY, name VARCHAR(100) NOT NULL, " +
                    "price DECIMAL(10, 2) NOT NULL, category_id INT NOT NULL, " +
                    "FOREIGN KEY (category_id) REFERENCES categories(id))");

            // Create addresses table with foreign key constraint
            statement.executeUpdate("CREATE TABLE addresses (id SERIAL PRIMARY KEY, " +
                    "street VARCHAR(255) NOT NULL, city VARCHAR(100) NOT NULL, " +
                    "state VARCHAR(100), postal_code VARCHAR(20) NOT NULL, " +
                    "user_id INT NOT NULL, FOREIGN KEY (user_id) REFERENCES users(id))");

            System.out.println("Tables created successfully.");
        }
    }
    private void insertDataIntoTables() throws SQLException {
        try(Connection connection = DBConnection.getConnection();
            Statement statement = connection.createStatement()) {
            statement.executeUpdate("INSERT INTO users (username, email) VALUES ('jane_smith', 'jane@example.com');\n" +
                    "INSERT INTO users (username, email) VALUES ('alice_jones', 'alice@example.com');\n" +
                    "INSERT INTO users (username, email) VALUES ('john_doe', 'john@example.com');");
        }
    }

    private void dropTables() throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement()) {
            // Drop tables if they exist
            statement.executeUpdate("DROP TABLE IF EXISTS addresses");
            statement.executeUpdate("DROP TABLE IF EXISTS products");
            statement.executeUpdate("DROP TABLE IF EXISTS categories");
            statement.executeUpdate("DROP TABLE IF EXISTS users");

            System.out.println("Tables dropped successfully.");
        }
    }
}