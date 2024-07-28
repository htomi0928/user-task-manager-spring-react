package com.bdo.digital.user_task_service.service;

import com.bdo.digital.user_task_service.model.Task;
import com.bdo.digital.user_task_service.model.User;
import com.bdo.digital.user_task_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service for managing user-related operations.
 * Provides methods for creating, retrieving, updating, and deleting users.
 */
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Creates a new user. The user's password is encoded before saving.
     *
     * @param user The user to be created.
     * @return The created user with an assigned ID.
     */
    public User createuser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id The ID of the user to retrieve.
     * @return The user with the given ID, or null if no user is found.
     */
    public User getUserById(long id) {
        return userRepository.findById(id).orElse(null);
    }

    /**
     * Retrieves all active users (users who are not marked as deleted).
     *
     * @return A list of non-deleted users.
     */
    public List<User> getAllActiveUsers() {
        return userRepository.findAllActiveUsers();
    }

    /**
     * Retrieves all users, including those marked as deleted.
     *
     * @return A list of all users.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Updates an existing user. The user's password is re-encoded before saving.
     *
     * @param id          The ID of the user to update.
     * @param updatedUser A user object containing the updated information.
     * @return The updated user, or null if the user does not exist.
     */
    public User updateUser(long id, User updatedUser) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setName(updatedUser.getName());
            user.setEmail(updatedUser.getEmail());
            user.setPassword(updatedUser.getPassword());
            user.setAddress(updatedUser.getAddress());
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            // Merge tasks
            if (updatedUser.getTasks() != null) {
                Map<Long, Task> existingTasksMap = user.getTasks().stream().collect(Collectors.toMap(Task::getId, task -> task));

                for (Task task : updatedUser.getTasks()) {
                    if (existingTasksMap.containsKey(task.getId())) {
                        // Update existing task
                        Task existingTask = existingTasksMap.get(task.getId());
                        existingTask.setTitle(task.getTitle());
                        existingTask.setDescription(task.getDescription());
                        existingTask.setDeleted(task.isDeleted());
                    } else {
                        // Add new task
                        user.getTasks().add(task);
                    }
                }
            }
            return userRepository.save(user);
        } else {
            return null;
        }
    }

    /**
     * Soft deletes a user by marking them and their tasks as deleted.
     *
     * @param id The ID of the user to soft delete.
     * @return True if the user was successfully soft deleted, false otherwise.
     */
    public boolean softDeleteUser(long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null || user.isDeleted()) {
            return false;
        } else {
            user.setDeleted(true);
            // Soft delete user's tasks
            for (Task task : user.getTasks()) {
                task.setDeleted(true);
            }
            userRepository.save(user);
            return true;
        }
    }
}
