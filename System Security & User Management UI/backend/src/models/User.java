package models;

public class User {
    private String id;
    private String username;
    private String password;
    private String role; // "STUDENT" or "ADMIN"
    private boolean active;

    public User(String id, String username, String password, String role, boolean active) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.active = active;
    }

    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public String toCsvString() {
        return id + "," + username + "," + password + "," + role + "," + active;
    }

    public static User fromCsvString(String csv) {
        String[] parts = csv.split(",");
        if (parts.length == 5) {
            return new User(parts[0], parts[1], parts[2], parts[3], Boolean.parseBoolean(parts[4]));
        }
        return null;
    }
    
    public String toJson() {
        return "{\"id\":\"" + id + "\", \"username\":\"" + username + "\", \"role\":\"" + role + "\", \"active\":" + active + "}";
    }
}
