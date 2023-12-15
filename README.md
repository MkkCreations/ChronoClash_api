
# CHRONOCLASH SERVER

## Description
This is the server for the Chronoclash game. It is a simple REST API that allows users to create accounts, login, and play the game.

## Installation
1. Clone the repository
2. Run `docker compose up -d database` to start the database
3. Run the project in your IDE
4. The server will be running on port `8080`
5. The database will be running on port `3308`

## Usage
The server is a REST API that allows users to create accounts, login, and play the game. The API is documented using Swagger and can be accessed at `http://localhost:8080/swagger-ui.html`

## Stack
- Java 17
- Spring Boot
- Spring Data JPA
- Spring Security
- Maven
- JUnit 5
- JWT
- BCrypt
- Hibernate
- Lombok
- MySQL
- Docker
- Swagger
- Git
