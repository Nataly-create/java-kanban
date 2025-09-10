package tracker.controllers;

import tracker.exceptions.ManagerSaveException;
import tracker.model.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        super();
        this.file = file;
    }

    public String toString(Task task) {
        LocalDateTime startTime = task.getStartTime();
        String startTimeString = (startTime == null) ? "null" : startTime.toString();
        return task.getId() + "," + task.getType() + "," + task.getTitle() + "," +
               task.getStatus() + "," + task.getDescription() + "," + startTimeString + "," + task.getDuration().toMinutes() +
               (task.getType() == TaskType.SUBTASK ? "," + ((Subtask) task).getEpic().getId() : "");
    }

    public Task fromString(String value) {
        String[] partsValue = value.split(",");

        Task task;
        int id = Integer.parseInt(partsValue[0]);
        String type =  partsValue[1];
        String name = partsValue[2];
        String description = partsValue[4];
        String startTimeString = partsValue[5];
        LocalDateTime startTime = (startTimeString.equals("null")) ? null : LocalDateTime.parse(startTimeString);
        int duration = Integer.parseInt(partsValue[6]);

        if (type.equals("EPIC")) {
            task = new Epic(name, description);
        } else if (type.equals("SUBTASK")) {
            int idEpic = Integer.parseInt(partsValue[7]);
            task = new Subtask(name, description, epics.get(idEpic), Duration.ofMinutes(duration), startTime);
        } else {
            task = new Task(name, description, Duration.ofMinutes(duration), startTime);
        }

        task.setId(Integer.parseInt(partsValue[0]));
        task.setStatus(Status.valueOf(partsValue[3]));

        return task;
    }

    public void save() {
        try (Writer fileWriter = new FileWriter(file)) {
            fileWriter.write("id,type,name,status,description,startTime,duration, epic\n");
            for (Task task: getTasks()) {
                fileWriter.write(toString(task) + "\n");
            }
            for (Epic epic: getEpics()) {
                fileWriter.write(toString(epic) + "\n");
            }
            for (Subtask subtask: getSubtasks()) {
                fileWriter.write(toString(subtask) + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileBackedTaskManager = Managers.getDefaultFileBacked(file);
        try {
            String content = Files.readString(file.toPath());
            String[] contentList = content.split("\n");
            for (int i = 1; i < contentList.length; ++i) {
                Task task = fileBackedTaskManager.fromString(contentList[i]);
                int addedId = fileBackedTaskManager.addTaskFromFile(task);
                setCount(addedId);
            }
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
        return fileBackedTaskManager;
    }

    public int addTaskFromFile(Task task) {
        int id = task.getId();
        if (task.getClass() == Task.class) {
            tasks.put(id, (Task) task);
        } else if (task.getClass() == Subtask.class) {
            subtasks.put(id, (Subtask) task);
            updateTask(((Subtask) task).getEpic());
        } else if (task.getClass() == Epic.class) {
            epics.put(id, (Epic) task);
        }
        return id;
    }

     public static void setCount(int id) {
        if (id > getCount()) {
            setCount(id);
        }
    }

    @Override
    public void deleteById(int id) {
        super.deleteById(id);
        save();
    }

    @Override
    public void deleteSubtasksOfEpic(int id) {
        super.deleteSubtasksOfEpic(id);
        save();
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void updateTask(Object task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void deleteTasksByType(TaskType type) {
        super.deleteTasksByType(type);
        save();
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public void deleteSubtasks() {
        super.deleteSubtasks();
        save();
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
        save();
    }

}
