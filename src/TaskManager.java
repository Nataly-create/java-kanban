import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    static int count = 0;
    static HashMap<Integer, Task> tasks = new HashMap<>();
    static HashMap<Integer, Subtask> subtasks = new HashMap<>();
    static HashMap<Integer, Epic> epics = new HashMap<>();

    public static HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public static HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public static HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public static int getNewId() {
        return ++count;
    }

    public static Task getById(int id) {
        if (tasks.containsKey(id)) {
            return tasks.get(id);
        } else if (subtasks.containsKey(id)) {
            return subtasks.get(id);
        } else return epics.getOrDefault(id, null);
    }

    public static void deleteById(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else if (subtasks.containsKey(id)) {
            subtasks.remove(id);
        } if (epics.containsKey(id)) {
            epics.remove(id);
        }  else
            printMessage("ID not found");
        }

    public static void addTask(Object task) {
        if(task.getClass() == Task.class) {
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

    public static void updateTask(Object task) {
        if(task.getClass() == Task.class) {
            tasks.put(((Task) task).getId(), (Task) task);
        } else if (task.getClass() == Subtask.class) {
            subtasks.put(((Subtask) task).getId(), (Subtask) task);
        } else if (task.getClass() == Epic.class) {
            epics.put(((Epic) task).getId(), (Epic) task);
        } else
            printMessage("Unknown object class");
    }

    public static void deleteTasks(Object tasks) {
          ((HashMap<?, ?>)tasks).clear();
           printMessage("Tasks have been deleted");
    }

    public static void printMessage(Object message) {
        System.out.println(message);
    }

    public static ArrayList<Object> getAllTasks() {
        ArrayList<Object> allTasks= new ArrayList<>();
        printMessage("- - - Tasks - - -");
        for(Object task: tasks.values()) {
            System.out.println(task);
            allTasks.add(task);
        }
        printMessage("- - - Epics - - -");
        for(Object epic: epics.values()) {
            printMessage(epic);
            allTasks.add(epic);
        }
        printMessage("- - - Subtasks - - -");
        for(Object subtask: subtasks.values()) {
            printMessage(subtask);
            allTasks.add(subtask);
        }

        return allTasks;
    }
}
