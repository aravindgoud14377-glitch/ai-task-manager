package com.taskmanager.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskmanager.model.Task;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ClaudeAIService {

    @Value("${claude.api.key}")
    private String apiKey;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String CLAUDE_API_URL = "https://api.anthropic.com/v1/messages";
    private static final String MODEL = "claude-sonnet-4-20250514";

    public String analyzeTasks(List<Task> tasks) {
        try {
            String taskSummary = tasks.stream()
                    .map(t -> String.format("- [%s] %s (Priority: %s, Status: %s, Deadline: %s)",
                            t.getId(), t.getTitle(), t.getPriority(), t.getStatus(),
                            t.getDeadline() != null ? t.getDeadline().toString() : "No deadline"))
                    .collect(Collectors.joining("\n"));

            String prompt = """
                    You are a productivity assistant. Analyze the following task list and provide:
                    1. A brief overall productivity assessment (1-2 sentences)
                    2. Top 3 tasks to focus on RIGHT NOW (with reasons)
                    3. Any tasks that seem overdue or at risk
                    4. One actionable tip to improve productivity

                    Tasks:
                    %s

                    Keep your response concise, clear, and motivating. Use bullet points.
                    """.formatted(taskSummary);

            Map<String, Object> requestBody = Map.of(
                    "model", MODEL,
                    "max_tokens", 1000,
                    "messages", List.of(
                            Map.of("role", "user", "content", prompt)));

            String requestJson = objectMapper.writeValueAsString(requestBody);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(CLAUDE_API_URL))
                    .header("Content-Type", "application/json")
                    .header("x-api-key", apiKey)
                    .header("anthropic-version", "2023-06-01")
                    .POST(HttpRequest.BodyPublishers.ofString(requestJson))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Map<?, ?> responseMap = objectMapper.readValue(response.body(), Map.class);
                List<?> content = (List<?>) responseMap.get("content");
                if (content != null && !content.isEmpty()) {
                    Map<?, ?> firstBlock = (Map<?, ?>) content.get(0);
                    return (String) firstBlock.get("text");
                }
            }

            return "Unable to get AI analysis at this time. Status: " + response.statusCode();

        } catch (Exception e) {
            return "AI analysis failed: " + e.getMessage();
        }
    }

    public String breakdownTask(Task task) {
        try {
            String prompt = """
                    You are a productivity assistant. Break down the following task into clear, actionable subtasks.

                    Task: %s
                    Description: %s
                    Priority: %s
                    Deadline: %s

                    Provide:
                    1. 3-5 concrete subtasks to complete this task
                    2. Estimated time for each subtask
                    3. A suggested order to tackle them

                    Keep it practical and concise.
                    """.formatted(
                    task.getTitle(),
                    task.getDescription() != null ? task.getDescription() : "No description provided",
                    task.getPriority(),
                    task.getDeadline() != null ? task.getDeadline().toString() : "No deadline");

            Map<String, Object> requestBody = Map.of(
                    "model", MODEL,
                    "max_tokens", 800,
                    "messages", List.of(
                            Map.of("role", "user", "content", prompt)));

            String requestJson = objectMapper.writeValueAsString(requestBody);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(CLAUDE_API_URL))
                    .header("Content-Type", "application/json")
                    .header("x-api-key", apiKey)
                    .header("anthropic-version", "2023-06-01")
                    .POST(HttpRequest.BodyPublishers.ofString(requestJson))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Map<?, ?> responseMap = objectMapper.readValue(response.body(), Map.class);
                List<?> content = (List<?>) responseMap.get("content");
                if (content != null && !content.isEmpty()) {
                    Map<?, ?> firstBlock = (Map<?, ?>) content.get(0);
                    return (String) firstBlock.get("text");
                }
            }

            return "Unable to break down task at this time.";

        } catch (Exception e) {
            return "Task breakdown failed: " + e.getMessage();
        }
    }
}