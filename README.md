# User task manager

## Overview

This project is a microservice application built with Spring Boot that manages users and tasks. It uses PostgreSQL for data storage and Docker Compose for containerization. Additionally, a React front-end application is provided for interacting with the API.

### Features

- **Spring Boot Microservice**:
    - User management (CRUD operations)
    - Task management (CRUD operations, linked to users)
    - Soft delete for users and tasks

- **PostgreSQL**:
    - Managed via Docker Compose
    - pgAdmin for database management

- **React Front-End**:
    - Displays users and tasks in tables
    - Includes functionality for viewing, editing, and deleting records

### Prerequisites

- Java 21 or later
- Docker and Docker Compose
- Node.js and npm (for the React app)

### API Endpoints

- **User Endpoints:**
    - `POST /users` - Create a new user
    - `GET /users` - Get all users
    - `GET /users/{id}` - Get user by ID
    - `PUT /users/{id}` - Update a user
    - `DELETE /users/{id}` - Soft delete a user

- **Task Endpoints:**
    - `POST /tasks` - Create a new task (associated with a user)
    - `GET /tasks` - Get all tasks
    - `GET /tasks/{id}` - Get task by ID
    - `PUT /tasks/{id}` - Update a task
    - `DELETE /tasks/{id}` - Soft delete a task




