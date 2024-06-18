# Spring Boot Application

This is a Spring Boot application for managing event bookings. Below are the instructions for building, running, and testing the application, both locally and using Docker.

## Prerequisites

- Java 17 or later
- Maven 3.6.3 or later
- Docker or Docker Desktop

## Build Instructions

### Building the Application

To build the application, run the following command:

```sh
mvn clean package
````
This will generate a JAR file in the target directory.

## Run Instructions
### Running the Application Locally
To run the application locally, execute the following command:

```sh
java -jar target/booking-1.0.0.jar
````
The application will start and be accessible at http://localhost:8088.

## Running the Application with Docker
### Step 1: Build Docker Image
To build the Docker image for the application, run the following command:

```sh
docker build -t booking-app/booking-app:1.0.0 .
````
## Step 2: Start Docker Containers
To start the Docker containers (including the application and the database), run the following command:

````sh
docker-compose up -d
````
The application will start and be accessible at http://localhost:8088.

## Step 3: Stop Docker Containers
To stop the running Docker containers, run the following command:

````sh
docker-compose down
````

## Test Instructions
Running Unit Tests
To run the unit tests, use the following command:

````sh
mvn test
````
# Additional Information
Application URL: http://localhost:8088

## Swagger UI Test Instruction

- Open computer browser
- Paste this url link http://localhost:8088/swagger-ui/index.html#/
- On the web page, @ user-Controller, click on api/v1/users
- Click on "try it out" and click the Execute button
- Copy the access_token generated in the response section.
- Use the copied token to access other endpoints via
- clicking on the padlock icon and pasting the copied token.
- Click Authorize and close.
- You are now authenticated and can visit endpoints that requires authentication.