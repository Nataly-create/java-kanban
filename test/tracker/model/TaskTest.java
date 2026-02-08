package tracker.model;

import org.junit.jupiter.api.Test;
import tracker.controllers.InMemoryTaskManager;
import tracker.controllers.Managers;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void shouldReturnTrueWhenIdsAreEqual() {
        Task task1 = new Task("Test title 1", "Test description 1");
        Task task2 = new Task("Test title 2", "Test description 2");

        InMemoryTaskManager inMemoryTaskManager = (InMemoryTaskManager) Managers.getDefault();
        inMemoryTaskManager.addTask(task1);
        task2.setId(task1.getId());
        assertEquals(task1, task2, "Tasks with same id should be equal.");
    }

    @Test
    void shouldIntersectBe() {
        InMemoryTaskManager inMemoryTaskManager = (InMemoryTaskManager) Managers.getDefault();
        Task task1 = new Task("Task 1", "Do task 1", Duration.ofMinutes(60), LocalDateTime.now().plusMinutes(20));
        inMemoryTaskManager.addTask(task1);

        Task task2 = new Task("Task 2", "Do task 2", Duration.ofMinutes(140), LocalDateTime.now().minusMinutes(20));
        inMemoryTaskManager.addTask(task2);

        assertEquals(true, task1.isIntersect(task2), "Tasks should be intersect.");
    }
}