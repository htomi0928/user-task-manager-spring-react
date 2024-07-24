package com.bdo.digital.user_task_service.repository;

import com.bdo.digital.user_task_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
