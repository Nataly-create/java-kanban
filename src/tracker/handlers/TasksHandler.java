package tracker.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import tracker.controllers.TaskManager;
import tracker.exceptions.NotFoundException;
import tracker.model.Task;
import tracker.model.TaskType;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class TasksHandler extends BaseHttpHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            HashMap<String, Object> parameters = getParameters(httpExchange);
            String requestMethod = (String) parameters.get("requestMethod");
            String[] pathParts = (String[]) parameters.get("pathParts");
            GsonBuilder gsonBuilder = getGsonBuilder();
            Gson gson = gsonBuilder.create();

            if (requestMethod.equals("GET")) {
                if (pathParts.length == 2) {
                    sendText(httpExchange, gson.toJson(taskManager.getTasks()), 200);
                } else if (pathParts.length == 3) {
                    String idString = pathParts[2];
                    int id = Integer.parseInt(idString);
                    Task task = getByIdByType(httpExchange, id, TaskType.TASK);
                    if (task != null) {
                        sendText(httpExchange, gson.toJson(task), 200);
                    }
                }
            }

            if (requestMethod.equals("POST")) {
                InputStream inputStream = httpExchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                Task task = gson.fromJson(body, Task.class);
                if (taskManager.hasIntersects(task)) {
                    sendHasInteractions(httpExchange, task);
                } else {
                    if (task.getId() == 0) {
                        taskManager.addTask(task);
                        sendText(httpExchange, "Task " + task.getId() + " has been added.", 201);
                    } else {
                        taskManager.updateTask(task);
                        sendText(httpExchange, "Task " + task.getId() + " has been updated.", 201);
                    }
                }
            }

            if (requestMethod.equals("DELETE")) {
                int id = Integer.parseInt(pathParts[2]);
                deleteByIdByType(httpExchange, id, TaskType.TASK);
                sendText(httpExchange, "Task " + id + " has been deleted.", 200);
            }

        } catch (NumberFormatException | NotFoundException e) {
            sendNotFound(httpExchange, e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public TasksHandler (TaskManager taskManager) {
        super(taskManager);
    }
}