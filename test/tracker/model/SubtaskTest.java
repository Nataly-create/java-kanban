package tracker.model;

import org.junit.jupiter.api.Test;
import tracker.controllers.InMemoryTaskManager;
import tracker.controllers.Managers;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {

    @Test
    void shouldReturnTrueWhenIdsAreEqual() {
        Epic epic = new Epic("Test title epic", "Test description epic");
        Subtask subtask1 = new Subtask("Test title 1", "Test description 1", epic);
        Subtask subtask2 = new Subtask("Test title 2", "Test description 2", epic);

        InMemoryTaskManager inMemoryTaskManager = (InMemoryTaskManager) Managers.getDefault();
        inMemoryTaskManager.addTask(subtask1);
        subtask2.setId(subtask1.getId());
        assertEquals(subtask1, subtask2, "Subtasks with same id should be equal.");
    }
}