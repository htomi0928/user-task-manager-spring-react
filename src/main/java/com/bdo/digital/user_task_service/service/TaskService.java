package com.bdo.digital.user_task_service.service;

import com.bdo.digital.user_task_service.dto.CreateTaskDTO;
import com.bdo.digital.user_task_service.model.Task;
import com.bdo.digital.user_task_service.model.User;
import com.bdo.digital.user_task_service.repository.TaskRepository;
import com.bdo.digital.user_task_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing task-related operations.
 * Provides methods for creating, retrieving, updating, and deleting tasks.
 */
@Service
public class TaskService {
    @Autowired
    TaskRepository taskRepository;

    @Autowired
    UserRepository userRepository;

    /**
     * Creates a new task based on the provided CreateTaskDTO.
     *
     * @param createTaskDTO Data Transfer Object containing task details.
     * @return The created {@link Task} entity.
     * @throws RuntimeException if the specified user is not found.
     */
    public Task createTask(CreateTaskDTO createTaskDTO) {
        User user = userRepository.findById(createTaskDTO.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        Task task = new Task();
        task.setTitle(createTaskDTO.getTitle());
        task.setDescription(createTaskDTO.getDescription());
        task.setDeleted(createTaskDTO.isDeleted());
        task.setUser(user);
        return taskRepository.save(task);
    }

    /**
     * Retrieves a task by its ID.
     *
     * @param id The ID of the task to retrieve.
     * @return The {@link Task} entity with the given ID, or null if not found.
     */
    public Task getTaskById(long id) {
        return taskRepository.findById(id).orElse(null);
    }

    /**
     * Retrieves all tasks that are not marked as deleted.
     *
     * @return A list of active {@link Task} entities.
     */
    public List<Task> getAllActiveTasks() {
        return taskRepository.findAllActiveTasks();
    }

    /**
     * Retrieves all tasks, including those marked as deleted.
     *
     * @return A list of all {@link Task} entities.
     */
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    /**
     * Updates an existing task with new details provided in CreateTaskDTO.
     *
     * @param id            The ID of the task to update.
     * @param createTaskDTO Data Transfer Object containing updated task details.
     * @return The updated {@link Task} entity, or null if the task does not exist.
     * @throws RuntimeException if the specified user is not found.
     */
    public Task updateTask(long id, CreateTaskDTO createTaskDTO) {
            Task existingTask = taskRepository.findById(id).orElse(null);
            if (existingTask != null) {
                User user = userRepository.findById(createTaskDTO.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
                existingTask.setTitle(createTaskDTO.getTitle());
                existingTask.setDescription(createTaskDTO.getDescription());
                existingTask.setUser(user);
                existingTask.setDeleted(createTaskDTO.isDeleted());
                existingTask.setId(id);
            return taskRepository.save(existingTask);
        }
        return null;
    }

    /**
     * Soft deletes a task by marking it as deleted.
     *
     * @param id The ID of the task to soft delete.
     * @return True if the task was successfully soft deleted, false otherwise.
     */
    public boolean softDeleteTask(long id) {
        Task task = taskRepository.findById(id).orElse(null);
        if (task == null || task.isDeleted()) {
            return false;
        } else {
            task.setDeleted(true);
            taskRepository.save(task);
            return true;
        }
    }
}
