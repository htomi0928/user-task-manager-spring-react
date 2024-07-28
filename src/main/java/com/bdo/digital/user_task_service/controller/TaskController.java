package com.bdo.digital.user_task_service.controller;

import com.bdo.digital.user_task_service.dto.CreateTaskDTO;
import com.bdo.digital.user_task_service.model.Task;
import com.bdo.digital.user_task_service.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for managing task-related HTTP requests.
 * Provides endpoints for creating, retrieving, updating, and deleting tasks.
 */
@RestController
@RequestMapping("api/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;

    /**
     * Handles the creation of a new task.
     *
     * @param createTaskDTO Data Transfer Object containing task details.
     * @return ResponseEntity with the created {@link Task} and HTTP status 201 (Created) if successful,
     * or an error message with HTTP status 500 (Internal Server Error) if an exception occurs.
     */
    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody CreateTaskDTO createTaskDTO) {
        try {
            Task createdTask = taskService.createTask(createTaskDTO);
            return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieves a list of all tasks that are not marked as deleted.
     *
     * @return ResponseEntity containing a list of active {@link Task} entities and HTTP status 200 (OK).
     */
    @GetMapping
    public ResponseEntity<List<Task>> getAllActiveTasks() {
        return new ResponseEntity<>(taskService.getAllActiveTasks(), HttpStatus.OK);
    }

    /**
     * Retrieves a list of all tasks, including those marked as deleted.
     *
     * @return ResponseEntity containing a list of all {@link Task} entities and HTTP status 200 (OK).
     */
    @GetMapping("/all")
    public ResponseEntity<List<Task>> getAllTasks() {
        return new ResponseEntity<>(taskService.getAllTasks(), HttpStatus.OK);
    }

    /**
     * Retrieves a task by its ID.
     *
     * @param id The ID of the task to retrieve.
     * @return ResponseEntity containing the {@link Task} if found and HTTP status 200 (OK),
     * or HTTP status 404 (Not Found) if the task does not exist.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable long id) {
        Task task = taskService.getTaskById(id);
        if (task == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(task, HttpStatus.OK);
        }
    }

    /**
     * Updates an existing task with new details provided in CreateTaskDTO.
     *
     * @param id            The ID of the task to update.
     * @param createTaskDTO Data Transfer Object containing updated task details.
     * @return ResponseEntity with a success message and HTTP status 200 (OK) if successful,
     * or an error message with HTTP status 400 (Bad Request) if the update fails.
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateTask(@PathVariable long id, @RequestBody CreateTaskDTO createTaskDTO) {
        Task updatedTask = taskService.updateTask(id, createTaskDTO);
        if (updatedTask == null) {
            return new ResponseEntity<>("Failed to update", HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>("Updated", HttpStatus.OK);
        }
    }

    /**
     * Soft deletes a task by marking it as deleted.
     *
     * @param id The ID of the task to soft delete.
     * @return ResponseEntity with a success message and HTTP status 200 (OK) if the task was successfully soft deleted,
     * or an error message with HTTP status 404 (Not Found) if the task was not found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable long id) {
        boolean deleted = taskService.softDeleteTask(id);
        if (deleted) {
            return new ResponseEntity<>("Deleted", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Task not found", HttpStatus.NOT_FOUND);
        }
    }
}
