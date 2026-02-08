package tracker.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tracker.model.Epic;
import tracker.model.Status;
import tracker.model.Subtask;
import tracker.model.Task;
import tracker.exceptions.ManagerSaveException;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTaskManagerTest {
    private static FileBackedTaskManager fileBackedTaskManager;
    private static File file;

    @BeforeEach
     void createBeforeEachTests() {
        try {
            file = File.createTempFile("temp_", ".csv");
            fileBackedTaskManager = Managers.getDefaultFileBacked(file);
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }

    @Test
    void save() {
        fileBackedTaskManager.save();
        FileBackedTaskManager fileBackedTaskManagerFile =  FileBackedTaskManager.loadFromFile(file);
        assertEquals(0,  fileBackedTaskManagerFile.getTasks().size(), "Tasks are not empty.");
    }

    @Test
    void shouldHaveTasksAfterLoad() {
        Task task1 = new Task("Task 1", "Do task 1");
        fileBackedTaskManager.addTask(task1);
        Epic epic1 = new Epic("Epic 1", "Do epic 1");
        fileBackedTaskManager.addTask(epic1);
        Subtask subtask1 = new Subtask("Subtask 1", "Do subtask 1", epic1);
        subtask1.setStatus(Status.DONE);
        fileBackedTaskManager.addTask(subtask1);

        fileBackedTaskManager.save();
        FileBackedTaskManager fileBackedTaskManagerFile =  FileBackedTaskManager.loadFromFile(file);

        assertEquals(1,  fileBackedTaskManagerFile.getTasks().size(), "Does not contain one task.");
        assertEquals(1,  fileBackedTaskManagerFile.getSubtasks().size(), "Does not contain one subtask.");
        assertEquals(1,  fileBackedTaskManagerFile.getEpics().size(), "Does not contain one epic.");
    }

    @Test
    void isCorrectID(){
        Task task1 = new Task("Task 1", "Do task 1");
        fileBackedTaskManager.addTask(task1);

        FileBackedTaskManager fileBackedTaskManagerFile =  FileBackedTaskManager.loadFromFile(file);

        assertEquals(1,  fileBackedTaskManagerFile.getTasks().size(), "Does not contain one task.");

        Task task2 = new Task("Task 2", "Do task 2");
        fileBackedTaskManagerFile.addTask(task2);

        assertEquals(2,  fileBackedTaskManagerFile.getTasks().size(), "Does not contain two tasks.");
    }
}
