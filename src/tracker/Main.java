package tracker;

import tracker.controllers.FileBackedTaskManager;
import tracker.controllers.Managers;
import tracker.model.*;
import tracker.controllers.InMemoryTaskManager;

import java.io.File;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        File file = new File("test.csv");
        FileBackedTaskManager fileBackedTaskManager = Managers.getDefaultFileBacked(file);
        Task task1 = new Task("Task 1", "Do task 1");
        fileBackedTaskManager.addTask(task1);
        Task task2 = new Task("Task 2", "Do task 2");
        fileBackedTaskManager.addTask(task2);

        Epic epic1 = new Epic("Epic 1", "Do epic 1");
        fileBackedTaskManager.addTask(epic1);
        Subtask subtask1 = new Subtask("Subtask 1", "Do subtask 1", epic1);
        subtask1.setStatus(Status.DONE);
        fileBackedTaskManager.addTask(subtask1);
        Subtask subtask2 = new Subtask("Subtask 2", "Do subtask 2", epic1);
        subtask2.setStatus(Status.IN_PROGRESS);
        fileBackedTaskManager.addTask(subtask2);

        Epic epic2 = new Epic("Epic 2", "Do epic 2");
        fileBackedTaskManager.addTask(epic2);
        Subtask subtask3 = new Subtask("Subtask 3", "Do subtask 3", epic2);
        fileBackedTaskManager.addTask(subtask3);

        System.out.println("- - - Tasks - - -");
        printArrayList(fileBackedTaskManager, TaskType.TASK);
        System.out.println("- - - Epics - - -");
        printArrayList(fileBackedTaskManager, TaskType.EPIC);
        System.out.println("- - - Subtask - - -");
        printArrayList(fileBackedTaskManager, TaskType.SUBTASK);

        System.out.println(" . . . History . . . ");
        for (int i = 1; i < 14; ++i) {
            fileBackedTaskManager.getById(i % 7 + 1);
        }
        ArrayList<Task> history = fileBackedTaskManager.getHistory();
        for (Task task : history) {
            System.out.println(task);
        }

        fileBackedTaskManager.save();
        FileBackedTaskManager fileBackedTaskManagerFile = FileBackedTaskManager.loadFromFile(file);

        System.out.println("- - - Tasks from file - - -");
        printArrayList(fileBackedTaskManagerFile, TaskType.TASK);
        System.out.println("- - - Epics from file- - -");
        printArrayList(fileBackedTaskManagerFile, TaskType.EPIC);
        System.out.println("- - - Subtask from file- - -");
        printArrayList(fileBackedTaskManagerFile, TaskType.SUBTASK);

    }

    public static void printArrayList(InMemoryTaskManager inMemoryTaskManager, TaskType type) {
        switch (type) {
            case TASK:
            for (Task task : inMemoryTaskManager.getTasks()) {
                System.out.println(task);
            }
            break;
            case EPIC:
            for (Epic epic : inMemoryTaskManager.getEpics()) {
                inMemoryTaskManager.printMessage(epic);
            }
            break;
            case SUBTASK:
            for (Subtask subtask : inMemoryTaskManager.getSubtasks()) {
                inMemoryTaskManager.printMessage(subtask);
            }
            break;
        }
    }
}