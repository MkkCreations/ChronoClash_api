
# CHRONOCLASH SERVER

## Description
This is the server for the ChronoClash game. It is a simple REST API that allows users to create accounts, login, and play the game.

## Installation
1. Clone the repository
2. Add `DATABASE_URL` environment variable to your system with the value `jdbc:mysql://127.0.0.1:3308/chronoclash_db`
3. Add `DATABASE_USERNAME` environment variable to your system with the value `root`
4. Add `DATABASE_PASSWORD` environment variable to your system with the value `password`
5. Add `REDIS_HOST` environment variable to your system with the value `localhost`
6. Add `REDIS_PORT` environment variable to your system with the value `6379`
7. Run `docker compose up -d` to start the database and the Redis server
8. Run the project in your IDE
9. The server will be running on port `8081`
10. The database will be running on port `3308`
11. The Redis server will be running on port `6379`

> ### .end.json example
> ```json
> {
>   "DATABASE_URL":"jdbc:mysql://127.0.0.1:3308/chronoclash_db",
>   "DATABASE_USERNAME":"root",
>   "DATABASE_PASSWORD":"password",
>   "REDIS_HOST":"localhost",
>   "REDIS_PORT":"6379",
>   "SECURITY_USER": "chronoclash",
>   "SECURITY_PASSWORD": "chronoclash"
> }
> ```



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
- Redis
- Cache
- MySQL
- Docker
- Git
- Railway

## Cache
The server uses Redis to cache the user's data. The cache is used to store the list of users. The cache is used to reduce the number of database calls and to improve the performance of the server.

> ### Performance
> The performance of the server has been improved by using Redis cache. The performance of the server has been improved by `2122%` by using Redis cache.
>> #### Database Request: `191 ms`
>> #### Cache Request: `9 ms`


## Database
The server uses MySQL to store the user's data. The database is used to store the user's information, the user's games, and the user's logs. The database is used to store the user's friends and the user's connections.

## Security
The server uses JWT to authenticate users. The server uses Spring Security to secure the endpoints. The server uses BCrypt to hash the user's password.

## Routes
### Auth
- Signup: `POST /api/auth/signup`
- Login: `POST /api/auth/login`
- Refresh Token: `POST /api/auth/refresh`
- Logout: `POST /api/auth/logout` Authenticated
- Logout All: `POST /api/auth/logoutAll` Authenticated
- Change Password: `POST /api/auth/change-password/{id}` Authenticated

### User
- Get User info: `GET /api/user/me` Authenticated
- Add game: `POST /api/user/game` Authenticated
- Get All Users: `GET /api/user/users` Authenticated

### Friend
- Add Friend: `POST /api/friend/add` Authenticated
- Accept Friend: `POST /api/friend/accept/{id}` Authenticated
- Get Friends: `GET /api/friend/all` Authenticated
- Friends Notifications: `GET /api/friend/notifications` Authenticated

### Connections
- Get All Connections: `GET /api/network/connections` Authenticated
- Delete Connection: `DELETE /api/network/disconnect/{id}` Authenticated


## API
### Authentication

<details>
  <summary>Signup</summary>

`POST /api/auth/signup`
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
</details>

<details>
  <summary>Login</summary>

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
</details>

### v2.0.0
This is the first version of the API. It is deployed on Railway and can be accessed at `https://chronoclashapi-production.up.railway.app/`
