# CRUD Application

This is a simple CRUD (Create, Read, Update, Delete) application built using Java Servlets and Jetty Server. It provides RESTful endpoints for managing users, products, addresses, and categories.

## Technologies Used

- Java Servlets
- Jetty Server
- JDBC
- Jackson JSON Library
- JUnit
- Mockito
- Java HttpClient

## How to Run

1. Clone this repository to your local machine.
   `git clone https://github.com/TanyaPakhomova/crud.git .`
2. Make sure you have Java and Gradle installed.
3. Set up your database and update the database configuration in `DBConnection.java`.
4. Run the following Gradle command to build the project:

   `./gradlew build`
5. Once the build is successful, start the application using the following command:

   `java -jar build/libs/crud-app.jar`
6. The application will start on port 8080 by default. You can access the API endpoints using tools like cURL, Postman, or any web browser.
 

## Postman collection 

`https://grey-space-659638.postman.co/workspace/telebot~9558477d-681c-4411-90da-9639f99891a8/collection/18284675-e7b2ff3c-b084-4946-b152-7cb678a896fe?action=share&creator=18284675`

