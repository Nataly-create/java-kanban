package tracker.controllers;
import tracker.model.Task;

public class Node {
    Task item;
    Node prev;
    Node next;

    public Node(Task item, Node prev, Node next) {
        this.item = item;
        this.next = next;
        this.prev = prev;
    }
}
