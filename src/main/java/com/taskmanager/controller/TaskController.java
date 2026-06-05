package com.taskmanager.controller;

import com.taskmanager.model.Task;
import com.taskmanager.service.ClaudeAIService;
import com.taskmanager.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private ClaudeAIService claudeAIService;

    // GET all tasks
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    // GET task by ID
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST create task
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task created = taskService.createTask(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // PUT update task
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task) {
        return taskService.updateTask(id, task)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PATCH update status only
    @PatchMapping("/{id}/status")
    public ResponseEntity<Task> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        try {
            Task.Status status = Task.Status.valueOf(body.get("status").toUpperCase());
            return taskService.updateStatus(id, status)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // DELETE task
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        if (taskService.deleteTask(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // POST AI analyze all tasks
    @PostMapping("/ai/analyze")
    public ResponseEntity<Map<String, String>> analyzeTasks() {
        List<Task> tasks = taskService.getAllTasks();
        if (tasks.isEmpty()) {
            return ResponseEntity.ok(Map.of("analysis", "No tasks found. Add some tasks first!"));
        }
        String analysis = claudeAIService.analyzeTasks(tasks);
        return ResponseEntity.ok(Map.of("analysis", analysis));
    }

    // POST AI breakdown single task
    @PostMapping("/{id}/ai/breakdown")
    public ResponseEntity<Map<String, String>> breakdownTask(@PathVariable Long id) {
        return taskService.getTaskById(id)
                .map(task -> {
                    String breakdown = claudeAIService.breakdownTask(task);
                    return ResponseEntity.ok(Map.of("breakdown", breakdown));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // GET tasks by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Task>> getByStatus(@PathVariable String status) {
        try {
            Task.Status taskStatus = Task.Status.valueOf(status.toUpperCase());
            return ResponseEntity.ok(taskService.getTasksByStatus(taskStatus));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}