package tracker.handlers.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import tracker.controllers.TaskManager;
import tracker.exceptions.NotFoundException;
import tracker.model.Epic;

import java.io.IOException;

public class EpicAdapter extends TypeAdapter<Epic> {
    TaskManager taskManager;

    public EpicAdapter(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void write(final JsonWriter jsonWriter, final Epic epic) throws IOException {
        jsonWriter.value(epic.getId());
    }

    @Override
    public Epic read(final JsonReader jsonReader) throws IOException {
        try {
            return (Epic) taskManager.getById(jsonReader.nextInt());
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
