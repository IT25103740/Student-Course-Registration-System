package com.university.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Student entity — encapsulates all student data.
 * Demonstrates OOP: encapsulation, immutable ID, validation in setters.
 */
public class Student {

    // ── Fields ──────────────────────────────────────────────────────────────
    private final String studentId;   // immutable once assigned
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String department;
    private String year;              // "1st", "2nd", "3rd", "4th"
    private double gpa;
    private LocalDate enrollmentDate;
    private StudentStatus status;
    private List<String> enrolledCourses;

    // ── Enum for status ──────────────────────────────────────────────────────
    public enum StudentStatus {
        ACTIVE, INACTIVE, GRADUATED, SUSPENDED
    }

    // ── Constructors ─────────────────────────────────────────────────────────

    /** Full constructor — used when recreating from persistence. */
    public Student(String studentId, String firstName, String lastName,
                   String email, String phone, String department,
                   String year, double gpa, LocalDate enrollmentDate,
                   StudentStatus status) {
        this.studentId      = studentId;
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
        setPhone(phone);
        setDepartment(department);
        setYear(year);
        setGpa(gpa);
        this.enrollmentDate  = enrollmentDate;
        this.status          = status;
        this.enrolledCourses = new ArrayList<>();
    }

    /** Factory — auto-generates a unique ID for new students. */
    public static Student create(String firstName, String lastName,
                                  String email, String phone,
                                  String department, String year, double gpa) {
        String id = "STU-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return new Student(id, firstName, lastName, email, phone,
                           department, year, gpa, LocalDate.now(),
                           StudentStatus.ACTIVE);
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    public String getStudentId()            { return studentId; }
    public String getFirstName()            { return firstName; }
    public String getLastName()             { return lastName; }
    public String getFullName()             { return firstName + " " + lastName; }
    public String getEmail()                { return email; }
    public String getPhone()                { return phone; }
    public String getDepartment()           { return department; }
    public String getYear()                 { return year; }
    public double getGpa()                  { return gpa; }
    public LocalDate getEnrollmentDate()    { return enrollmentDate; }
    public StudentStatus getStatus()        { return status; }
    public List<String> getEnrolledCourses(){ return new ArrayList<>(enrolledCourses); }

    // ── Setters (with validation) ────────────────────────────────────────────

    public void setFirstName(String firstName) {
        if (firstName == null || firstName.isBlank())
            throw new IllegalArgumentException("First name cannot be empty.");
        this.firstName = firstName.trim();
    }

    public void setLastName(String lastName) {
        if (lastName == null || lastName.isBlank())
            throw new IllegalArgumentException("Last name cannot be empty.");
        this.lastName = lastName.trim();
    }

    public void setEmail(String email) {
        if (email == null || !email.matches("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$"))
            throw new IllegalArgumentException("Invalid email address: " + email);
        this.email = email.trim().toLowerCase();
    }

    public void setPhone(String phone) {
        if (phone == null || phone.isBlank())
            throw new IllegalArgumentException("Phone cannot be empty.");
        this.phone = phone.trim();
    }

    public void setDepartment(String department) {
        if (department == null || department.isBlank())
            throw new IllegalArgumentException("Department cannot be empty.");
        this.department = department.trim();
    }

    public void setYear(String year) {
        List<String> valid = List.of("1st", "2nd", "3rd", "4th");
        if (!valid.contains(year))
            throw new IllegalArgumentException("Year must be one of: " + valid);
        this.year = year;
    }

    public void setGpa(double gpa) {
        if (gpa < 0.0 || gpa > 4.0)
            throw new IllegalArgumentException("GPA must be between 0.0 and 4.0.");
        this.gpa = gpa;
    }

    public void setStatus(StudentStatus status) {
        if (status == null) throw new IllegalArgumentException("Status cannot be null.");
        this.status = status;
    }

    // ── Course enrolment helpers ─────────────────────────────────────────────

    public void enrollCourse(String courseCode) {
        if (!enrolledCourses.contains(courseCode))
            enrolledCourses.add(courseCode);
    }

    public void dropCourse(String courseCode) {
        enrolledCourses.remove(courseCode);
    }

    // ── toString ─────────────────────────────────────────────────────────────

    @Override
    public String toString() {
        return String.format("Student{id='%s', name='%s', email='%s', dept='%s', year='%s', gpa=%.2f, status=%s}",
                studentId, getFullName(), email, department, year, gpa, status);
    }
}
