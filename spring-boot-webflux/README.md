# Spring Boot Webflux Reactive Application

This is a reactive web application built with Spring Boot and Spring Webflux. It provides a set of RESTful endpoints to manage a collection of books.

## Technologies Used

*   **Java 21**
*   **Spring Boot 3.3.4**
*   **Spring Webflux:** For building reactive, non-blocking web applications.
*   **Spring Data R2DBC:** For reactive database access.
*   **H2 Database:** An in-memory/file-based database.
*   **Maven:** For dependency management and build automation.

## Getting Started

### Prerequisites

*   Java 21 or later
*   Maven

### Running the Application

1.  Clone the repository:
    ```bash
    git clone <repository-url>
    ```
2.  Navigate to the project directory:
    ```bash
    cd spring-boot-webflux
    ```
3.  Build the project using Maven:
    ```bash
    mvn clean install
    ```
4.  Run the application:
    ```bash
    mvn spring-boot:run
    ```

The application will start on port `8009`.

## Database Configuration

The application uses an H2 file-based database. The connection details are configured in `src/main/resources/application.properties`:

*   **R2DBC URL:** `spring.r2dbc.url=r2dbc:h2:file:///./data/testdb`
*   **Username:** `sa`
*   **Password:** `admin`

The H2 console is enabled and can be accessed at `http://localhost:8009/h2-console`. Use the R2DBC URL from the `application.properties` file as the JDBC URL in the H2 console, but replace `r2dbc` with `jdbc`.

## API Endpoints

The following endpoints are available to manage books:

*   **Create a new book:**
    *   `POST /books`
    *   **Request Body:**
        ```json
        {
          "name": "Book Name",
          "description": "Book Description",
          "publisher": "Publisher Name",
          "author": "Author Name"
        }
        ```
*   **Get all books:**
    *   `GET /books`
*   **Get a single book by ID:**
    *   `GET /books/{bookId}`
*   **Update a book:**
    *   `PUT /books/{bookId}`
    *   **Request Body:**
        ```json
        {
          "name": "Updated Book Name",
          "description": "Updated Book Description",
          "publisher": "Updated Publisher Name",
          "author": "Updated Author Name"
        }
        ```
*   **Delete a book:**
    *   `DELETE /books/{bookId}`
*   **Search for books:**
    *   `GET /books/search?query=<search-term>`
