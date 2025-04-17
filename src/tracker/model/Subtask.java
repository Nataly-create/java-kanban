package tracker.model;

public class Subtask extends Task {
    private Epic epic;

    public Subtask(String title, String description, Epic epic) {
        super(title, description);
        setEpic(epic);
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
}

