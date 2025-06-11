package tracker.controllers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tracker.model.Task;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest {
    private static InMemoryHistoryManager inMemoryHistoryManager;
    private static Task task;

    @BeforeAll
    static void createBeforeAllTests() {
        inMemoryHistoryManager = new InMemoryHistoryManager();
        task = new Task("Title task test", "Description task test");
    }

    @Test
    void shouldNotBeNullAfterLinkLast() {
        inMemoryHistoryManager.linkLast(task);
        assertNotNull(inMemoryHistoryManager.getHead(), "Head not found.");
        assertNotNull(inMemoryHistoryManager.getLast(), "Last not found.");
    }

    @Test
    void shouldBeEmptyAfterRemoveNode() {
        inMemoryHistoryManager.linkLast(task);
        HashMap<Integer, Node> historyMap = inMemoryHistoryManager.getHistoryMap();
        Node nodeHead = inMemoryHistoryManager.getHead();
        inMemoryHistoryManager.removeNode(nodeHead);
        assertEquals(0,  historyMap.size(), "HistoryMap is not empty.");
    }

}
