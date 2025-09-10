package tracker.model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private Epic epic;

    public Subtask(String title, String description, Epic epic, Duration duration, LocalDateTime startTime) {
        super(title, description);
        setEpic(epic);
        this.setStartTime(startTime);
        this.setDuration(duration);
    }

    public Subtask(String title, String description, Epic epic) {
        this(title, description, epic, Duration.ofMinutes(0), null);
    }

    @Override
    public String getString() {
        return super.getString() +
                ", epic='" + epic.getId() + '\'';
    }

    @Override
    public String toString() {
        return "Subtask{" + getString() + '}';
    }

    public Epic getEpic() {
        return epic;
    }

    private void setEpic(Epic epic) {
        this.epic = epic;
        epic.getSubtasks().add(this);
        epic.setDuration();
    }

    @Override
    public void setStatus(Status status) {
        super.setStatus(status);
        getEpic().setStatus();
    }

    @Override
    public TaskType getType() {
        return TaskType.SUBTASK;
    }

    @Override
    public void setDuration(Duration duration) {
        super.setDuration(duration);
        epic.setDuration();
    }

    @Override
    public void setDuration(int duration) {
        super.setDuration(duration);
        epic.setDuration();
    }

    @Override
    public void setStartTime(LocalDateTime startTime) {
        super.setStartTime(startTime);
        epic.setStartTime();
    }
}

