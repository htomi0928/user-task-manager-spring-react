package com.bdo.digital.user_task_service.repository;

import com.bdo.digital.user_task_service.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository interface for managing {@link Task} entities.
 * Provides methods for performing CRUD operations on the Task table.
 */
public interface TaskRepository extends JpaRepository<Task, Long> {

    /**
     * Custom query method to retrieve all tasks that are not marked as deleted.
     *
     * @return A list of active {@link Task} entities (tasks that are not deleted).
     */
    @Query("SELECT t FROM Task t WHERE t.deleted = false")
    List<Task> findAllActiveTasks();
}
