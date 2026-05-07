package com.university.repository;

import com.university.model.Student;
import com.university.exception.DuplicateStudentException;
import com.university.exception.StudentNotFoundException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * In-memory implementation of StudentRepository.
 *
 * OOP concepts demonstrated:
 *  - Implements interface (polymorphism)
 *  - Uses ConcurrentHashMap for thread-safety
 *  - Stream-based querying
 */
public class InMemoryStudentRepository implements StudentRepository {

    // Primary store: studentId → Student
    private final Map<String, Student> store = new ConcurrentHashMap<>();

    // Secondary index: email → studentId  (enforces uniqueness)
    private final Map<String, String> emailIndex = new ConcurrentHashMap<>();

    // ── Seed data ────────────────────────────────────────────────────────────

    public InMemoryStudentRepository() {
        seedData();
    }

    private void seedData() {
        List<Student> seeds = List.of(
            Student.create("Amal",   "Perera",    "amal.perera@uni.lk",   "+94771234567", "Computer Science", "2nd", 3.75),
            Student.create("Nimal",  "Silva",     "nimal.silva@uni.lk",   "+94772345678", "Engineering",      "3rd", 3.20),
            Student.create("Kamali", "Fernando",  "kamali.f@uni.lk",      "+94773456789", "Business",         "1st", 3.90),
            Student.create("Ruwan",  "Jayawardena","ruwan.j@uni.lk",      "+94774567890", "Medicine",         "4th", 3.55),
            Student.create("Dilani", "Wijesinghe","dilani.w@uni.lk",      "+94775678901", "Computer Science", "1st", 3.10)
        );
        seeds.forEach(this::save);
    }

    // ── CRUD ─────────────────────────────────────────────────────────────────

    @Override
    public Student save(Student student) {
        if (emailIndex.containsKey(student.getEmail())) {
            throw new DuplicateStudentException(
                "A student with email '" + student.getEmail() + "' already exists.");
        }
        store.put(student.getStudentId(), student);
        emailIndex.put(student.getEmail(), student.getStudentId());
        return student;
    }

    @Override
    public Optional<Student> findById(String studentId) {
        return Optional.ofNullable(store.get(studentId));
    }

    @Override
    public Optional<Student> findByEmail(String email) {
        String id = emailIndex.get(email.toLowerCase());
        return id == null ? Optional.empty() : findById(id);
    }

    @Override
    public List<Student> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public List<Student> searchByName(String query) {
        String q = query.toLowerCase();
        return store.values().stream()
            .filter(s -> s.getFullName().toLowerCase().contains(q))
            .collect(Collectors.toList());
    }

    @Override
    public List<Student> findByDepartment(String department) {
        return store.values().stream()
            .filter(s -> s.getDepartment().equalsIgnoreCase(department))
            .collect(Collectors.toList());
    }

    @Override
    public Student update(Student student) {
        Student existing = store.get(student.getStudentId());
        if (existing == null) {
            throw new StudentNotFoundException("Student not found: " + student.getStudentId());
        }
        // If email changed, update index
        if (!existing.getEmail().equals(student.getEmail())) {
            if (emailIndex.containsKey(student.getEmail())) {
                throw new DuplicateStudentException(
                    "Email '" + student.getEmail() + "' is already in use.");
            }
            emailIndex.remove(existing.getEmail());
            emailIndex.put(student.getEmail(), student.getStudentId());
        }
        store.put(student.getStudentId(), student);
        return student;
    }

    @Override
    public boolean deleteById(String studentId) {
        Student removed = store.remove(studentId);
        if (removed != null) {
            emailIndex.remove(removed.getEmail());
            return true;
        }
        return false;
    }

    @Override
    public long count() {
        return store.size();
    }
}
