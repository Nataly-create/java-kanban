package tracker.model;

import org.junit.jupiter.api.Test;
import tracker.controllers.InMemoryTaskManager;
import tracker.controllers.Managers;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void shouldReturnTrueWhenIdsAreEqual() {
        Task task1 = new Task("Test title 1", "Test description 1");
        Task task2 = new Task("Test title 2", "Test description 2");

        InMemoryTaskManager inMemoryTaskManager = (InMemoryTaskManager) Managers.getDefault();
        inMemoryTaskManager.addTask(task1);
        task2.setId(1);
        assertEquals(task1, task2, "Tasks with same id should be equal.");
    }
}