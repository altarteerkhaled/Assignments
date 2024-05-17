# FX Deals Task

This project is a Spring Boot application designed to handle FX (foreign exchange) deal processing. It accepts deal details via a REST API, validates the incoming data, and persists it to a MySQL database. The application ensures no duplicate deals are imported, and implements various validations and exception handling mechanisms to maintain data integrity.

## Table of Contents

- [FX Deals Task](#fx-deals-task)
    - [Features](#features)
    - [Technologies Used](#technologies-used)
    - [Setup and Installation](#setup-and-installation)
    - [Running the Application](#running-the-application)
    - [Docker Setup](#docker-setup)
    - [License](#license)


## Features

- Accepts FX deal details through a REST API.
- Validates the deal details to ensure data integrity.
- Prevents duplicate deals from being imported.
- Implements proper exception handling for various error scenarios.
- Logs important events and errors for monitoring and debugging.

## Technologies Used

- Java
- Spring Boot
- Hibernate (JPA)
- MySQL
- Docker
- Maven
- JUnit and Mockito for testing

## Setup and Installation

1. **Clone the repository**:

    ```sh
    git clone https://github.com/yourusername/fx-deals-task.git
    cd fx-deals-task
    ```

2. **Build the project**:

   Make sure you have Maven installed. Run the following command to build the project:

    ```sh
    mvn clean install
    ```

3. **Set up the database**:

   The application uses MySQL. Ensure you have MySQL installed and running. The database configuration is specified in the `application.properties` file:

    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/fx_deals?createDatabaseIfNotExist=true
    spring.datasource.username=root
    spring.datasource.password=yourpassword
    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
    ```

   Adjust the configuration as needed for your environment.

## Running the Application

To run the application locally, use the following command:

```sh
mvn spring-boot:run
```
## Docker setup
   The project includes a Dockerfile and docker-compose.yml to run the application and MySQL database in Docker containers.

```sh
docker-compose up --build
```

## License

This `README.md` file covers the essential aspects of your project, including setup, running instructions, Docker setup. Adjust the content as needed to fit your specific project details and requirements.
