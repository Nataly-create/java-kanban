package tracker.controllers;
import java.util.*;

import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;
import tracker.model.TaskType;

public class InMemoryTaskManager implements TaskManager {
    private static int count = 0;
    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();
    protected TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    public static int getCount() {
        return count;
    }

    public static void setCount(int count) {
        InMemoryTaskManager.count = count;
    }

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
    public ArrayList<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
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
        } else {
            task = epics.getOrDefault(id, null);
        }
        if (task != null) {
            historyManager.add(task);
        }
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
            historyManager.remove(id);
            prioritizedTasks.remove(tasks.get(id));
        } else if (subtasks.containsKey(id)) {
            Epic epic = ((Subtask) getById(id)).getEpic();
            epic.deleteSubtask(subtasks.get(id));
            subtasks.remove(id);
            historyManager.remove(id);
            prioritizedTasks.remove(subtasks.get(id));
        } else if (epics.containsKey(id)) {
            deleteSubtasksOfEpic(id);
            epics.remove(id);
            historyManager.remove(id);
            prioritizedTasks.remove(epics.get(id));
        }
    }

    @Override
    public void deleteSubtasksOfEpic(int id) {
        ArrayList<Subtask> subtasksToDelete = epics.get(id).getSubtasks();
        for (Subtask subtask : subtasksToDelete) {
            int idSubtask = subtask.getId();
            subtasks.remove(idSubtask);
            historyManager.remove(idSubtask);
            prioritizedTasks.remove(subtasks.get(id));
        }
    }

    @Override
    public void addTask(Task task) {
        if (hasIntersects(task)) {
            return;
        }
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
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
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
        for (Task task : tasks.values()) {
            historyManager.remove(task.getId());
            prioritizedTasks.remove(task);
        }
        tasks.clear();
    }

    @Override
    public void deleteSubtasks() {
        for (Epic epic : epics.values()) {
            deleteSubtasksOfEpic(epic.getId());
            epic.setStatus();
            epic.setDuration();
            epic.setStartTime();
        }
        subtasks.clear();
    }

    @Override
    public void deleteEpics() {
        for (Task epic : epics.values()) {
            historyManager.remove(epic.getId());
            prioritizedTasks.remove(epic);
        }
        for (Task subtask : subtasks.values()) {
            historyManager.remove(subtask.getId());
            prioritizedTasks.remove(subtask);
        }
        epics.clear();
        subtasks.clear();
    }

    public void printMessage(Object message) {
        System.out.println(message);
    }

    @Override
    public boolean hasIntersects(Task task) {
        return getPrioritizedTasks().stream().filter(t -> !t.equals(task)).anyMatch(task::isIntersect);
    }
}