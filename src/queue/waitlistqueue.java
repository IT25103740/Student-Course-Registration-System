package queue;

import model.Student;

class Node {
    Student data;
    Node next;

    Node(Student s) {
        data = s;
        next = null;
    }
}

public class WaitlistQueue {

    private Node front, rear;

    public void enqueue(Student s) {
        Node n = new Node(s);

        if (rear == null) {
            front = rear = n;
            return;
        }

        rear.next = n;
        rear = n;
    }

    public Student dequeue() {
        if (front == null) return null;

        Student s = front.data;
        front = front.next;

        if (front == null) rear = null;

        return s;
    }

    public boolean isEmpty() {
        return front == null;
    }

    public int size() {
        int count = 0;
        Node temp = front;

        while (temp != null) {
            count++;
            temp = temp.next;
        }
        return count;
    }
}