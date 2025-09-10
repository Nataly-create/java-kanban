package tracker.model;

import org.junit.jupiter.api.Test;
import tracker.controllers.InMemoryTaskManager;
import tracker.controllers.Managers;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    @Test
    void shouldReturnTrueWhenIdsAreEqual() {
        Epic epic1 = new Epic("Test title 1", "Test description 1");
        Epic epic2 = new Epic("Test title 2", "Test description 2");
        InMemoryTaskManager inMemoryTaskManagerTest = (InMemoryTaskManager) Managers.getDefault();
        inMemoryTaskManagerTest.addTask(epic1);

        epic2.setId(epic1.getId());
        assertEquals(epic1, epic2, "Epics with same id should be equal.");
    }

    @Test
    void shouldStatusInProgressBe() {
        Epic epic1 = new Epic("Test title 1", "Test description 1");
        Subtask subtask1 = new Subtask("Test title 2", "Test description 2", epic1);
        subtask1.setStatus(Status.IN_PROGRESS);
        Subtask subtask2 = new Subtask("Test title 3", "Test description 3", epic1);
        subtask2.setStatus(Status.IN_PROGRESS);

        InMemoryTaskManager inMemoryTaskManagerTest = (InMemoryTaskManager) Managers.getDefault();
        inMemoryTaskManagerTest.addTask(epic1);
        inMemoryTaskManagerTest.addTask(subtask1);
        inMemoryTaskManagerTest.addTask(subtask2);

        epic1.setId(epic1.getId());
        assertEquals(epic1.getStatus(), Status.IN_PROGRESS , "Epics status should be In Progress.");
    }

}