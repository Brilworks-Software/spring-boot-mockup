# Getting Started for Spring Boot Mockup APP

## Project Structure

The project has the following structure:

```
├── src
│   ├── main
│   │   ├── java
│   │   │   ├── com
│   │   │   │   ├── brilworks
│   │   │   │   │   ├── mockup
│   │   │   │   │   │   ├── controller
│   │   │   │   │   │   │   └── UserController.java
│   │   │   │   │   │   ├── entity
│   │   │   │   │   │   │   └── User.java
│   │   │   │   │   │   ├── repository
│   │   │   │   │   │   │   └── UserRepository.java
│   │   │   │   │   │   └── service
│   │   │   │   │   │       ├── UserService.java
│   │   │   │   └── resources
│   │   │   │       └── application.yml
│   │   └── test
│   │       └── java
│   │           ├── com
│   │           │   └── brilworks
│   │           │       └── mockup
│   │           │           ├── controller
│   │           │           │   └── UserControllerTest.java
│   │           │           └── service
│   │           │               └── UserServiceTest.java
├── pom.xml
└── README.md
```

- The main code is located in the `src/main/java` directory.
- The unit tests are located in the `src/test/java` directory.

### Prerequisites

- Java Development Kit (JDK) 17
- Gradle
- Junit 5
- H2 DB
- Open API
- Sentry

### Installing

1. Clone the repository to your local machine:
2. Run with command `./gradlew bootRun`

## Server URL

The API server is running at: `http://localhost:5000`

API Documentation: `http://localhost:5000/swagger-ui/index.html`

### Access token flow
#### Client login
The user will first need to call:
<pre>  POST /api/auth/login</pre>
Example Body:
<pre>  {
    "user_name" : "abc@xyz.com",
    "password" : "Qwerty@123"
  }</pre>
If the response code is 200, the login was successful. In the response, you will see:
  <pre>
  {
    "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzY09wMDFJbmdTekEyb3BjTVlTdCIsImNyZWF0ZWQiOjE2OTIzNDU2OTkxOTUsImV4cCI6MTY5NDI2MDk1OH0.9xZLFMkDV-436e5mhnevYFg_-yQ3PaXGUIcoUYjWsnkHumjvFLUij8xKQcGZkSBwUMzfuyaZsMSY2tQHx2Tvjw"
}
  </pre>
Use the access token to make authenticated requests to the API server by adding the Authentication header on each request:
<pre>    Authentication: Bearer {access_token}</pre>

## API Endpoints

### Create a new user

Creates a new user with provided details.

- **HTTP Method:** POST
- **Endpoint:** `/api/users`
- **Request Body:**
    - Content-Type: `application/json`
    - Schema: User object

```json
{
  "name": "Jon Smith",
  "email": "john.s@brilworks.com"
}
```

- **Responses:**
    - **200 OK:** User created successfully.
        - Content: Created User object
    - **404 Not Found:** Error creating user.
        - Content: Error response object

### Get a user by ID

Retrieves a user's details based on their ID.

- **HTTP Method:** GET
- **Endpoint:** `/api/users/{id}`
- **Parameters:**
    - `id` (path parameter) - User ID (integer)
- **Responses:**
    - **200 OK:** User details retrieved successfully.
        - Content: User object

```json
{
  "success": true,
  "result": {
    "created": "2023-08-04T16:32:33.591058+05:30",
    "updated": "2023-08-04T16:32:33.591058+05:30",
    "active": false,
    "id": 2,
    "name": "Jon Smith",
    "email": "john.s@brilworks.com"
  }
}
```

- **404 Not Found:** User with the specified ID not found.
    - Content:

```json
{
  "success": false,
  "error": {
    "code": 404,
    "txt": "User not found with ID: 3"
  }
}
```

### Update an existing user

Updates an existing user's details.

- **HTTP Method:** PUT
- **Endpoint:** `/api/users/{id}`
- **Parameters:**
    - `id` (path parameter) - User ID (integer)
- **Request Body:**
    - Content-Type: `application/json`
    - Schema: User object
- **Responses:**
    - **200 OK:** User details updated successfully.
        - Content: Updated User object
    - **404 Not Found:** User with the specified ID not found.
        - Content: Error response object

### Delete a user by ID

Deletes a user based on their ID.

- **HTTP Method:** DELETE
- **Endpoint:** `/api/users/{id}`
- **Parameters:**
    - `id` (path parameter) - User ID (integer)
- **Responses:**
    - **200 OK:** User deleted successfully.
    - **404 Not Found:** User with the specified ID not found.
        - Content: Error response object

### Get all users

Get all Users with Pagination and Search Options Retrieves a paginated list of all users with optional search criteria.

- **Method:** GET
- **Endpoint:** `/api/users`
- **Parameters:**
- **page (query parameter)** - The page number for pagination. The default is 0. (optional, integer, format: int32)
- **size (query parameter)** - The number of items per page. The default is 10. (optional, integer, format: int32)
- **searchQuery (query parameter)** - The search query to filter users. (optional, string)
- **Responses:**
- **Responses:**
    - **200 OK:** User details retrieved successfully.
        - Content: User object

```json
{
  "success": true,
  "result": {
    "data": [
      {
        "created": "2023-08-04T17:43:59.387379+05:30",
        "updated": "2023-08-04T17:43:59.38837+05:30",
        "active": false,
        "id": 1,
        "name": "John Smith",
        "email": "john.s@brilworks.com"
      }
    ],
    "count": 1
  }
}
```

### Running the Tests

- Run Tests with command `./gradlew test`

The tests will execute, and the results will be displayed in the console.

## Conclusion

Thank you for exploring this API documentation. We hope you found it helpful in understanding the available endpoints,
request parameters, and response structures for the sample APIs.

If you have any questions, encounter issues, or need further assistance, please don't hesitate to reach out to our team
at hello@brilworks.com.

Happy coding! 🚀