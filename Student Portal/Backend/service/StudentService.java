package com.university.service;

import com.university.exception.StudentNotFoundException;
import com.university.model.Student;
import com.university.repository.StudentRepository;

import java.util.List;
import java.util.Map;

/**
 * Service layer — contains all business logic.
 *
 * OOP: Dependency Injection, Single Responsibility, Encapsulation.
 */
public class StudentService {

    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    // ── Expose repository for DB viewer ──────────────────────────────────────
    public StudentRepository getRepository() { return repository; }

    // ── CREATE ────────────────────────────────────────────────────────────────
    public Student createStudent(String firstName, String lastName, String email,
                                  String phone, String department, String year, double gpa) {
        Student student = Student.create(firstName, lastName, email, phone, department, year, gpa);
        return repository.save(student);
    }

    // ── READ ──────────────────────────────────────────────────────────────────
    public Student getStudentById(String studentId) {
        return repository.findById(studentId)
            .orElseThrow(() -> new StudentNotFoundException("No student found with ID: " + studentId));
    }

    public List<Student> getAllStudents()                       { return repository.findAll(); }
    public List<Student> searchStudents(String query)          { return repository.searchByName(query); }
    public List<Student> getStudentsByDepartment(String dept)  { return repository.findByDepartment(dept); }

    // ── UPDATE ────────────────────────────────────────────────────────────────
    public Student updateStudent(String studentId, Map<String, String> fields) {
        Student student = getStudentById(studentId);
        if (fields.containsKey("firstName"))  student.setFirstName(fields.get("firstName"));
        if (fields.containsKey("lastName"))   student.setLastName(fields.get("lastName"));
        if (fields.containsKey("email"))      student.setEmail(fields.get("email"));
        if (fields.containsKey("phone"))      student.setPhone(fields.get("phone"));
        if (fields.containsKey("department")) student.setDepartment(fields.get("department"));
        if (fields.containsKey("year"))       student.setYear(fields.get("year"));
        if (fields.containsKey("gpa"))        student.setGpa(Double.parseDouble(fields.get("gpa")));
        if (fields.containsKey("status"))
            student.setStatus(Student.StudentStatus.valueOf(fields.get("status").toUpperCase()));
        return repository.update(student);
    }

    // ── DELETE ────────────────────────────────────────────────────────────────
    public void deleteStudent(String studentId) {
        if (!repository.deleteById(studentId))
            throw new StudentNotFoundException("Cannot delete — student not found: " + studentId);
    }

    // ── STATS ─────────────────────────────────────────────────────────────────
    public Map<String, Object> getDashboardStats() {
        List<Student> all = repository.findAll();
        long active    = all.stream().filter(s -> s.getStatus() == Student.StudentStatus.ACTIVE).count();
        long inactive  = all.stream().filter(s -> s.getStatus() == Student.StudentStatus.INACTIVE).count();
        long graduated = all.stream().filter(s -> s.getStatus() == Student.StudentStatus.GRADUATED).count();
        double avgGpa  = all.stream().mapToDouble(Student::getGpa).average().orElse(0.0);
        return Map.of("total", all.size(), "active", active, "inactive", inactive,
                      "graduated", graduated, "avgGpa", String.format("%.2f", avgGpa));
    }
}
