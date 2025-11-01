package tracker.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import tracker.controllers.TaskManager;
import tracker.handlers.adapters.EpicAdapter;
import tracker.model.Epic;
import tracker.model.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            HashMap<String, Object> parameters = getParameters(httpExchange);
            String requestMethod = (String) parameters.get("requestMethod");
            GsonBuilder gsonBuilder = getGsonBuilder();
            gsonBuilder.registerTypeAdapter(Epic.class, new EpicAdapter(taskManager));
            Gson gson = gsonBuilder.create();

            if (requestMethod.equals("GET")) {
                ArrayList<Task> prioritized = taskManager.getPrioritizedTasks();
                if (prioritized.isEmpty()) {
                    sendText(httpExchange, "Not found.", 404);
                } else {
                    sendText(httpExchange, gson.toJson(prioritized), 200);
                }
            } else {
                sendText(httpExchange, "Method not allowed.", 405);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public PrioritizedHandler(TaskManager taskManager) {
        super(taskManager);
    }
}