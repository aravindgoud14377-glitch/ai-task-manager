# TaskFlow AI — AI-Powered Task Manager

![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen?style=flat-square&logo=springboot)
![Claude API](https://img.shields.io/badge/Claude%20API-Integrated-blueviolet?style=flat-square)
![License](https://img.shields.io/badge/License-MIT-blue?style=flat-square)

A full-stack task management application with AI-powered productivity insights, built with **Java Spring Boot** and the **Claude API**.

---

## ✨ Features

- **Full CRUD** — Create, update, complete, and delete tasks
- **Priority Levels** — LOW / MEDIUM / HIGH / CRITICAL with visual indicators
- **Status Tracking** — PENDING → IN_PROGRESS → COMPLETED workflow
- **Deadline Management** — Visual overdue warnings
- **AI Task Analysis** — Analyze all tasks and get personalized productivity advice via Claude
- **AI Task Breakdown** — Break any single task into step-by-step subtasks with time estimates
- **Filter Views** — Filter tasks by status
- **RESTful API** — Clean API endpoints following REST conventions
- **In-Memory DB** — H2 database (zero setup required)

---

## 🛠 Tech Stack

| Layer    | Technology                                |
| -------- | ----------------------------------------- |
| Backend  | Java 17, Spring Boot 3.2, Spring Data JPA |
| Frontend | HTML5, CSS3, Vanilla JavaScript           |
| Database | H2 In-Memory                              |
| AI       | Anthropic Claude API (claude-sonnet-4)    |
| Build    | Maven                                     |
| Testing  | JUnit 5, Mockito                          |

---

## 🚀 Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+
- Anthropic API Key ([Get one here](https://console.anthropic.com))

### 1. Clone the repository

```bash
git clone https://github.com/YOUR_USERNAME/ai-task-manager.git
cd ai-task-manager
```

### 2. Add your API Key

Open `src/main/resources/application.properties` and replace:

```properties
claude.api.key=YOUR_CLAUDE_API_KEY_HERE
```

### 3. Run the backend

```bash
mvn spring-boot:run
```

Backend starts at `http://localhost:8080`

### 4. Open the frontend

Open `frontend/index.html` directly in your browser.

---

## 📡 API Endpoints

| Method | Endpoint                       | Description              |
| ------ | ------------------------------ | ------------------------ |
| GET    | `/api/tasks`                   | Get all tasks            |
| GET    | `/api/tasks/{id}`              | Get task by ID           |
| POST   | `/api/tasks`                   | Create new task          |
| PUT    | `/api/tasks/{id}`              | Update task              |
| PATCH  | `/api/tasks/{id}/status`       | Update status only       |
| DELETE | `/api/tasks/{id}`              | Delete task              |
| POST   | `/api/tasks/ai/analyze`        | AI analyze all tasks     |
| POST   | `/api/tasks/{id}/ai/breakdown` | AI breakdown single task |
| GET    | `/api/tasks/status/{status}`   | Filter by status         |

---

## 🧪 Running Tests

```bash
mvn test
```

---

## 📂 Project Structure

```
ai-task-manager/
├── src/
│   ├── main/java/com/taskmanager/
│   │   ├── TaskManagerApplication.java
│   │   ├── controller/TaskController.java
│   │   ├── model/Task.java
│   │   ├── repository/TaskRepository.java
│   │   └── service/
│   │       ├── TaskService.java
│   │       └── ClaudeAIService.java
│   └── resources/
│       └── application.properties
├── src/test/java/com/taskmanager/
│   └── TaskServiceTest.java
├── frontend/
│   └── index.html
├── pom.xml
└── README.md
```

---

## 🖥 Screenshots

> Add screenshots here after running the app!

---

## 🔮 Future Improvements

- [ ] User authentication (Spring Security + JWT)
- [ ] PostgreSQL / MySQL support
- [ ] Email reminders for deadlines
- [ ] Task categories and tags
- [ ] Drag-and-drop Kanban board view
- [ ] Mobile responsive PWA

---

## 👨‍💻 Author

**Aravind** — B.Tech CSE @ CMR Engineering College  
[GitHub](https://github.com/YOUR_USERNAME) · [LinkedIn](https://linkedin.com/in/YOUR_PROFILE)

---

## 📄 License

MIT License — feel free to use and modify.
