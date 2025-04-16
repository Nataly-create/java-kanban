public class Main {

    public static void main(String[] args) {
        Task task1 = new Task("Task 1", "Do task 1");
        TaskManager.addTask(task1);
        Task task2 = new Task("Task 2", "Do task 2");
        TaskManager.addTask(task2);

        Epic epic1 = new Epic("Epic 1", "Do epic 1");
        TaskManager.addTask(epic1);
        Subtask subtask1 = new Subtask("Subtask 1", "Do subtask 1", epic1);
        subtask1.setStatus(Status.DONE);
        TaskManager.addTask(subtask1);
        Subtask subtask2 = new Subtask("Subtask 2", "Do subtask 2", epic1);
        subtask2.setStatus(Status.IN_PROGRESS);
        TaskManager.addTask(subtask2);

        Epic epic2 = new Epic("Epic 2", "Do epic 2");
        TaskManager.addTask(epic2);
        Subtask subtask3 = new Subtask("Subtask 3", "Do subtask 3", epic2);
        TaskManager.addTask(subtask3);

        TaskManager.getAllTasks();
    }
}
