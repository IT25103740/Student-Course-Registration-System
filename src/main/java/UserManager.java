package models;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserManager {
    // Text file where user data will be stored
    private static final String FILE_NAME = "users.txt";

    // --- 1. CREATE (Register User) ---
    public void registerUser(User user) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            // Format: ID,Name,Password,Role
            writer.write(user.getUserId() + "," + user.getName() + "," + user.getPassword() + "," + user.getRole());
            writer.newLine();
            System.out.println("Success: User " + user.getUserId() + " registered.");
        } catch (IOException e) {
            System.err.println("Error saving user: " + e.getMessage());
        }
    }

    // --- 2. READ (View Users) ---
    public List<String> viewAllUsers() {
        List<String> userList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length == 4) {
                    // Hide password for security. Only show ID, Name, Role
                    userList.add("ID: " + details[0] + " | Name: " + details[1] + " | Role: " + details[3]);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No users found. File does not exist yet.");
        } catch (IOException e) {
            System.err.println("Error reading users: " + e.getMessage());
        }
        return userList;
    }

    // --- READ (Authenticate Login) ---
    // Added this specifically for your system login feature!
    public User authenticateUser(String loginId, String loginPassword) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length == 4) {
                    String id = details[0];
                    String name = details[1];
                    String pass = details[2];
                    String role = details[3];

                    // Check if credentials match
                    if (id.equals(loginId) && pass.equals(loginPassword)) {
                        if (role.equals("Student")) {
                            return new Student(id, name, pass);
                        } else if (role.equals("Admin")) {
                            return new Admin(id, name, pass);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading users: " + e.getMessage());
        }
        return null; // Return null if login fails
    }

    // --- 3. UPDATE (Change Password) ---
    public boolean updatePassword(String userId, String newPassword) {
        File file = new File(FILE_NAME);
        File tempFile = new File("temp_users.txt");
        boolean isUpdated = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length == 4 && details[0].equals(userId)) {
                    // Replace old password with new password
                    line = details[0] + "," + details[1] + "," + newPassword + "," + details[3];
                    isUpdated = true;
                }
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error updating password: " + e.getMessage());
            return false;
        }

        if (isUpdated) {
            file.delete();
            tempFile.renameTo(file);
            System.out.println("Success: Password updated for " + userId);
            return true;
        } else {
            tempFile.delete();
            System.out.println("User ID not found.");
            return false;
        }
    }

    // --- 4. DELETE (Remove User) ---
    public boolean deleteUser(String userId) {
        File file = new File(FILE_NAME);
        File tempFile = new File("temp_users.txt");
        boolean isDeleted = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                // If ID matches, DO NOT write to new file (this deletes them)
                if (details.length > 0 && !details[0].equals(userId)) {
                    writer.write(line);
                    writer.newLine();
                } else {
                    isDeleted = true; // Found and skipped
                }
            }
        } catch (IOException e) {
            System.err.println("Error deleting user: " + e.getMessage());
            return false;
        }

        if (isDeleted) {
            file.delete();
            tempFile.renameTo(file);
            System.out.println("Success: User " + userId + " deleted.");
            return true;
        } else {
            tempFile.delete();
            System.out.println("User ID not found.");
            return false;
        }
    }
}