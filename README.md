# Warehouse Management Service

## Overview
The Warehouse Management Service is a Spring Boot application designed to manage warehouse operations, employees, and the receipt of inventory deliveries. This service provides a RESTful API for interacting with warehouse data and operations.

## Project Structure
The project follows a standard Gradle structure with the following key directories:

- **src/main/java/com/training/callum/whoms**: Contains the main application code.
  - **config**: Configuration classes for the application.
  - **domain**: Domain model classes representing core entities.
  - **repository**: Repository interfaces for data access.
  - **service**: Service classes implementing business logic.
  - **web**: Controller classes handling HTTP requests.

- **src/test/java/com/training/callum/whoms**: Contains unit tests for the application.
  - **config**: Test configuration classes.
  - **domain**: Unit tests for domain model classes.
  - **repository**: Unit tests for repository classes.
  - **service**: Unit tests for service classes.
  - **web**: Unit tests for controller classes.

## Dependencies
The project uses the Spring Boot BOM for dependency management, including:
- Spring Boot Actuator for monitoring and managing the application.
- Spring Boot DevTools for development-time features.
- Testing libraries such as JUnit and Mockito for unit and integration testing.

## Running the Application
To run the application, use the following command:
```
./gradlew bootRun
```

## Building the Application
To build the application, use the following command:
```
./gradlew clean build
```

## Testing
To run the tests, use the following command:
```
./gradlew test
```

## Configuration
The main application configuration is located in `src/main/resources/application.properties`. Test-specific configurations can be found in `src/test/resources/application-test.properties`.

## Contribution
Contributions to the project are welcome. Please ensure that all tests pass before submitting a pull request.