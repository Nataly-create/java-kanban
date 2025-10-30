package tracker.controllers;

import org.junit.jupiter.api.Test;
import tracker.model.Task;
import static org.junit.jupiter.api.Assertions.*;

public class NodeTest {

    @Test
    void node() {
        Task task = new Task("Test", "Test");
        Task taskPrev = new Task("Tesk prev", "Task prev");
        Task taskNext = new Task("Task next", "Task next");
        InMemoryHistoryManager.Node nodePrev = new InMemoryHistoryManager.Node(taskPrev, null, null);
        InMemoryHistoryManager.Node nodeNext = new InMemoryHistoryManager.Node(taskNext, null, null);
        InMemoryHistoryManager.Node node = new InMemoryHistoryManager.Node(task, nodePrev, nodeNext);

        assertNotNull(node, "node not found.");
        assertNotNull(node.prev, "node.prev not found.");
        assertNotNull(node.next, "node.next found.");

        assertEquals(nodeNext, node.next, "node.next are different.");
        assertEquals(nodePrev, node.prev, "node.prev are different.");
    }
}

