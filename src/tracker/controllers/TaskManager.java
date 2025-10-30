package tracker.controllers;

import tracker.exceptions.NotFoundException;
import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;
import tracker.model.TaskType;

import java.util.ArrayList;

public interface TaskManager {
    ArrayList<Task> getTasks();

    ArrayList<Subtask> getSubtasks();

    ArrayList<Epic> getEpics();

    ArrayList<Subtask> getSubtasksForEpic(Epic epic);

    ArrayList<Task> getHistory();

    ArrayList<Task> getPrioritizedTasks();

    int getNewId();

    Task getById(int id) throws NotFoundException;

    void deleteById(int id) throws NotFoundException;

    void deleteSubtasksOfEpic(int id);

    void addTask(Task task);

    void updateTask(Object task);

    void deleteTasksByType(TaskType type);

    void deleteTasks();

    void deleteSubtasks();

    void deleteEpics();

    boolean hasIntersects(Task task);
}
