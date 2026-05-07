package com.university.repository;

import com.university.model.Student;
import java.util.List;
import java.util.Optional;

/**
 * Repository abstraction — OOP Interface / Dependency Inversion.
 * The service layer depends on this interface, not on a concrete implementation,
 * making it easy to swap the data store (in-memory → database) without
 * changing business logic.
 */
public interface StudentRepository {

    /** Persist a new student. */
    Student save(Student student);

    /** Find by primary key. */
    Optional<Student> findById(String studentId);

    /** Find by email (unique). */
    Optional<Student> findByEmail(String email);

    /** Return all students. */
    List<Student> findAll();

    /** Search by name fragment (case-insensitive). */
    List<Student> searchByName(String query);

    /** Search by department. */
    List<Student> findByDepartment(String department);

    /** Replace the stored record with the supplied student object. */
    Student update(Student student);

    /** Remove a student from the store. */
    boolean deleteById(String studentId);

    /** How many students are stored. */
    long count();
}
