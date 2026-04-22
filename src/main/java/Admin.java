package models;

public class Admin extends User {

    // Constructor calls the parent (User) constructor
    public Admin(String userId, String name, String password) {
        super(userId, name, password);
    }

    // Implementing the abstract method from User class
    @Override
    public String getRole() {
        return "Admin";
    }
}