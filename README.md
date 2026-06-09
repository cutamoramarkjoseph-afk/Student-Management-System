# Student Record Management System

## Overview

The Student Record Management System is a JavaFX desktop application developed using Java, JavaFX, JDBC, and SQLite. The system allows users to manage student records through basic CRUD (Create, Read, Update, Delete) operations in a simple graphical user interface.

This project was created as a learning project for database connectivity and JavaFX application development.

---

## Features

* Add student records
* Update existing student records
* Delete student records
* Display student information in a table
* Local SQLite database storage
* Simple and responsive JavaFX interface

---

## Technologies Used

* Java 21
* JavaFX 21
* JDBC
* SQLite
* Maven
* IntelliJ IDEA

---

## Database

The application uses SQLite as its local database.

Database file:

```plaintext
students.db
```

Table used:

```sql
CREATE TABLE students (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    course TEXT NOT NULL,
    year_level TEXT NOT NULL
);
```

---

## How to Run the Project

### 1. Clone or Download the Project

```bash
git clone <repository-link>
```

### 2. Open in IntelliJ IDEA

* Open IntelliJ IDEA
* Select "Open Project"
* Choose the project folder

### 3. Install Dependencies

Make sure Maven dependencies are downloaded automatically.

### 4. Add SQLite JDBC Driver

Add the SQLite JDBC library to the project dependencies if not yet included.

### 5. Run the Application

Run:

```plaintext
MainApp.java
```

---

## User Interface

The application contains:

* Student input form
* Year level selection
* CRUD operation buttons
* Student records table

---

## Future Improvements

* Add search functionality
* Add student validation improvements
* Improve user interface design
* Export records to PDF or Excel
* Add login authentication
