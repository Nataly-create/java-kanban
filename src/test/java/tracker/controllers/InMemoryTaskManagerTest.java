package tracker.controllers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class InMemoryTaskManagerTest {
    private static InMemoryTaskManager inMemoryTaskManager;

    @BeforeAll
    static void createBeforeAllTests() {
        inMemoryTaskManager = (InMemoryTaskManager) Managers.getDefault();
    }

    @Test
    void addTask() {
        Task task = new Task("Test title 1", "Test description 1");
        inMemoryTaskManager.addTask(task);

        Epic epic = new Epic("Test title 2", "Test description 2");
        inMemoryTaskManager.addTask(epic);

        Subtask subtask = new Subtask("Test title 3", "Test description 3", epic);
        inMemoryTaskManager.addTask(subtask);

        final int idTask = task.getId();
        final int idEpic = epic.getId();
        final int idSubtask = subtask.getId();

        final Task savedTask = inMemoryTaskManager.getById(idTask);
        final Task savedEpic = inMemoryTaskManager.getById(idEpic);
        final Task savedSubtask = inMemoryTaskManager.getById(idSubtask);

        assertNotNull(savedTask, "Task not found.");
        assertNotNull(savedEpic, "Epic not found.");
        assertNotNull(savedSubtask, "Subtask not found.");

        assertEquals(task, savedTask, "Tasks are different.");
        assertEquals(epic, savedEpic, "Epics are different.");
        assertEquals(subtask, savedSubtask, "Subtasks are different.");

        final List<Task> tasks = inMemoryTaskManager.getTasks();
        final List<Epic> epics = inMemoryTaskManager.getEpics();
        final List<Subtask> subtasks = inMemoryTaskManager.getSubtasks();

        assertNotNull(tasks, "No tasks are returned.");
        assertNotNull(epics, "No epics are returned.");
        assertNotNull(subtasks, "No subtasks are returned.");


        assertEquals(1, tasks.size(), "Incorrect tasks count.");
        assertEquals(1, epics.size(), "Incorrect epics count.");
        assertEquals(1, subtasks.size(), "Incorrect subtasks count.");

        assertEquals(task, tasks.get(0), "Tasks are different.");
        assertEquals(epic, epics.get(0), "Epics are different.");
        assertEquals(subtask, subtasks.get(0), "Subtasks are different.");
    }

    @Test
    void shouldBeUnchangedAfterAdd() {
        Task task = new Task("Test title 1", "Test description 1");
        inMemoryTaskManager.addTask(task);
        String title = task.getTitle();
        String description = task.getDescription();
        int id = task.getId();

        assertEquals(title, task.getTitle(), "Tasks title ist different.");
        assertEquals(description, task.getDescription(), "Tasks description ist different.");
        assertEquals(id, task.getId(), "Tasks id ist different.");
    }
}