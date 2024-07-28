package com.bdo.digital.user_task_service.controller;

import com.bdo.digital.user_task_service.model.User;
import com.bdo.digital.user_task_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for managing user-related HTTP requests.
 * Provides endpoints for creating, retrieving, updating, and deleting users.
 */
@RestController
@RequestMapping("api/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Handles the creation of a new user.
     *
     * @param user The user object to be created, provided in the request body.
     * @return ResponseEntity with the created user and HTTP status 201 (Created) if successful,
     * or an error message with HTTP status 500 (Internal Server Error) if an exception occurs.
     */
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            User createdUser = userService.createuser(user);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieves a list of all non-deleted users.
     *
     * @return ResponseEntity containing a list of non-deleted users and HTTP status 200 (OK).
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllActiveUsers() {
        return new ResponseEntity<>(userService.getAllActiveUsers(), HttpStatus.OK);
    }

    /**
     * Retrieves a list of all users, including those marked as deleted.
     *
     * @return ResponseEntity containing a list of all users and HTTP status 200 (OK).
     */
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id The ID of the user to retrieve, provided in the path variable.
     * @return ResponseEntity containing the user if found and HTTP status 200 (OK),
     * or HTTP status 404 (Not Found) if the user does not exist.
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable long id) {
        User user = userService.getUserById(id);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
    }

    /**
     * Updates an existing user.
     *
     * @param id   The ID of the user to update, provided in the path variable.
     * @param user The user object containing updated information, provided in the request body.
     * @return ResponseEntity with a success message and HTTP status 200 (OK) if successful,
     * or an error message with HTTP status 400 (Bad Request) if the update fails.
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable long id, @RequestBody User user) {
        User updatedUser = userService.updateUser(id, user);
        if (updatedUser == null) {
            return new ResponseEntity<>("Failed to update", HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>("Updated", HttpStatus.OK);
        }
    }

    /**
     * Soft deletes a user by marking them as deleted.
     *
     * @param id The ID of the user to soft delete, provided in the path variable.
     * @return ResponseEntity with a success message and HTTP status 200 (OK) if the user was successfully soft deleted,
     * or an error message with HTTP status 404 (Not Found) if the user was not found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable long id) {
        boolean deleted = userService.softDeleteUser(id);
        if (deleted) {
            return new ResponseEntity<>("Deleted", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }
}
