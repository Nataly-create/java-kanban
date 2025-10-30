package tracker.handlers.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import tracker.controllers.TaskManager;
import tracker.exceptions.NotFoundException;
import tracker.model.Subtask;

import java.io.IOException;

public class SubtaskAdapter extends TypeAdapter<Subtask> {
    TaskManager taskManager;

    public SubtaskAdapter(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void write(final JsonWriter jsonWriter, final Subtask subtask) throws IOException {
        jsonWriter.value(subtask.getId());
    }

    @Override
    public Subtask read(final JsonReader jsonReader) throws IOException {
        try {
            return (Subtask) taskManager.getById(jsonReader.nextInt());
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
