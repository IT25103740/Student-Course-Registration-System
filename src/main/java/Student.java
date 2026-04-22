package models;

public class Student extends User {

    // Constructor calls the parent (User) constructor using 'super'
    public Student(String userId, String name, String password) {
        super(userId, name, password);
    }

    // Implementing the abstract method from User class
    @Override
    public String getRole() {
        return "Student";
    }
}
