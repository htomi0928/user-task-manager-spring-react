package com.bdo.digital.user_task_service.repository;

import com.bdo.digital.user_task_service.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("SELECT t FROM Task t WHERE t.deleted = false")
    List<Task> findAllActiveTasks();
}
