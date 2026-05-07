package com.university.exception;

/** Thrown when a student lookup returns no result. */
public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(String message) {
        super(message);
    }
}
