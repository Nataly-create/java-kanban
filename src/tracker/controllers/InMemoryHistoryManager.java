package tracker.controllers;

import tracker.model.Task;
import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {
    private final HashMap<Integer, Node> historyMap;
    private Node head;
    private Node last;

    public InMemoryHistoryManager() {
        historyMap = new HashMap<>();
        head = null;
        last = null;
    }

    public Node getHead() {
        return head;
    }

    public Node getLast() {
        return last;
    }

    public HashMap<Integer, Node> getHistoryMap() {
        return historyMap;
    }

    public void linkLast(Task item) {
        Node node = new Node(item, last, null);
        if (head == null) {
          head = node;
        } else {
            last.next = node;
        }
        last = node;
        historyMap.put(item.getId(), node);
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasksList= new ArrayList<>();
        Node current = head;
        while(current != null) {
            tasksList.add(current.item);
            current = current.next;
        }
        return tasksList;
    }

    void removeNode(Node node) {
        if ((node != last) && (node != head)) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        } else if (node == head) {
            head = node.next;
            if (head != null) {
                head.prev = null;
            }
        } else {
            last = node.prev;
            last.next = null;
        }
        historyMap.remove(node.item.getId());
    }


    @Override
    public void add(Task task) {
        Node node = historyMap.get(task.getId());
        if (node != null) {
            removeNode(node);
        }
        linkLast(task);
    }

    @Override
    public void remove(int id) {
        historyMap.remove(id);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return getTasks();
    }
}
