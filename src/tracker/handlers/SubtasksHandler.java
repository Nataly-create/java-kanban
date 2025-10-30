package tracker.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import tracker.controllers.TaskManager;
import tracker.exceptions.NotFoundException;
import tracker.handlers.adapters.EpicAdapter;
import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;
import tracker.model.TaskType;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class SubtasksHandler extends BaseHttpHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            HashMap<String, Object> parameters = getParameters(httpExchange);
            String requestMethod = (String) parameters.get("requestMethod");
            String[] pathParts = (String[]) parameters.get("pathParts");
            GsonBuilder gsonBuilder = getGsonBuilder();
            gsonBuilder.registerTypeAdapter(Epic.class, new EpicAdapter(taskManager));
            Gson gson = gsonBuilder.create();

            if (requestMethod.equals("GET")) {
                if (pathParts.length == 2) {
                    sendText(httpExchange, gson.toJson(taskManager.getSubtasks()), 200);
                } else if (pathParts.length == 3) {
                    String idString = pathParts[2];
                    int id = Integer.parseInt(idString);
                    Task task = getByIdByType(httpExchange, id, TaskType.SUBTASK);
                    if (task != null) {
                        sendText(httpExchange, gson.toJson(task), 200);
                    }
                }
            }

            if (requestMethod.equals("POST")) {
                InputStream inputStream = httpExchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                Subtask subtask = gson.fromJson(body, Subtask.class);
                if (taskManager.hasIntersects(subtask)) {
                    sendHasInteractions(httpExchange, subtask);
                } else {
                    if (subtask.getId() == 0) {
                        taskManager.addTask(subtask);
                        sendText(httpExchange, "Subtask " + subtask.getId() + " has been added.", 201);
                    } else {
                        taskManager.updateTask(subtask);
                        sendText(httpExchange, "Subtask " + subtask.getId() + " has been updated.", 201);
                    }
                }
            }

            if (requestMethod.equals("DELETE")) {
                int id = Integer.parseInt(pathParts[2]);
                deleteByIdByType(httpExchange, id, TaskType.SUBTASK);
                sendText(httpExchange, "Subtask " + id + " has been deleted.", 200);
            }

        } catch (NumberFormatException | NotFoundException e) {
            sendNotFound(httpExchange, e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public SubtasksHandler(TaskManager taskManager) {
        super(taskManager);
    }
}