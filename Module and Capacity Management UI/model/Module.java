package model;

public class Module {

    private String id;
    private String name;
    private int capacity;
    private int enrolled;

    // Constructor (IMPORTANT - 4 parameters)
    public Module(String id, String name, int capacity, int enrolled) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.enrolled = enrolled;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getCapacity() { return capacity; }
    public int getEnrolled() { return enrolled; }

    public void setName(String name) { this.name = name; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public void setEnrolled(int enrolled) { this.enrolled = enrolled; }
}