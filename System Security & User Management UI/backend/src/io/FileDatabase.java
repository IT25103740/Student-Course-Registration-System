package io;

import models.User;
import models.AuthLog;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileDatabase {
    private static final String DATA_DIR = "data";
    private static final String USERS_FILE = DATA_DIR + "/users.txt";
    private static final String LOGS_FILE = DATA_DIR + "/logs.txt";

    static {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            File users = new File(USERS_FILE);
            if (users.createNewFile()) {
                // Create a default admin if new file
                User admin = new User(UUID.randomUUID().toString(), "admin", "admin123", "ADMIN", true);
                saveUsers(new ArrayList<>(List.of(admin)));
            }
            new File(LOGS_FILE).createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                User user = User.fromCsvString(line);
                if (user != null) users.add(user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static void saveUsers(List<User> users) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(USERS_FILE))) {
            for (User user : users) {
                bw.write(user.toCsvString());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<AuthLog> loadLogs() {
        List<AuthLog> logs = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(LOGS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                AuthLog log = AuthLog.fromCsvString(line);
                if (log != null) logs.add(log);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return logs;
    }

    public static void addLog(AuthLog log) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(LOGS_FILE, true))) {
            bw.write(log.toCsvString());
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void clearLogs() {
        try (FileWriter fw = new FileWriter(LOGS_FILE, false)) {
            fw.write(""); // Empty the file
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
