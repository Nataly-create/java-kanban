package tracker.controllers;
import java.util.ArrayList;
import java.util.HashMap;
import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;
import tracker.model.TaskType;

public class InMemoryTaskManager implements TaskManager {
    private static int count = 0;
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Subtask> getSubtasksForEpic(Epic epic) {
        return new ArrayList<>(epic.getSubtasks());
    }

    @Override
    public int getNewId() {
        return ++count;
    }

    @Override
    public Task getById(int id) {
        Task task;
        if (tasks.containsKey(id)) {
            task = tasks.get(id);
        } else if (subtasks.containsKey(id)) {
            task = subtasks.get(id);
        } else task = epics.getOrDefault(id, null);
        historyManager.add(task);
        return task;
    }

    @Override
    public ArrayList<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public void deleteById(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else if (subtasks.containsKey(id)) {
            Epic epic = ((Subtask) getById(id)).getEpic();
            epic.deleteSubtask(subtasks.get(id));
            subtasks.remove(id);
        } else if (epics.containsKey(id)) {
            deleteSubtasksOfEpic(id);
            epics.remove(id);
        }
    }

    @Override
    public void deleteSubtasksOfEpic(int id) {
        ArrayList<Subtask> subtasksToDelete = epics.get(id).getSubtasks();
        for (Subtask subtask : subtasksToDelete)
            subtasks.remove(subtask.getId());
    }

    @Override
    public void addTask(Task task) {
        if (task.getClass() == Task.class) {
            ((Task) task).setId(getNewId());
            tasks.put(count, (Task) task);
        } else if (task.getClass() == Subtask.class) {
            ((Subtask) task).setId(getNewId());
            subtasks.put(count, (Subtask) task);
            updateTask(((Subtask) task).getEpic());
        } else if (task.getClass() == Epic.class) {
            ((Epic) task).setId(getNewId());
            epics.put(count, (Epic) task);
        }
    }

    @Override
    public void updateTask(Object task) {
        if (task.getClass() == Task.class) {
            tasks.put(((Task) task).getId(), (Task) task);
        } else if (task.getClass() == Subtask.class) {
            subtasks.put(((Subtask) task).getId(), (Subtask) task);
        } else if (task.getClass() == Epic.class) {
            epics.put(((Epic) task).getId(), (Epic) task);
        }
    }

    @Override
    public void deleteTasksByType(TaskType type) {
        switch (type) {
            case TASK:
                deleteTasks();
                break;
            case SUBTASK:
                deleteSubtasks();
                break;
            case EPIC:
                deleteEpics();
                break;
        }
    }

    @Override
    public void deleteTasks() {
        tasks.clear();
    }

    @Override
    public void deleteSubtasks() {
        for (Epic epic : epics.values()) {
            deleteSubtasksOfEpic(epic.getId());
            epic.setStatus();
        }
        subtasks.clear();
    }

    @Override
    public void deleteEpics() {
        epics.clear();
        subtasks.clear();
    }

    public void printMessage(Object message) {
        System.out.println(message);
    }
}
