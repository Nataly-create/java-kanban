package tracker.model;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    private int id;
    private String title;
    private String description;
    private Status status;
    private Duration duration;
    private LocalDateTime startTime;

    public Task(String title, String description, Duration duration, LocalDateTime startTime) {
        this.title = title;
        this.description = description;
        this.status = Status.NEW;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(String title, String description) {
        this(title, description, Duration.ofMinutes(0), null);
    }

    @Override
    public final boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public final int hashCode() {
        return Objects.hashCode(id);
    }

    public String getString() {
        return "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", startTime='" + startTime + '\'' +
                ", duration='" + duration.toMinutes() + '\'' +
                ", endTime='" + getEndTime() + '\'';
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        if (startTime != null)
            return startTime.plus(duration);
        else
            return null;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setDuration(int minutes) {
        this.duration = Duration.ofMinutes(minutes);
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setStartTime(short year, byte month, byte day, byte hour, byte minute) {
        this.startTime = LocalDateTime.of(year, month, day, hour, minute);
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public TaskType getType() {
        return TaskType.TASK;
    }

    @Override
    public String toString() {
        return "Task{" + getString() + '}';
    }

    public boolean isIntersect(Task task) {
        LocalDateTime firstStart = startTime;
        LocalDateTime firstEnd = getEndTime();
        LocalDateTime secondStart = task.getStartTime();
        LocalDateTime secondEnd = task.getEndTime();
        LocalDateTime maxStart;
        LocalDateTime minEnd;

        if (firstStart == null || firstEnd == null || secondStart == null || secondEnd == null) {
            return false;
        }

        if (firstStart.isBefore(secondStart)) {
            maxStart = secondStart;
        } else {
            maxStart = firstStart;
        }

        if (firstEnd.isAfter(secondEnd)) {
            minEnd = secondEnd;
        } else {
            minEnd = firstEnd;
        }
        return minEnd.isAfter(maxStart);
    }
}