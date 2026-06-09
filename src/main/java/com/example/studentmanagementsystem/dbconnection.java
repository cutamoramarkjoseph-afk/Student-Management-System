package com.example.studentmanagementsystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class dbconnection {

    public static Connection connect() {

        Connection conn = null;

        try {

            Class.forName("org.sqlite.JDBC");

            conn = DriverManager.getConnection(
                    "jdbc:sqlite:students.db"
            );

            Statement stmt = conn.createStatement();

            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS students (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "name TEXT," +
                            "course TEXT," +
                            "year_level TEXT" +
                            ")"
            );

            System.out.println("Connected to SQLite");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return conn;
    }
}