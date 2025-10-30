package tracker;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import tracker.controllers.FileBackedTaskManager;
import tracker.controllers.InMemoryTaskManager;
import tracker.controllers.Managers;
import tracker.controllers.TaskManager;
import tracker.exceptions.NotFoundException;
import tracker.handlers.*;
import tracker.handlers.adapters.DurationAdapter;
import tracker.handlers.adapters.EpicAdapter;
import tracker.handlers.adapters.LocalDataTimeAdapter;
import tracker.model.*;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    TaskManager taskManager;
    static HttpServer httpServer;

    public HttpTaskServer(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public HttpTaskServer() {
        this.taskManager = Managers.getDefault();
    }

    public void start() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TasksHandler(taskManager));
        httpServer.createContext("/subtasks", new SubtasksHandler(taskManager));
        httpServer.createContext("/epics", new EpicsHandler(taskManager));
        httpServer.createContext("/history", new HistoryHandler(taskManager));
        httpServer.createContext("/prioritized", new PrioritizedHandler(taskManager));
        httpServer.start();
        System.out.println("HTTP-server is running.");
    }

    public void stop() {
        httpServer.stop(0);
    }

    public static void main(String[] args) throws IOException {
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
        httpTaskServer.testTaskManager();
    }

    public static void test() {
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
        try {
            for (int i = 1; i < 14; ++i) {
                fileBackedTaskManager.getById(i % 7 + 1);
            }
        } catch (NotFoundException e) {
            System.out.println(e.getMessage());
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

    public void testTaskManager() {
        Task task1 = new Task("Task 1", "Do task 1", Duration.ofMinutes(60), LocalDateTime.now().plusMinutes(120));
        taskManager.addTask(task1);

        Task task2 = new Task("Task 2", "Do task 2", Duration.ofMinutes(140), LocalDateTime.now().minusMinutes(20));
        taskManager.addTask(task2);

        System.out.println(task1.isIntersect(task2));
        System.out.println(taskManager.hasIntersects(task1));
        Epic epic1 = new Epic("Epic 1", "Do epic 1");
        taskManager.addTask(epic1);
        Subtask subtask1 = new Subtask("Subtask 1", "Do subtask 1", epic1);
        subtask1.setStatus(Status.DONE);
        subtask1.setStartTime((short) 2025, (byte) 7, (byte) 30, (byte) 10, (byte) 0);
        subtask1.setDuration(100);
        taskManager.addTask(subtask1);

        Subtask subtask2 = new Subtask("Subtask 2", "Do subtask 2", epic1);
        subtask2.setStatus(Status.IN_PROGRESS);
        subtask2.setStartTime(LocalDateTime.now().withSecond(0).withNano(0));
        subtask2.setDuration(300);
        taskManager.addTask(subtask2);

        Epic epic2 = new Epic("Epic 2", "Do epic 2");
        taskManager.addTask(epic2);
        Subtask subtask3 = new Subtask("Subtask 3", "Do subtask 3", epic2);
        subtask3.setStartTime(LocalDateTime.now().withSecond(0).withNano(0));
        taskManager.addTask(subtask3);

        System.out.println("- - - Tasks - - -");
        printArrayList(taskManager, TaskType.TASK);
        System.out.println("- - - Epics - - -");
        printArrayList(taskManager, TaskType.EPIC);
        System.out.println("- - - Subtask - - -");
        printArrayList(taskManager, TaskType.SUBTASK);
        System.out.println("- - - Prioritized Tasks - - -");
        printPrioritizedArrayList(taskManager);

        System.out.println(" . . . History . . . ");
        try {
            for (int i = 1; i < 14; ++i) {
                taskManager.getById(i % 7 + 1);
            }
        } catch (NotFoundException exception) {
            System.out.println(exception.getMessage());
        }

        ArrayList<Task> history = taskManager.getHistory();
        for (Task task : history) {
            System.out.println(task);
        }

        Task task4 = new Task("Task 4", "Do task 4");
        taskManager.addTask(task4);

        GsonBuilder gsonBuilder = new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(Epic.class, new EpicAdapter(taskManager))
                .registerTypeAdapter(LocalDateTime.class, new LocalDataTimeAdapter());

        Gson gson = gsonBuilder.create();
        try {
            System.out.println(gson.toJson(taskManager.getById(4)));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static <T extends TaskManager> void printArrayList (T inMemoryTaskManager, TaskType type) {
        switch (type) {
            case TASK:
                for (Task task : inMemoryTaskManager.getTasks()) {
                    System.out.println(task);
                }
                break;
            case EPIC:
                for (Epic epic : inMemoryTaskManager.getEpics()) {
                    System.out.println(epic);
                }
                break;
            case SUBTASK:
                for (Subtask subtask : inMemoryTaskManager.getSubtasks()) {
                    System.out.println(subtask);
                }
                break;
        }
    }

    public static <T extends TaskManager> void printPrioritizedArrayList(T inMemoryTaskManager) {
        for (Task task : inMemoryTaskManager.getPrioritizedTasks()) {
            System.out.println(task);
        }
    }
}