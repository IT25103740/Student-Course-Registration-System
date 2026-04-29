package model;

import queue.WaitlistQueue;

public class Module {

    private String name;
    private int capacity;
    private int enrolled;

    private WaitlistQueue queue = new WaitlistQueue();

    public Module(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
        this.enrolled = 0;
    }

    public void register(Student s) {
        if (enrolled < capacity) {
            enrolled++;
            System.out.println(s.name + " ENROLLED");
        } else {
            queue.enqueue(s);
            System.out.println(s.name + " WAITLISTED");
        }
    }

    public void addSeats(int seats) {
        capacity += seats;

        while (enrolled < capacity && !queue.isEmpty()) {
            Student s = queue.dequeue();
            enrolled++;
            System.out.println(s.name + " MOVED FROM WAITLIST");
        }
    }

    public String getName() { return name; }
    public int getCapacity() { return capacity; }
    public int getEnrolled() { return enrolled; }
    public int getWaitlistCount() { return queue.size(); }
}