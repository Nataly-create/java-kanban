package tracker;

import tracker.controllers.Managers;
import tracker.model.*;
import tracker.controllers.InMemoryTaskManager;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        InMemoryTaskManager inMemoryTaskManager = (InMemoryTaskManager) Managers.getDefault();
        Task task1 = new Task("Task 1", "Do task 1");
        inMemoryTaskManager.addTask(task1);
        Task task2 = new Task("Task 2", "Do task 2");
        inMemoryTaskManager.addTask(task2);

        Epic epic1 = new Epic("Epic 1", "Do epic 1");
        inMemoryTaskManager.addTask(epic1);
        Subtask subtask1 = new Subtask("Subtask 1", "Do subtask 1", epic1);
        subtask1.setStatus(Status.DONE);
        inMemoryTaskManager.addTask(subtask1);
        Subtask subtask2 = new Subtask("Subtask 2", "Do subtask 2", epic1);
        subtask2.setStatus(Status.IN_PROGRESS);
        inMemoryTaskManager.addTask(subtask2);

        Epic epic2 = new Epic("Epic 2", "Do epic 2");
        inMemoryTaskManager.addTask(epic2);
        Subtask subtask3 = new Subtask("Subtask 3", "Do subtask 3", epic2);
        inMemoryTaskManager.addTask(subtask3);

        System.out.println("- - - Tasks - - -");
        printArrayList(inMemoryTaskManager, TaskType.TASK);
        System.out.println("- - - Epics - - -");
        printArrayList(inMemoryTaskManager, TaskType.EPIC);
        System.out.println("- - - Subtask - - -");
        printArrayList(inMemoryTaskManager, TaskType.SUBTASK);

        System.out.println(" . . . History . . . ");
        for (int i = 1; i < 14; ++i) {
            inMemoryTaskManager.getById(i % 7 + 1);
        }
        ArrayList<Task> history = inMemoryTaskManager.getHistory();
        for (Task task : history) {
            System.out.println(task);
        }
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