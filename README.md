# Restaurant Management Application

## Overview
The Restaurant Management Application is a modern, scalable backend system for restaurant operations management. Built with a clean architecture approach, the application offers user management functionality with secure authentication, setting a foundation for broader restaurant management features.

## Technologies

### Backend
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
- **MapStruct**: Object mapping between layers
- **Swagger/OpenAPI**: API documentation
- **BCrypt**: Password encryption
- **Docker & Docker Compose**: Containerization and orchestration

## Architecture

The application follows Clean Architecture principles, with a clear separation of concerns:

### Core Layer
- **Domain**: Contains business entities that encapsulate enterprise-wide business rules
- **Use Cases**: Contains application-specific business rules
- **Controllers**: Orchestrates the flow of data to and from use cases
- **Gateways**: Interfaces that define data persistence contracts

### Adapter Layer
- **Web**: REST controllers and JSON DTOs
- **Database**: JPA entities and repositories
- **Presenters**: Implementations for data transformation between layers

### Configuration Layer
- **Security Configuration**: Authentication and authorization setup
- **Bean Configuration**: Application component wiring
- **Exception Handling**: Global error handling

## Project Structure
```
src/
├── main/
│   ├── java/fiap/restaurant/app/
│   │   ├── adapter/
│   │   │   ├── database/
│   │   │   │   └── jpa/
│   │   │   ├── presenter/
│   │   │   └── web/
│   │   ├── config/
│   │   ├── core/
│   │   │   ├── controller/
│   │   │   ├── domain/
│   │   │   ├── gateway/
│   │   │   └── usecase/
│   │   └── AppApplication.java
│   └── resources/
│       ├── db/migration/
│       └── application.properties
└── test/
    ├── java/fiap/restaurant/app/
    │   ├── adapter/
    │   │   └── web/
    │   │       └── integration/
    │   ├── core/
    │   └── config/
    └── resources/
        └── application-test.properties
```

## Getting Started

### Prerequisites
- Java 21 or higher
- Maven 3.8.x or higher
- PostgreSQL 14.x or higher
- Docker and Docker Compose (optional, for containerized deployment)

### Running with Docker Compose (Recommended)

The easiest way to get started is using Docker Compose, which will set up both the PostgreSQL database and the application in containers:

1. Clone the repository:
```bash
git clone https://github.com/yourusername/fiap-restaurante-management-app.git
cd fiap-restaurante-management-app
```

2. Start the application stack with Docker Compose:
```bash
docker compose up -d
```

This command will:
- Build the application from source code
- Package it into a Docker image
- Start a PostgreSQL container
- Start the application container connected to the PostgreSQL database

3. The application will be available at:
   - API: http://localhost:8080/api/v1
   - Swagger UI: http://localhost:8080/swagger-ui.html

4. To stop the containers:
```bash
docker compose down
```

5. To rebuild the application after code changes:
```bash
docker compose up --build -d
```

### Manual Setup and Run

#### Database Setup
1. Create a PostgreSQL database:
```sql
CREATE DATABASE fiap_food;
```

2. Update database connection properties in `application.properties` if needed:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/fiap_food
spring.datasource.username=postgres
spring.datasource.password=postgres
```

#### Building and Running the Application
1. Clone the repository:
```bash
git clone https://github.com/yourusername/fiap-restaurante-management-app.git
cd fiap-restaurante-management-app
```

2. Build the application:
```bash
mvn clean install
```

3. Run the application:
```bash
mvn spring-boot:run
```

4. The application will be available at:
   - API: http://localhost:8080/api/v1
   - Swagger UI: http://localhost:8080/swagger-ui.html

### Running Tests
```bash
# Run all tests
mvn test

# Run specific test classes
mvn test -Dtest="LoginIntegrationTest"
mvn test -Dtest="UpdatePasswordIntegrationTest"
```

## API Endpoints

### User Management
- **POST /api/v1/users**: Create a new user
- **GET /api/v1/users**: Get all users
- **GET /api/v1/users/{id}**: Get user by ID
- **PUT /api/v1/users/{id}**: Update a user
- **DELETE /api/v1/users/{id}**: Delete a user

### Authentication
- **POST /api/v1/users/login**: Validate user credentials
- **POST /api/v1/users/password**: Update user password

## Postman Collection

A Postman collection is included in the repository to help you test the API endpoints. The collection includes all available endpoints with pre-configured request bodies and parameters.

### Importing the Collection
1. Open Postman
2. Click on "Import" in the top left corner
3. Select the file `fiap-restaurant-management.postman_collection.json` from the project repository
4. Click "Import" to add the collection to your Postman workspace

### Using the Collection
1. After importing, you'll see the "FIAP Restaurant Management API" collection in your Postman sidebar
2. The collection uses environment variables:
   - `baseUrl`: Default is set to `http://localhost:8080` 
   - `userId`: Used to store a user ID for API calls that require it

3. Workflow for testing:
   - First, use the "Create User" request to create a new user
   - Copy the returned user ID into the `userId` variable (right-click and select "Set as variable value" → `userId`)
   - Now you can use all other requests with the correct user ID

### Testing Authentication
1. Create a user with the "Create User" request
2. Use the "Login" request with the same credentials to test authentication
3. To test password updating, use the "Update Password" request with the correct credentials

## Development Guidelines

### Adding a New Feature
1. Define the domain entity in the core layer
2. Create use case interfaces and implementations
3. Update or create controller methods
4. Implement gateways for data persistence
5. Create adapters for web and database interaction
6. Write unit and integration tests

### Testing Strategy
- Unit tests for use cases and business logic
- Integration tests for API endpoints
- In-memory H2 database for test environment

## Docker Configuration
The project includes Docker configuration files:
- **Dockerfile**: Multi-stage build process for the application
- **docker-compose.yml**: Orchestrates the PostgreSQL database and application containers

### Docker Image Structure
- Build stage: Uses Maven to compile and package the application
- Runtime stage: Uses minimal JRE image to run the application
- Exposed port: 8080
- Health check: Ensures database is ready before starting the application

## Contributing
1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a pull request

## License
This project is licensed under the MIT License - see the LICENSE file for details.