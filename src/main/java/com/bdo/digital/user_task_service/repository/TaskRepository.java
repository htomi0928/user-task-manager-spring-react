package com.bdo.digital.user_task_service.repository;

import com.bdo.digital.user_task_service.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
