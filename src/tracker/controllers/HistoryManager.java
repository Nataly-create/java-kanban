package tracker.controllers;

import tracker.model.Task;
import java.util.ArrayList;

public interface HistoryManager {
    public static final int SIZE_HISTORY = 10;

    public void add(Task task);

    public ArrayList<Task> getHistory();
}
