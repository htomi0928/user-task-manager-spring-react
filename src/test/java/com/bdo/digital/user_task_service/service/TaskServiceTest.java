package com.bdo.digital.user_task_service.service;

import com.bdo.digital.user_task_service.dto.CreateTaskDTO;
import com.bdo.digital.user_task_service.model.Task;
import com.bdo.digital.user_task_service.model.User;
import com.bdo.digital.user_task_service.repository.TaskRepository;
import com.bdo.digital.user_task_service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateTask() {
        User user = new User();
        user.setId(1L);

        CreateTaskDTO createTaskDTO = new CreateTaskDTO();
        createTaskDTO.setTitle("Test Task");
        createTaskDTO.setDescription("Test Description");
        createTaskDTO.setDeleted(false);
        createTaskDTO.setUserId(1L);

        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setDeleted(false);
        task.setUser(user);

        // Mock the user repository to return the user
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        // Mock the task repository save method
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task createdTask = taskService.createTask(createTaskDTO);

        assertNotNull(createdTask);
        assertEquals("Test Task", createdTask.getTitle());
        assertEquals("Test Description", createdTask.getDescription());
        assertFalse(createdTask.isDeleted());
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    public void testGetTaskById() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");

        // Mock the task repository findById method
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));

        Task foundTask = taskService.getTaskById(1L);

        assertNotNull(foundTask);
        assertEquals("Test Task", foundTask.getTitle());
    }

    @Test
    public void testGetTaskByIdNotFound() {
        // Mock the task repository findById method
        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());

        Task foundTask = taskService.getTaskById(1L);

        assertNull(foundTask);
    }

    @Test
    public void testGetAllActiveTasks() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDeleted(false);

        // Mock the task repository findAllActiveTasks method
        when(taskRepository.findAllActiveTasks()).thenReturn(Collections.singletonList(task));

        List<Task> tasks = taskService.getAllActiveTasks();

        assertNotNull(tasks);
        assertFalse(tasks.isEmpty());
        assertEquals(1, tasks.size());
        assertEquals("Test Task", tasks.get(0).getTitle());
    }

    @Test
    public void testGetAllTasks() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");

        // Mock the task repository findAll method
        when(taskRepository.findAll()).thenReturn(Collections.singletonList(task));

        List<Task> tasks = taskService.getAllTasks();

        assertNotNull(tasks);
        assertFalse(tasks.isEmpty());
        assertEquals(1, tasks.size());
        assertEquals("Test Task", tasks.get(0).getTitle());
    }

    @Test
    public void testUpdateTask() {
        Task existingTask = new Task();
        existingTask.setId(1L);
        existingTask.setTitle("Old Title");
        existingTask.setDescription("Old Description");
        existingTask.setDeleted(false);

        CreateTaskDTO updateDTO = new CreateTaskDTO();
        updateDTO.setTitle("Updated Title");
        updateDTO.setDescription("Updated Description");
        updateDTO.setDeleted(false);
        updateDTO.setUserId(1L);

        User user = new User();
        user.setId(1L);

        Task updatedTask = new Task();
        updatedTask.setId(1L);
        updatedTask.setTitle("Updated Title");
        updatedTask.setDescription("Updated Description");
        updatedTask.setDeleted(false);
        updatedTask.setUser(user);

        // Mock the task repository findById method
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(existingTask));

        // Mock the user repository findById method
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        // Mock the task repository save method
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        Task result = taskService.updateTask(1L, updateDTO);

        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        assertEquals("Updated Description", result.getDescription());
        assertFalse(result.isDeleted());
    }

    @Test
    public void testUpdateTaskNotFound() {
        CreateTaskDTO updateDTO = new CreateTaskDTO();
        updateDTO.setTitle("Updated Title");
        updateDTO.setDescription("Updated Description");
        updateDTO.setDeleted(false);
        updateDTO.setUserId(1L);

        // Mock the task repository findById method
        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());

        Task result = taskService.updateTask(1L, updateDTO);

        assertNull(result);
    }

    @Test
    public void testSoftDeleteTask() {
        Task task = new Task();
        task.setId(1L);
        task.setDeleted(false);

        // Mock the task repository findById method
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));

        // Mock the task repository save method
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        boolean result = taskService.softDeleteTask(1L);

        assertTrue(result);
        assertTrue(task.isDeleted());
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    public void testSoftDeleteTaskNotFound() {
        // Mock the task repository findById method
        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());

        boolean result = taskService.softDeleteTask(1L);

        assertFalse(result);
    }
}
