package tracker.handlers;

import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import tracker.controllers.TaskManager;
import tracker.exceptions.NotFoundException;
import tracker.handlers.adapters.DurationAdapter;
import tracker.handlers.adapters.LocalDataTimeAdapter;
import tracker.model.Task;
import tracker.model.TaskType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;

public class BaseHttpHandler {
    protected final TaskManager taskManager;

    public BaseHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    protected HashMap<String, Object> getParameters(HttpExchange httpExchange) {
        String requestPath = httpExchange.getRequestURI().getPath();
        String[] pathParts = requestPath.split("/");
        String requestMethod = httpExchange.getRequestMethod();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("pathParts", pathParts);
        hashMap.put("requestMethod", requestMethod);
        return hashMap;
    }

    public GsonBuilder getGsonBuilder() {
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDataTimeAdapter());
    }

    protected void sendText(HttpExchange h, String text, int code) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(code, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendNotFound(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "text/plain;charset=utf-8");
        h.sendResponseHeaders(404, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendHasInteractions(HttpExchange h, Task task) throws IOException {
        String text = "Task " + task.toString() + " has interactions.";
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "text/plain;charset=utf-8");
        h.sendResponseHeaders(406, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected Task getByIdByType(HttpExchange httpExchange, int id, TaskType type) throws IOException, NotFoundException {
        try {
            Task task = taskManager.getById(id);
            if (task.getType() == type) {
                return task;
            } else {
                throw new NotFoundException(type + " " + id + " not found.");
            }
        } catch (NotFoundException exception) {
            sendNotFound(httpExchange, exception.getMessage());
            return null;
        }
    }

    protected void deleteByIdByType(HttpExchange httpExchange, int id, TaskType type) throws IOException, NotFoundException {
        try {
            Task task = taskManager.getById(id);
            if (task.getType() == type) {
                taskManager.deleteById(id);
            } else {
                throw new NotFoundException(type + " " + id + " not found.");
            }
        } catch (NotFoundException exception) {
            sendNotFound(httpExchange, exception.getMessage());
        }
    }

}
