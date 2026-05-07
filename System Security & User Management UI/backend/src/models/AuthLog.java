package models;

public class AuthLog {
    private String id;
    private String username;
    private String action;
    private String timestamp;

    public AuthLog(String id, String username, String action, String timestamp) {
        this.id = id;
        this.username = username;
        this.action = action;
        this.timestamp = timestamp;
    }

    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getAction() { return action; }
    public String getTimestamp() { return timestamp; }

    public String toCsvString() {
        return id + "," + username + "," + action + "," + timestamp;
    }

    public static AuthLog fromCsvString(String csv) {
        String[] parts = csv.split(",");
        if (parts.length == 4) {
            return new AuthLog(parts[0], parts[1], parts[2], parts[3]);
        }
        return null;
    }

    public String toJson() {
        return "{\"id\":\"" + id + "\", \"username\":\"" + username + "\", \"action\":\"" + action + "\", \"timestamp\":\"" + timestamp + "\"}";
    }
}
