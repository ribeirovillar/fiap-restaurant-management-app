# Restaurant Management Application Documentation

## Table of Contents
1. [Project Introduction](#project-introduction)
2. [System Architecture](#system-architecture)
   - [Clean Architecture](#clean-architecture)
   - [Layer Responsibilities](#layer-responsibilities)
   - [Package Structure](#package-structure)
3. [Domain Model](#domain-model)
   - [Core Entities](#core-entities)
   - [Entity Relationships](#entity-relationships)
4. [Technology Stack](#technology-stack)
5. [API Documentation](#api-documentation)
   - [User Management API](#user-management-api)
   - [User Type Management API](#user-type-management-api)
   - [Authentication API](#authentication-api)
   - [Restaurant Management API](#restaurant-management-api)
   - [Menu Item Management API](#menu-item-management-api)
6. [Configuration and Setup](#configuration-and-setup)
   - [Prerequisites](#prerequisites)
   - [Environment Setup](#environment-setup)
   - [Database Configuration](#database-configuration)
   - [Docker Deployment](#docker-deployment)
7. [Testing Strategy](#testing-strategy)
   - [Unit Testing](#unit-testing)
   - [Integration Testing](#integration-testing)
8. [Feature Details](#feature-details)
   - [User Management](#user-management)
   - [Restaurant Management](#restaurant-management)
   - [Menu Management](#menu-management)
   - [Exception Handling](#exception-handling)
9. [Security Considerations](#security-considerations)
10. [Future Enhancements](#future-enhancements)

## Project Introduction

The Restaurant Management Application is a comprehensive backend system designed for restaurant operations. Built with a modern tech stack and clean architecture principles, it provides a scalable foundation for restaurant businesses to manage their digital presence.

The application enables restaurant owners to register their establishments, manage menus, and provides customers with the ability to browse restaurants and view menu items. The system features role-based access control through user types (CUSTOMER, OWNER), facilitating appropriate permissions for different user categories.

This document provides detailed technical documentation covering the architecture, API endpoints, configuration instructions, and feature descriptions of the Restaurant Management Application.

## System Architecture

### Clean Architecture

The application follows the Clean Architecture pattern proposed by Robert C. Martin, organizing code in concentric layers with the domain at the center. This approach ensures:

- **Independence from frameworks**: The core business logic doesn't depend on external frameworks.
- **Testability**: Business rules can be tested without UI, database, or any external elements.
- **Independence from UI**: The UI can change without affecting the rest of the system.
- **Independence from database**: The database can be changed without affecting the rest of the system.
- **Independence from external agencies**: Business rules don't know about the outside world.

### Layer Responsibilities

The application is organized into the following layers:

1. **Core Layer**
   - **Domain**: Contains business entities that represent core business concepts and rules.
   - **Use Cases**: Implements specific business rules and application logic.
   - **Gateways (Interfaces)**: Defines contracts for data access and external communications.
   - **Exceptions**: Business-specific exception classes.

2. **Adapter Layer**
   - **Web**: REST controllers that handle HTTP requests and responses.
   - **Database**: JPA entities, repositories, and gateway implementations.
   - **Presenters**: Transform data between the use cases and external format.

3. **Configuration Layer**
   - **Security Configuration**: Authentication and authorization setup.
   - **Bean Configuration**: Application component wiring.
   - **Exception Handling**: Global error handling for consistent responses.

### Package Structure

The application follows a well-defined package structure:

```
src/
├── main/
│   ├── java/fiap/restaurant/app/
│   │   ├── adapter/
│   │   │   ├── database/jpa/
│   │   │   │   ├── entity/
│   │   │   │   ├── repository/
│   │   │   │   └── gateway/
│   │   │   ├── presenter/impl/
│   │   │   └── web/
│   │   │       ├── json/
│   │   │       └── *ApiController.java
│   │   ├── configuration/
│   │   │   └── GlobalExceptionHandler.java
│   │   ├── core/
│   │   │   ├── domain/
│   │   │   ├── exception/
│   │   │   ├── gateway/
│   │   │   └── usecase/
│   │   └── AppApplication.java
```

## Domain Model

### Core Entities

The application centers around these core domain entities:

1. **User**: Represents users of the system (customers, restaurant owners)
2. **UserType**: Defines the user roles (CUSTOMER, OWNER)
3. **Restaurant**: Represents a restaurant establishment
4. **MenuItem**: Represents items on a restaurant's menu
5. **Address**: Represents physical location information
6. **BusinessHours**: Represents when a restaurant is open for business
7. **CuisineType**: Represents types of cuisine (Italian, Japanese, etc.)

### Entity Relationships

The domain entities relate to each other in the following ways:

- A **User** has one **UserType** (CUSTOMER, OWNER)
- A **User** (with OWNER type) can own multiple **Restaurants**
- A **Restaurant** has one **Address**
- A **Restaurant** has one **BusinessHours** configuration
- A **Restaurant** has one **CuisineType**
- A **Restaurant** can have multiple **MenuItems**

## Technology Stack

The application uses a modern technology stack:

- **Java 21**: Latest LTS version of Java
- **Spring Boot 3.3.x**: Modern application framework
- **Spring Security**: Authentication and authorization
- **Spring Data JPA**: Data access layer
- **Hibernate**: ORM for database interaction
- **PostgreSQL**: Primary production database
- **H2 Database**: In-memory database for testing
- **Flyway**: Database migration and versioning
- **Maven**: Project build and dependency management
- **JUnit 5**: Unit and integration testing framework
- **Mockito**: Mocking framework for tests
- **Lombok**: Reduces boilerplate code
- **Swagger/OpenAPI**: API documentation
- **BCrypt**: Password encryption
- **Docker & Docker Compose**: Containerization and orchestration

## API Documentation

The REST API is organized around resources and uses standard HTTP methods. All responses are in JSON format.

### Base URL

All API URLs referenced in the documentation have the following base:

```
http://localhost:8080/api/v1
```

### User Management API

#### Create User

- **Endpoint**: `POST /users`
- **Description**: Create a new user
- **Request Body**:
  ```json
  {
    "name": "John Doe",
    "login": "johndoe",
    "email": "john.doe@example.com",
    "password": "securepassword",
    "userTypeId": "UUID_OF_USER_TYPE",
    "address": {
      "street": "123 Main St",
      "city": "New York",
      "state": "NY",
      "zipCode": "10001",
      "country": "USA"
    }
  }
  ```
- **Response**: `201 Created` with the created user data

#### Get All Users

- **Endpoint**: `GET /users`
- **Description**: Retrieve all users
- **Response**: `200 OK` with array of users

#### Get User by ID

- **Endpoint**: `GET /users/{id}`
- **Description**: Retrieve a specific user by ID
- **Path Parameters**: `id` - UUID of the user
- **Response**: `200 OK` with user data or `404 Not Found`

#### Update User

- **Endpoint**: `PUT /users/{id}`
- **Description**: Update an existing user
- **Path Parameters**: `id` - UUID of the user
- **Request Body**: Same as create user
- **Response**: `200 OK` with updated user data or `404 Not Found`

#### Delete User

- **Endpoint**: `DELETE /users/{id}`
- **Description**: Delete a user
- **Path Parameters**: `id` - UUID of the user
- **Response**: `204 No Content` or `404 Not Found`

### User Type Management API

#### Create User Type

- **Endpoint**: `POST /user-types`
- **Description**: Create a new user type
- **Request Body**:
  ```json
  {
    "name": "MANAGER"
  }
  ```
- **Response**: `201 Created` with the created user type data

#### Get All User Types

- **Endpoint**: `GET /user-types`
- **Description**: Retrieve all user types
- **Response**: `200 OK` with array of user types

#### Get User Type by ID

- **Endpoint**: `GET /user-types/{id}`
- **Description**: Retrieve a specific user type by ID
- **Path Parameters**: `id` - UUID of the user type
- **Response**: `200 OK` with user type data or `404 Not Found`

#### Get User Type by Name

- **Endpoint**: `GET /user-types/name/{name}`
- **Description**: Retrieve a specific user type by name
- **Path Parameters**: `name` - Name of the user type (e.g., "CUSTOMER")
- **Response**: `200 OK` with user type data or `404 Not Found`

#### Update User Type

- **Endpoint**: `PUT /user-types/{id}`
- **Description**: Update an existing user type
- **Path Parameters**: `id` - UUID of the user type
- **Request Body**: Same as create user type
- **Response**: `200 OK` with updated user type data or `404 Not Found`

#### Delete User Type

- **Endpoint**: `DELETE /user-types/{id}`
- **Description**: Delete a user type
- **Path Parameters**: `id` - UUID of the user type
- **Response**: `204 No Content` or `400 Bad Request` if it's a system default type

### Authentication API

#### User Login

- **Endpoint**: `POST /users/login`
- **Description**: Authenticate a user
- **Request Body**:
  ```json
  {
    "login": "johndoe",
    "password": "securepassword"
  }
  ```
- **Response**: `200 OK` with user data or `401 Unauthorized`

#### Update Password

- **Endpoint**: `POST /users/password`
- **Description**: Update a user's password
- **Request Body**:
  ```json
  {
    "login": "johndoe",
    "currentPassword": "oldpassword",
    "newPassword": "newpassword"
  }
  ```
- **Response**: `200 OK` or `400 Bad Request` if passwords don't match

### Restaurant Management API

#### Create Restaurant

- **Endpoint**: `POST /restaurants`
- **Description**: Create a new restaurant
- **Request Body**:
  ```json
  {
    "name": "Italian Delight",
    "cuisineType": "ITALIAN",
    "ownerId": "UUID_OF_OWNER",
    "address": {
      "street": "456 Park Ave",
      "city": "New York",
      "state": "NY",
      "zipCode": "10022",
      "country": "USA"
    },
    "businessHours": {
      "openingTime": "11:00",
      "closingTime": "22:00"
    }
  }
  ```
- **Response**: `201 Created` with the created restaurant data

#### Get All Restaurants

- **Endpoint**: `GET /restaurants`
- **Description**: Retrieve all restaurants
- **Response**: `200 OK` with array of restaurants

#### Get Restaurant by ID

- **Endpoint**: `GET /restaurants/{id}`
- **Description**: Retrieve a specific restaurant by ID
- **Path Parameters**: `id` - UUID of the restaurant
- **Response**: `200 OK` with restaurant data or `404 Not Found`

#### Get Restaurants by Owner ID

- **Endpoint**: `GET /restaurants/owner/{ownerId}`
- **Description**: Retrieve all restaurants owned by a specific user
- **Path Parameters**: `ownerId` - UUID of the owner
- **Response**: `200 OK` with array of restaurants

#### Find Restaurants by Name

- **Endpoint**: `GET /restaurants/name/{name}`
- **Description**: Find restaurants by name (partial match)
- **Path Parameters**: `name` - Name or part of the name
- **Response**: `200 OK` with array of matching restaurants

#### Find Restaurants by Cuisine Type

- **Endpoint**: `GET /restaurants/cuisine/{cuisineType}`
- **Description**: Find restaurants by cuisine type
- **Path Parameters**: `cuisineType` - Type of cuisine (e.g., "ITALIAN")
- **Response**: `200 OK` with array of matching restaurants

#### Search Restaurants

- **Endpoint**: `GET /restaurants/search`
- **Description**: Search restaurants by name or cuisine type
- **Query Parameters**: 
  - `name` (optional) - Name to search for
  - `cuisineType` (optional) - Cuisine type to search for
- **Response**: `200 OK` with array of matching restaurants

#### Update Restaurant

- **Endpoint**: `PUT /restaurants/{id}`
- **Description**: Update an existing restaurant
- **Path Parameters**: `id` - UUID of the restaurant
- **Request Body**: Same as create restaurant
- **Response**: `200 OK` with updated restaurant data or `404 Not Found`

#### Delete Restaurant

- **Endpoint**: `DELETE /restaurants/{id}`
- **Description**: Delete a restaurant
- **Path Parameters**: `id` - UUID of the restaurant
- **Response**: `204 No Content` or `404 Not Found`

### Menu Item Management API

#### Create Menu Item

- **Endpoint**: `POST /restaurants/{restaurantId}/menu-items`
- **Description**: Create a new menu item for a restaurant
- **Path Parameters**: `restaurantId` - UUID of the restaurant
- **Request Body**:
  ```json
  {
    "name": "Margherita Pizza",
    "description": "Classic pizza with tomato sauce, mozzarella, and basil",
    "price": 12.99,
    "imageUrl": "https://example.com/images/pizza.jpg"
  }
  ```
- **Response**: `201 Created` with the created menu item data

#### Get All Menu Items for a Restaurant

- **Endpoint**: `GET /restaurants/{restaurantId}/menu-items`
- **Description**: Retrieve all menu items for a specific restaurant
- **Path Parameters**: `restaurantId` - UUID of the restaurant
- **Response**: `200 OK` with array of menu items

#### Get Menu Item by ID

- **Endpoint**: `GET /restaurants/{restaurantId}/menu-items/{id}`
- **Description**: Retrieve a specific menu item by ID
- **Path Parameters**: 
  - `restaurantId` - UUID of the restaurant
  - `id` - UUID of the menu item
- **Response**: `200 OK` with menu item data or `404 Not Found`

#### Update Menu Item

- **Endpoint**: `PUT /restaurants/{restaurantId}/menu-items/{id}`
- **Description**: Update an existing menu item
- **Path Parameters**: 
  - `restaurantId` - UUID of the restaurant
  - `id` - UUID of the menu item
- **Request Body**: Same as create menu item
- **Response**: `200 OK` with updated menu item data or `404 Not Found`

#### Delete Menu Item

- **Endpoint**: `DELETE /restaurants/{restaurantId}/menu-items/{id}`
- **Description**: Delete a menu item
- **Path Parameters**: 
  - `restaurantId` - UUID of the restaurant
  - `id` - UUID of the menu item
- **Response**: `204 No Content` or `404 Not Found`

## Configuration and Setup

### Prerequisites

To set up and run the application, you need:

- Java 21 or higher
- Maven 3.8.x or higher
- PostgreSQL 14.x or higher
- Docker and Docker Compose (optional, for containerized deployment)

### Environment Setup

#### Option 1: Running with Docker Compose (Recommended)

1. Clone the repository:
   ```bash
   git clone https://github.com/ribeirovillar/fiap-restaurant-management-app.git
   cd fiap-restaurant-management-app
   ```

2. Start the application stack with Docker Compose:
   ```bash
   docker compose up -d
   ```

   This will:
   - Build the application from source
   - Start PostgreSQL in a container
   - Start the application in a container
   - Connect the application to the database

3. Access the application:
   - API: http://localhost:8080/api/v1
   - Swagger UI: http://localhost:8080/swagger-ui.html

4. Stop the application:
   ```bash
   docker compose down
   ```

#### Option 2: Manual Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/ribeirovillar/fiap-restaurant-management-app.git
   cd fiap-restaurant-management-app
   ```

2. Build the application:
   ```bash
   mvn clean install
   ```

3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

4. Access the application:
   - API: http://localhost:8080/api/v1
   - Swagger UI: http://localhost:8080/swagger-ui.html

### Database Configuration

#### PostgreSQL Setup

1. Create a PostgreSQL database:
   ```sql
   CREATE DATABASE fiap_food;
   ```

2. Configure database connection in `application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/fiap_food
   spring.datasource.username=postgres
   spring.datasource.password=postgres
   spring.datasource.driver-class-name=org.postgresql.Driver
   
   spring.jpa.hibernate.ddl-auto=none
   spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
   spring.jpa.show-sql=true
   ```

### Docker Deployment

The application includes Docker configuration for containerized deployment:

- **Dockerfile**: Defines the application container build
- **docker-compose.yml**: Orchestrates both application and database containers

Configuration in `docker-compose.yml`:
```yaml
version: '3.8'
services:
  app:
    build: .
    ports:
      - "8080:8080"
    depends_on:
      - postgres
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/fiap_food
      SPRING_DATASOURCE_USERNAME: postgres
      SPRING_DATASOURCE_PASSWORD: postgres
  
  postgres:
    image: postgres:14-alpine
    ports:
      - "5432:5432"
    environment:
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
      POSTGRES_DB: fiap_food
    volumes:
      - postgres-data:/var/lib/postgresql/data

volumes:
  postgres-data:
```

## Testing Strategy

The application includes a comprehensive testing strategy:

### Unit Testing

Unit tests focus on testing individual components in isolation:

- Domain entity validation logic
- Use case implementations
- Business rules

Run unit tests:
```bash
mvn test
```

### Integration Testing

Integration tests verify the correct interaction between components:

- API controllers
- Database operations
- End-to-end workflows

Run integration tests:
```bash
mvn test -Dtest="*IntegrationTest"
```

Run specific test class:
```bash
mvn test -Dtest="UserTypeTest"
```

## Feature Details

### User Management

The user management module provides functionality for managing users:

- **User Registration**: Create new users with different roles
- **User Types**: System supports CUSTOMER and OWNER roles with different permissions
- **Profile Management**: Update user profile information
- **Account Management**: Delete user accounts

Key implementation details:
- Passwords are hashed using BCrypt for security
- User emails must be unique
- User logins must be unique
- Each user can have a physical address

### Restaurant Management

The restaurant management module handles restaurant information:

- **Restaurant Registration**: Owners can register new restaurants
- **Restaurant Information**: Store and update restaurant details
- **Search Functionality**: Find restaurants by name, cuisine type
- **Owner Association**: Connect restaurants to their owners

Key implementation details:
- Each restaurant has a physical address
- Restaurants are associated with a cuisine type
- Business hours define when a restaurant is open
- Only owners can create and manage restaurants

### Menu Management

The menu management module handles restaurant menu items:

- **Menu Creation**: Add menu items to restaurants
- **Menu Modification**: Update menu item details
- **Menu Organization**: Group menu items by restaurant

Key implementation details:
- Menu items include name, description, price
- Optional image URL for visual representation
- Menu items are always associated with a specific restaurant

### Exception Handling

The application includes robust exception handling for data validation and business rules:

- **BusinessException**: Custom exception for business rule violations
- **GlobalExceptionHandler**: Central handling of exceptions
- **HTTP Status Codes**: Appropriate status codes for different error types

Example error response:
```json
{
  "timestamp": "2023-03-10T14:32:45.123Z",
  "status": 400,
  "error": "Bad Request",
  "message": "User type with name MANAGER already exists",
  "path": "/api/v1/user-types"
}
```

## Security Considerations

The application implements several security measures:

1. **Password Handling**
   - Passwords are never stored in plain text
   - BCrypt algorithm used for secure password hashing
   - Password validation during login

2. **Authorization**
   - Different user types have different access levels
   - OWNER users can only manage their own restaurants
   - Validation checks for appropriate permissions

3. **Input Validation**
   - All input data is validated before processing
   - Protection against invalid data entry

## Future Enhancements

Potential future enhancements for the system:

1. **JWT Authentication**
   - Implement token-based authentication
   - Support for refresh tokens

2. **Order Management**
   - Allow customers to place orders
   - Track order status
   - Order history

3. **Payment Integration**
   - Integrate with payment gateways
   - Support multiple payment methods

4. **Rating System**
   - Allow customers to rate restaurants
   - Restaurant reviews

5. **Notification System**
   - Email notifications
   - SMS notifications
   - Push notifications for mobile apps 