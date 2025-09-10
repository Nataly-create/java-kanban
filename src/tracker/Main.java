package tracker;

import tracker.controllers.FileBackedTaskManager;
import tracker.controllers.Managers;
import tracker.model.*;
import tracker.controllers.InMemoryTaskManager;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        File file = new File("test.csv");
        FileBackedTaskManager fileBackedTaskManager = Managers.getDefaultFileBacked(file);
        Task task1 = new Task("Task 1", "Do task 1", Duration.ofMinutes(60), LocalDateTime.now().plusMinutes(120));
        fileBackedTaskManager.addTask(task1);

        Task task2 = new Task("Task 2", "Do task 2", Duration.ofMinutes(140), LocalDateTime.now().minusMinutes(20));
        fileBackedTaskManager.addTask(task2);

        System.out.println(task1.isIntersect(task2));
        System.out.println(fileBackedTaskManager.hasIntersects(task1));
        Epic epic1 = new Epic("Epic 1", "Do epic 1");
        fileBackedTaskManager.addTask(epic1);
        Subtask subtask1 = new Subtask("Subtask 1", "Do subtask 1", epic1);
        subtask1.setStatus(Status.DONE);
        subtask1.setStartTime((short) 2025, (byte) 7, (byte) 30, (byte) 10, (byte) 0);
        subtask1.setDuration(100);
        fileBackedTaskManager.addTask(subtask1);

        Subtask subtask2 = new Subtask("Subtask 2", "Do subtask 2", epic1);
        subtask2.setStatus(Status.IN_PROGRESS);
        subtask2.setStartTime(LocalDateTime.now().withSecond(0).withNano(0));
        subtask2.setDuration(300);
        fileBackedTaskManager.addTask(subtask2);

        Epic epic2 = new Epic("Epic 2", "Do epic 2");
        fileBackedTaskManager.addTask(epic2);
        Subtask subtask3 = new Subtask("Subtask 3", "Do subtask 3", epic2);
        subtask3.setStartTime(LocalDateTime.now().withSecond(0).withNano(0));
        fileBackedTaskManager.addTask(subtask3);

        System.out.println("- - - Tasks - - -");
        printArrayList(fileBackedTaskManager, TaskType.TASK);
        System.out.println("- - - Epics - - -");
        printArrayList(fileBackedTaskManager, TaskType.EPIC);
        System.out.println("- - - Subtask - - -");
        printArrayList(fileBackedTaskManager, TaskType.SUBTASK);
        System.out.println("- - - Prioritized Tasks - - -");
        printPrioritizedArrayList(fileBackedTaskManager);

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

        Task task4 = new Task("Task 4", "Do task 4");
        fileBackedTaskManagerFile.addTask(task4);

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

    public static void printPrioritizedArrayList(InMemoryTaskManager inMemoryTaskManager) {
        for (Task task : inMemoryTaskManager.getPrioritizedTasks()) {
            System.out.println(task);
        }
    }
}