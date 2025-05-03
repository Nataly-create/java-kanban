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
        InMemoryTaskManager inMemoryTaskManager = (InMemoryTaskManager) Managers.getDefault();
        inMemoryTaskManager.addTask(epic1);

        epic2.setId(1);
        assertEquals(epic1, epic2, "Epics with same id should be equal.");
    }
}