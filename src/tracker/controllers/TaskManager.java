package tracker.controllers;
import java.util.ArrayList;
import java.util.HashMap;
import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;
import tracker.model.TaskType;

public class TaskManager {
    private static int count = 0;
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Subtask> getSubtasksForEpic(Epic epic) {
        return new ArrayList<>(epic.getSubtasks());
    }

    public int getNewId() {
        return ++count;
    }

    public Task getById(int id) {
        if (tasks.containsKey(id)) {
            return tasks.get(id);
        } else if (subtasks.containsKey(id)) {
            return subtasks.get(id);
        } else return epics.getOrDefault(id, null);
    }

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

    private void deleteSubtasksOfEpic(int id) {
        ArrayList<Integer> keysToDelete = new ArrayList<>();
        for (Integer key : subtasks.keySet()) {
            Subtask subtask = subtasks.get(key);
            if (subtask.getEpic().getId() == id)
                keysToDelete.add(key);
        }
        for (int keyToDelete : keysToDelete)
            subtasks.remove(keyToDelete);
    }

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
        } else
            printMessage("Unknown object class");
    }

    public void updateTask(Object task) {
        if (task.getClass() == Task.class) {
            tasks.put(((Task) task).getId(), (Task) task);
        } else if (task.getClass() == Subtask.class) {
            subtasks.put(((Subtask) task).getId(), (Subtask) task);
        } else if (task.getClass() == Epic.class) {
            epics.put(((Epic) task).getId(), (Epic) task);
        } else
            printMessage("Unknown object class");
    }

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

    public void deleteTasks() {
        tasks.clear();
    }

    public void deleteSubtasks() {
        for (Epic epic : epics.values()) {
            deleteSubtasksOfEpic(epic.getId());
            epic.setStatus();
        }
        subtasks.clear();
    }

    public void deleteEpics() {
        epics.clear();
        subtasks.clear();
    }

    public void printMessage(Object message) {
        System.out.println(message);
    }
}
