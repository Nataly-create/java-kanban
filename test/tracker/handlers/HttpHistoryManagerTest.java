package tracker.handlers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tracker.HttpTaskServer;
import tracker.controllers.InMemoryTaskManager;
import tracker.controllers.TaskManager;
import tracker.exceptions.NotFoundException;
import tracker.handlers.adapters.SubtaskAdapter;
import tracker.handlers.adapters.TaskListTypeToken;
import tracker.model.Subtask;
import tracker.model.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpHistoryManagerTest {

    // создаём экземпляр InMemoryTaskManager
    TaskManager manager = new InMemoryTaskManager();
    // передаём его в качестве аргумента в конструктор HttpTaskServer
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = new BaseHttpHandler(manager).getGsonBuilder().create();

    public HttpHistoryManagerTest() throws IOException {
    }

    @BeforeEach
    public void setUp() throws IOException {
        manager.deleteTasks();
        manager.deleteSubtasks();
        manager.deleteEpics();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void testHistory() throws IOException, InterruptedException, NotFoundException {
        Task task = new Task("Test title", "Test description");
        manager.addTask(task);
        manager.getById(task.getId());

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());

        String body = response.body();
        List<Task> history = gson.fromJson(body, new TaskListTypeToken().getType());

        assertEquals(1, history.size(), "Некорректное количество задач");
    }
}