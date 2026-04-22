package models;

public abstract class User {
    private String userId;
    private String name;
    private String password;

    // Constructor
    public User(String userId, String name, String password) {
        this.userId = userId;
        this.name = name;
        this.password = password;
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Abstract method for Polymorphism - Child classes must implement this
    public abstract String getRole();
}