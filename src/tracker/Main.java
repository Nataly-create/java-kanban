package tracker;
import java.util.ArrayList;

import tracker.model.*;
import tracker.controllers.TaskManager;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Task task1 = new Task("Task 1", "Do task 1");
        taskManager.addTask(task1);
        Task task2 = new Task("Task 2", "Do task 2");
        taskManager.addTask(task2);

        Epic epic1 = new Epic("Epic 1", "Do epic 1");
        taskManager.addTask(epic1);
        Subtask subtask1 = new Subtask("Subtask 1", "Do subtask 1", epic1);
        subtask1.setStatus(Status.DONE);
        taskManager.addTask(subtask1);
        Subtask subtask2 = new Subtask("Subtask 2", "Do subtask 2", epic1);
        subtask2.setStatus(Status.IN_PROGRESS);
        taskManager.addTask(subtask2);

        Epic epic2 = new Epic("Epic 2", "Do epic 2");
        taskManager.addTask(epic2);
        Subtask subtask3 = new Subtask("Subtask 3", "Do subtask 3", epic2);
        taskManager.addTask(subtask3);

        System.out.println("- - - Tasks - - -");
        printArrayList(taskManager, TaskType.TASK);
        System.out.println("- - - Epics - - -");
        printArrayList(taskManager, TaskType.EPIC);
        System.out.println("- - - Subtask - - -");
        printArrayList(taskManager, TaskType.SUBTASK);
    }

    public static void printArrayList(TaskManager taskManager, TaskType type) {
        switch (type) {
            case TASK:
            for (Task task : taskManager.getTasks()) {
                System.out.println(task);
            }
            break;
            case EPIC:
            for (Epic epic : taskManager.getEpics()) {
                taskManager.printMessage(epic);
            }
            break;
            case SUBTASK:
            for (Subtask subtask : taskManager.getSubtasks()) {
                taskManager.printMessage(subtask);
            }
            break;
        }
    }
}