package tracker.controllers;

import org.junit.jupiter.api.Test;
import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    void shouldTaskManagersListsBeInitialized() {
        TaskManager taskManager = Managers.getDefault();
        ArrayList<Task> tasks = taskManager.getTasks();
        ArrayList<Subtask> subtasks = taskManager.getSubtasks();
        ArrayList<Epic> epics = taskManager.getEpics();

        assertNotNull(tasks, "The list ´tasks´ is not initialized.");
        assertNotNull(subtasks, "The list ´subtasks´ is not initialized.");
        assertNotNull(epics, "The list ´epics´ is not initialized.");
    }

    @Test
    void shouldHistoryListBeInitialized() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        ArrayList<Task> history = historyManager.getHistory();
        assertNotNull(history, "The list ´history´ is not initialized.");
    }
}