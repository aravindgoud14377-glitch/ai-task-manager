package com.taskmanager;

import com.taskmanager.model.Task;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateTask() {
        Task task = new Task();
        task.setTitle("Test Task");
        task.setPriority(Task.Priority.HIGH);

        when(taskRepository.save(task)).thenReturn(task);

        Task result = taskService.createTask(task);
        assertNotNull(result);
        assertEquals("Test Task", result.getTitle());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void shouldReturnAllTasks() {
        Task t1 = new Task();
        t1.setTitle("Task 1");
        Task t2 = new Task();
        t2.setTitle("Task 2");

        when(taskRepository.findByOrderByCreatedAtDesc()).thenReturn(List.of(t1, t2));

        List<Task> tasks = taskService.getAllTasks();
        assertEquals(2, tasks.size());
    }

    @Test
    void shouldDeleteExistingTask() {
        when(taskRepository.existsById(1L)).thenReturn(true);
        doNothing().when(taskRepository).deleteById(1L);

        boolean result = taskService.deleteTask(1L);
        assertTrue(result);
        verify(taskRepository).deleteById(1L);
    }

    @Test
    void shouldReturnFalseForNonExistingTask() {
        when(taskRepository.existsById(99L)).thenReturn(false);
        boolean result = taskService.deleteTask(99L);
        assertFalse(result);
    }

    @Test
    void shouldUpdateTaskStatus() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Test");
        task.setStatus(Task.Status.PENDING);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any())).thenReturn(task);

        Optional<Task> updated = taskService.updateStatus(1L, Task.Status.COMPLETED);
        assertTrue(updated.isPresent());
        assertEquals(Task.Status.COMPLETED, updated.get().getStatus());
    }
}