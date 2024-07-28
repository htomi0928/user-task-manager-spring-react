package com.bdo.digital.user_task_service.repository;

import com.bdo.digital.user_task_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository interface for managing {@link User} entities.
 * Provides methods for performing CRUD operations on the User table.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Custom query method to retrieve all users who are not marked as deleted.
     *
     * @return A list of active {@link User} entities (users who are not deleted).
     */
    @Query("SELECT u FROM User u WHERE u.deleted = false")
    List<User> findAllActiveUsers();
}
