package tracker.controllers;

import org.junit.jupiter.api.Test;
import tracker.model.Task;
import static org.junit.jupiter.api.Assertions.*;

public class NodeTest {

    @Test
    void Node() {
        Task task = new Task("Test", "Test");
        Task taskPrev = new Task("Tesk prev", "Task prev");
        Task taskNext = new Task("Task next", "Task next");
        Node nodePrev = new Node(taskPrev, null, null);
        Node nodeNext = new Node(taskNext, null, null);
        Node node = new Node(task, nodePrev, nodeNext);

        assertNotNull(node, "node not found.");
        assertNotNull(node.prev, "node.prev not found.");
        assertNotNull(node.next, "node.next found.");

        assertEquals(nodeNext, node.next, "node.next are different.");
        assertEquals(nodePrev, node.prev, "node.prev are different.");
    }
}

