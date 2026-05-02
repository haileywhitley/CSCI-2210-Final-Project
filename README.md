# Clinic Scheduler Application

## Overview

The Clinic Scheduler Application is a Spring Boot-based backend system
designed to manage scheduling operations for a medical clinic. It
supports the creation and management of patients, providers, and
appointments, while enforcing scheduling constraints and preventing
conflicts.

This project demonstrates RESTful API development, layered architecture,
database integration, and structured exception handling.

\---

## Features

### Patient Management

* Create, retrieve, update, and delete patient records

### Provider Management

* Manage healthcare providers and their associated data

### Appointment Scheduling

* Schedule appointments between patients and providers
* Prevent overlapping or conflicting appointments
* Reschedule existing appointments
* View and manage appointment records

### Error Handling

* Custom exceptions for invalid input, scheduling conflicts, and
missing resources
* Centralized exception handling

\---

## Project Structure

&#x20;   src/main/java/finalProject/

    controller/     REST API endpoints
    service/        Business logic
    repository/     Data access layer (JPA)
    model/          Entity classes
    dto/            Data Transfer Objects
    exception/      Custom exceptions and handlers


Main application entry point:

&#x20;   ClinicSchedulerApplication.java


\---

## Technologies Used

* Java
* Spring Boot
* Spring Data JPA
* Maven
* SQLite

\---

## Setup and Installation

### Prerequisites

* Java 17 or later
* Maven

### Steps

1. Clone the repository:

&#x20;   ``` bash
    git clone <repository-url>
    cd CSCI-2210-Final-Project
    ```

2. Build the project:

&#x20;   ``` bash
    mvn clean install
    ```

3. Run the application:

&#x20;   ``` bash
    mvn spring-boot:run
    ```

4. Access the application at:

http://localhost:8080


\---

## API Endpoints

### Patients

* GET /patients --- Retrieve all patients
* GET /patients/{id} --- Retrieve a patient by ID
* POST /patients --- Create a new patient
* PUT /patients/{id} --- Update a patient
* DELETE /patients/{id} --- Delete a patient

### Providers

* GET /providers --- Retrieve all providers
* POST /providers --- Create a new provider

### Appointments

* GET /appointments --- Retrieve all appointments
* POST /appointments --- Schedule a new appointment
* PUT /appointments/reschedule --- Reschedule an appointment

\---

## Business Logic

* Prevents overlapping appointments for providers
* Validates all appointment requests before persistence
* Uses DTOs to separate internal models from API responses
* Centralizes exception handling for consistent error responses

\---

## Testing

Unit tests are included for core service components.

Run tests with:

``` bash
mvn test
```

\---

## Database

The application uses a local SQLite database: clinic.db

Database configuration is defined in application.properties.

\---

## Future Improvements

* Add authentication and authorization
* Improve frontend interface
* Implement pagination and filtering
* Enhance provider availability management
* Deploy to a cloud platform

\---

## Author

Abby Yearout, Hailey Whitley, Anna Zagrai
CSCI-2210

