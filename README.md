# **Java‑Kanban**  
A training project developed as part of the **Yandex Practicum Java Developer** program.  
The application implements a simple Kanban‑style task manager with Tasks, Epics, and Subtasks, including automatic status calculation and a task history feature.

---

## **Table of Contents**
- [Features](#features)
- [Technologies](#technologies)
- [Project Structure](#project-structure)
- [How to Run](#how-to-run)
- [Learning Objectives](#learning-objectives)
- [Author](#author)

---

## **Features**

- Create and manage:
  - **Tasks**
  - **Epics**
  - **Subtasks**
- Automatic Epic status calculation  
- In‑memory storage  
- History tracking of viewed tasks  
- Core managers:
  - `TaskManager`
  - `InMemoryTaskManager`
  - `HistoryManager`
  - `InMemoryHistoryManager`
- Unit tests for main components

---

## **Technologies**

- Java 11+  
- JUnit  
- Java Collections Framework

---

## **Project Structure**

```
src/
 ├── tracker/
 │    ├── controllers/
 │    │     ├── HistoryManager.java
 │    │     ├── InMemoryHistoryManager.java
 │    │     ├── InMemoryTaskManager.java
 │    │     ├── Managers.java
 │    │     └── TaskManager.java
 │
 ├── model/
 │    ├── Epic.java
 │    ├── Status.java
 │    ├── Subtask.java
 │    ├── Task.java
 │    ├── TaskType.java
 │    └── Main.java
 │
test/
 └── java/
      └── tracker/
           ├── controllers/
           │     ├── HistoryManagerTest.java
           │     ├── InMemoryTaskManagerTest.java
           │     └── ManagersTest.java
           │
           ├── model/
           │     ├── EpicTest.java
           │     ├── SubtaskTest.java
           │     └── TaskTest.java
```

---

## **How to Run**

1. Open the project in IntelliJ IDEA or any Java IDE.  
2. Run the `Main` class located in `src/model/Main.java`.  
3. View the console output to observe task creation and manager behavior.

---

## **Learning Objectives**

This project demonstrates:

- Understanding of OOP principles  
- Working with inheritance and composition  
- Designing interfaces and abstractions  
- Managing collections and object relationships  
- Writing unit tests for core logic

---

## **Author**

Developed as part of the **Yandex Practicum Java Developer** course.

---

If you'd like, I can also prepare a **Russian version**, or add **badges for code coverage, license, or build status** to make the repository look even more polished.
