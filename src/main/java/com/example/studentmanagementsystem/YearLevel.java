package com.example.studentmanagementsystem;

public enum YearLevel {
    FIRST_YEAR,
    SECOND_YEAR,
    THIRD_YEAR,
    FOURTH_YEAR;

    @Override
    public String toString() {
        return switch (this) {
            case FIRST_YEAR -> "1st Year";
            case SECOND_YEAR -> "2nd Year";
            case THIRD_YEAR -> "3rd Year";
            case FOURTH_YEAR -> "4th Year";
        };
    }
}