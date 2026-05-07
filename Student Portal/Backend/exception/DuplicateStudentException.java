package com.university.exception;

/** Thrown when a student with the same email or ID already exists. */
public class DuplicateStudentException extends RuntimeException {
    public DuplicateStudentException(String message) {
        super(message);
    }
}
