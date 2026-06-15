# Spring Boot Event Sourcing with Kafka and MongoDB

This project is a demonstration of an event-sourcing architecture using Spring Boot, Apache Kafka, and MongoDB. It consists of two services: an `order-service` and a `shipping-service`.

## Architecture

- **`order-service`**: Exposes a REST API to create orders. When an order is created, it publishes an `OrderCreated` event to a Kafka topic.
- **`shipping-service`**: Listens for `OrderCreated` events from the Kafka topic and processes them to create shipments.
- **Apache Kafka**: Acts as the event broker between the two services.
- **MongoDB**: Used by both services to store their respective data (orders and shipments).
- **Kafbat UI**: A web-based UI for managing and monitoring Kafka clusters, available at `http://localhost:8080`.

## Technologies Used

- Java 21
- Spring Boot 3
- Apache Kafka
- MongoDB
- Docker and Docker Compose
- Maven

## How to Run

1. **Prerequisites**:
   - Docker and Docker Compose must be installed.
   - Java 21 and Maven must be installed to build the project.

2. **Build the Services**:
   Navigate to the root directory of the project and run the following Maven command to build the services:
   ```bash
   mvn clean install
   ```

3. **Start the Application**:
   In the root directory, run the following command to start all the services using Docker Compose:
   ```bash
   docker-compose up -d
   ```
   This will start the following containers:
   - `kafka`
   - `kafbat-ui`
   - `mongodb`

## How to Use

1. **Create an Order**:
   Send a `POST` request to the `order-service` to create a new order.

   **Endpoint**: `POST /api/orders`
   **Body**:
   ```json
   {
     "item": "Laptop",
     "quantity": 1
   }
   ```

2. **Verify the Order**:
   - The `order-service` will log that an `OrderCreated` event has been published.
   - The `shipping-service` will log that it has received the event and created a shipment.

3. **Monitor with Kafbat UI**:
   - Open your browser and navigate to `http://localhost:8080`.
   - You will see the `local` Kafka cluster and can inspect the `order-events` topic to see the messages.
