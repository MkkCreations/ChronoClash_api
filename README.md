
# CHRONOCLASH SERVER

## Description
This is the server for the Chronoclash game. It is a simple REST API that allows users to create accounts, login, and play the game.

## Installation
1. Clone the repository
2. Add `DATABASE_URL` environment variable to your system with the value `jdbc:mysql://127.0.0.1:3308/chronoclash_db`
3. Add `DATABASE_USERNAME` environment variable to your system with the value `root`
4. Add `DATABASE_PASSWORD` environment variable to your system with the value `password`
5. Run `docker compose up -d database` to start the database
6. Run the project in your IDE
7. The server will be running on port `8081`
8. The database will be running on port `3308`

## Usage
The server is a REST API that allows users to create accounts, login, and play the game. The API is deployed on Railway and can be accessed at `https://chronoclashapi-production.up.railway.app/`

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
- Git
- Railway

## Routes
### Auth
- Register: `POST /api/auth/register`
- Login: `POST /api/auth/login`
- Refresh Token: `POST /api/auth/refresh`
- Logout: `POST /api/auth/logout` Authenticated
- Logout All: `POST /api/auth/logoutAll` Authenticated
- Change Password: `POST /api/auth/change-password/{id}` Authenticated

### User
- Get User info: `GET /api/user/me` Authenticated
- Add game: `POST /api/user/game` Authenticated

## API
### Authentication
#### Register
`POST /api/auth/register`
##### Request
```json
{
  "name": "User",
  "username": "username",
  "email": "email@email.com",
  "password": "password"
}
```
##### Response
```json
{
    "id": "bd54fb35-5bec-42c3-8f9e-eb828d2b1433",
    "name": "Mohamed",
    "username": "username",
    "email": "moha@gmail.com",
    "image": null,
    "role": "USER",
    "logs": [],
    "level": {
      "id": "b34ba3e6-564d-4e47-b28c-b72f8244fa3a",
      "level": 0,
      "xp": 0
    },
    "games": [],
    "enabled": true,
    "authorities": [
      {
        "authority": "ROLE_USER"
      }
    ],
    "accountNonLocked": true,
    "credentialsNonExpired": true,
    "accountNonExpired": true
}
```
#### Login
`POST /api/auth/login`
##### Request
```json
{
  "username": "username",
  "password": "password"
}
```

##### Response
```json
{
  "user": {
    "id": "bd54fb35-5bec-42c3-8f9e-eb828d2b1433",
    "name": "User",
    "username": "username",
    "email": "email@email.com",
    "image": null,
    "role": "USER",
    "logs": [
      {
        "id": "37c754ff-520a-4fd6-89bb-ffad50f29837",
        "name": "Game",
        "operations": [
          {
            "id": "1721eb60-f971-4bd3-902d-e65f80cbdc86",
            "type": "Win",
            "description": "You won against alexandre and earned 40 xp",
            "date": "Fri Jan 12 12:32:31 CET 2024"
          }
        ]
      },
      {
        "id": "8e7384ee-0cac-486b-a3f4-306fc0e6ef82",
        "name": "User",
        "operations": [
          {
            "id": "06a6089d-8d5e-48f5-988e-9173dabefe81",
            "type": "login",
            "description": "User moha logged in",
            "date": "Fri Jan 12 11:27:57 CET 2024"
          }
        ]
      }
    ],
    "level": {
      "id": "b34ba3e6-564d-4e47-b28c-b72f8244fa3a",
      "level": 1,
      "xp": 40
    },
    "games": [
      {
        "id": "1440ee6b-4bdc-4422-9659-3896184986c6",
        "enemy": "Alexandre",
        "win": 1
      }
    ],
    "enabled": true,
    "authorities": [
      {
        "authority": "ROLE_USER"
      }
    ],
    "accountNonLocked": true,
    "credentialsNonExpired": true,
    "accountNonExpired": true
  },
  "accessToken": "...",
  "refreshToken": "..."
}
```

### v1.0.0
This is the first version of the API. It is deployed on Railway and can be accessed at `https://chronoclashapi-production.up.railway.app/`
