package tracker.model;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Subtask> subtasks;
    private LocalDateTime endTime;

    public Epic(String title, String description) {
        super(title, description);
        this.subtasks = new ArrayList<>();
    }

    @Override
    public String getString() {
        return super.getString() +
                ", subtasks.size='" + getSubtasks().size() + '\'';
    }

    @Override
    public String toString() {
        return "Epic{" + getString() + '}';
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    public void deleteSubtask(Subtask subtask) {
        if (this.subtasks.contains(subtask)) {
            this.subtasks.remove(subtask);
            this.setStatus();
            setTimeValues();
        }
    }

    public void setStatus() {
        super.setStatus(calculateStatus());
    }

    private Status calculateStatus() {
        boolean statusNew = false;
        boolean statusInProgress = false;
        boolean statusDone = false;
        for (Task task : subtasks) {
            Status tasksStatus = task.getStatus();
            if (statusInProgress) return Status.IN_PROGRESS;
            if (tasksStatus == Status.DONE && !statusNew) {
                statusDone = true;
            } else if (tasksStatus == Status.NEW && !statusDone) {
                statusNew = true;
            } else statusInProgress = true;
        }
        return statusInProgress ? Status.IN_PROGRESS : (statusDone ? Status.DONE : Status.NEW);
    }

    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }

    public void setTimeValues() {
        setStartTime();
        setDuration();
    }

    public void setDuration() {
        Duration durationEpic = Duration.ZERO;
        for (Task subtask : subtasks) {
            Duration durationSubtask = subtask.getDuration();
            if (durationSubtask != null) {
                durationEpic = durationEpic.plus(durationSubtask);
            }
        }
        setDuration(durationEpic);
    }

    public void setStartTime() {
        LocalDateTime epicStartTime = null;
        for (Task subtask : subtasks) {
            LocalDateTime subtaskStartTime = subtask.getStartTime();
            epicStartTime = (epicStartTime == null) ? subtaskStartTime : epicStartTime;
            if ((subtaskStartTime != null) && (subtaskStartTime.isBefore(epicStartTime))) {
                epicStartTime = subtaskStartTime;
            }
        }
        this.setStartTime(epicStartTime);
    }

    @Override
    public LocalDateTime getEndTime() {
        LocalDateTime endTimeEpic = LocalDateTime.MIN;
        for (Task subtask : subtasks) {
            LocalDateTime endTimeTask = subtask.getEndTime();
            if (endTimeTask != null && endTimeTask.isAfter(endTimeEpic)) {
                endTimeEpic = endTimeTask;
            }
        }
        return endTimeEpic;
    }
}
