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

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            HashMap<String, Object> parameters = getParameters(httpExchange);
            String requestMethod = (String) parameters.get("requestMethod");
            GsonBuilder gsonBuilder = getGsonBuilder();
            gsonBuilder.registerTypeAdapter(Epic.class, new EpicAdapter(taskManager));
            Gson gson = gsonBuilder.create();

            if (requestMethod.equals("GET")) {
                ArrayList<Task> history = taskManager.getHistory();
                if (history.isEmpty()) {
                    sendText(httpExchange, "Not found.", 404);
                } else {
                    sendText(httpExchange, gson.toJson(history), 200);
                }
            } else {
                sendText(httpExchange, "Method not allowed.", 405);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public HistoryHandler(TaskManager taskManager) {
        super(taskManager);
    }
}