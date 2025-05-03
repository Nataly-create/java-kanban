package tracker.controllers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tracker.model.Task;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {
    private static HistoryManager historyManager;
    private static InMemoryTaskManager inMemoryTaskManager;

    @BeforeAll
    static void createBeforeAllTests() {
        historyManager =  Managers.getDefaultHistory();
        inMemoryTaskManager = (InMemoryTaskManager) Managers.getDefault();
    }

    @Test
    void add() {
        Task task = new Task("Test title", "Test description");
        inMemoryTaskManager.addTask(task);

        final ArrayList<Task> history = historyManager.getHistory();
        historyManager.add(task);

        assertNotNull(history, "History cannot be empty after task creation.");
        assertEquals(1, history.size(), "History cannot be empty after task creation.");
    }

    @Test
    void shouldBeUnchangedAfterAdd() {
        Task task = new Task("Test title", "Test description");

        String title = task.getTitle();
        String description = task.getDescription();
        int id = task.getId();

        historyManager.add(task);
        assertEquals(title, task.getTitle(), "Tasks title ist different.");
        assertEquals(description, task.getDescription(), "Tasks description ist different.");
        assertEquals(id, task.getId(), "Tasks id ist different.");
    }
}