<p align="center">
  <img src="https://cubo.network/assets/images/cubo.svg" width="200">
</p>

# Backend

A Spring Boot application for managing users and their participations.

This application provides a RESTful API for creating, reading, updating, and deleting users. Each user has a unique name and a participation value, which cannot exceed 100% when combined with other users.

## API Endpoints

### User Controller

    POST /api/user/save: Create a new user
    GET /api/user/all: Retrieve all users
    PUT /api/user/update: Update an existing user
    DELETE /api/user/delete/{id}: Delete a user by ID

## Technical Details

### Technologies

    Spring Boot 3.3
    Java 22
    Lombok
    JPA for database persistence
    Hibernate for database interactions

## Database

The application uses a h2 database to store user data.

## Getting Started

### Prerequisites

    Java 22 installed
    Spring Boot CLI installed (optional)

### Running the Application

    Run the application using your IDE or by executing the following command: mvn spring-boot:run
    Access the API endpoints using a tool like Postman or cURL

## Docker

Building the Docker Image

To build a Docker image for this application, run the following command:

```docker
docker build -t itaucubonet.
```

Running the Docker Container

To run the Docker container, use the following command:

```docker
docker run -p 8080:8080 itaucubonet
```

This will start the application on port 8080. You can access the API endpoints using a tool like Postman or cURL.
