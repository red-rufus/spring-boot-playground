# Spring Boot GraphQL Application

This project is a simple Spring Boot application demonstrating the use of GraphQL with a library system. It provides basic CRUD operations for books and authors using GraphQL APIs.

## Features
- Manage authors and books
- GraphQL API for querying and mutating data
- Sample data initialization on startup

## Technologies Used
- Java 21
- Spring Boot
- Spring Data JPA
- GraphQL Java Tools
- H2 Database (in-memory, for development)

## Getting Started

### Prerequisites
- Java 21
- Maven

### Running the Application

1. Clone the repository:
   ```sh
   git clone <repository-url>
   cd spring-boot-graphql
   ```
2. Build and run the application:
   ```sh
   ./mvnw spring-boot:run
   ```
   or on Windows:
   ```cmd
   mvnw.cmd spring-boot:run
   ```
3. The application will start on `http://localhost:8080`.

### Accessing GraphQL Playground
You can test the GraphQL APIs through Postman at:
- `http://localhost:8080/graphql`

### Example GraphQL Queries

#### Get all books
```graphql
query {
  books {
    id
    title
    price
    pages
    author {
      id
      name
    }
  }
}
```

#### Add a new author
```graphql
mutation {
  createAuthor(name: "New Author") {
    id
    name
  }
}
```

## Project Structure
- `entities/` - JPA entities for Author and Book
- `repositories/` - Spring Data JPA repositories
- `services/` - Service layer for business logic
- `controllers/` - GraphQL resolvers/controllers
- `resources/graphql/schema.graphqls` - GraphQL schema definition