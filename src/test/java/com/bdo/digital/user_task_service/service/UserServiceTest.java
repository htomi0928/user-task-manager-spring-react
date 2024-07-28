package com.bdo.digital.user_task_service.service;

import com.bdo.digital.user_task_service.model.Task;
import com.bdo.digital.user_task_service.model.User;
import com.bdo.digital.user_task_service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateUser() {
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password123");
        user.setDeleted(false);

        // Mock the password encoder
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");

        // Mock the repository save method
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.createuser(user);

        assertNotNull(createdUser);
        assertEquals("John Doe", createdUser.getName());
        assertEquals("john.doe@example.com", createdUser.getEmail());
        //assertTrue(passwordEncoder.matches("password123", createdUser.getPassword()));
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void testGetUserById() {
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");

        // Mock the repository findById method
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        User foundUser = userService.getUserById(1L);

        assertNotNull(foundUser);
        assertEquals("John Doe", foundUser.getName());
        assertEquals("john.doe@example.com", foundUser.getEmail());
    }

    @Test
    public void testGetUserByIdNotFound() {
        // Mock the repository findById method
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        User foundUser = userService.getUserById(1L);

        assertNull(foundUser);
    }

    @Test
    public void testGetAllActiveUsers() {
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setDeleted(false);

        // Mock the repository findAllActiveUsers method
        when(userRepository.findAllActiveUsers()).thenReturn(Collections.singletonList(user));

        List<User> users = userService.getAllActiveUsers();

        assertNotNull(users);
        assertFalse(users.isEmpty());
        assertEquals(1, users.size());
        assertEquals("John Doe", users.get(0).getName());
    }

    @Test
    public void testUpdateUser() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setName("John Doe");
        existingUser.setEmail("john.doe@example.com");
        existingUser.setPassword("oldPassword");
        existingUser.setDeleted(false);

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setName("Jane Doe");
        updatedUser.setEmail("jane.doe@example.com");
        updatedUser.setPassword("newPassword");
        updatedUser.setDeleted(false);

        // Mock the repository findById method
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(existingUser));

        // Mock the password encoder
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedNewPassword");

        // Mock the repository save method
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        User result = userService.updateUser(1L, updatedUser);

        assertNotNull(result);
        assertEquals("Jane Doe", result.getName());
        assertEquals("jane.doe@example.com", result.getEmail());
        //assertTrue(passwordEncoder.matches("newPassword", result.getPassword()));
    }

    @Test
    public void testSoftDeleteUser() {
        User user = new User();
        user.setId(1L);
        user.setDeleted(false);

        // Initialize tasks list
        List<Task> tasks = new ArrayList<>();

        // Add a task to the list
        Task task = new Task();
        task.setId(1L);
        task.setDeleted(false);
        tasks.add(task);

        user.setTasks(tasks);

        // Mock the repository findById method
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        // Mock the repository save method
        when(userRepository.save(any(User.class))).thenReturn(user);

        boolean result = userService.softDeleteUser(1L);

        assertTrue(result);
        assertTrue(user.isDeleted());

        // Verify that task is also soft deleted
        assertTrue(user.getTasks().get(0).isDeleted());

        verify(userRepository).save(any(User.class));
    }

    @Test
    public void testSoftDeleteUserNotFound() {
        // Mock the repository findById method
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        boolean result = userService.softDeleteUser(1L);

        assertFalse(result);
    }
}
