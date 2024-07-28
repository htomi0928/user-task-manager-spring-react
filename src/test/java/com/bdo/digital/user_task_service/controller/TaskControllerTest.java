package com.bdo.digital.user_task_service.controller;

import com.bdo.digital.user_task_service.dto.CreateTaskDTO;
import com.bdo.digital.user_task_service.model.Task;
import com.bdo.digital.user_task_service.model.User;
import com.bdo.digital.user_task_service.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateTask() throws Exception {
        CreateTaskDTO createTaskDTO = new CreateTaskDTO();
        createTaskDTO.setTitle("Task Title");
        createTaskDTO.setDescription("Task Description");
        createTaskDTO.setDeleted(false);
        createTaskDTO.setUserId(1L);

        Task task = new Task();
        task.setId(1L);
        task.setTitle("Task Title");
        task.setDescription("Task Description");
        task.setDeleted(false);
        User user = new User();
        user.setId(1L);
        task.setUser(user);

        when(taskService.createTask(any(CreateTaskDTO.class))).thenReturn(task);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createTaskDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Task Title")))
                .andExpect(jsonPath("$.description", is("Task Description")));
    }

    @Test
    public void testGetAllActiveTasks() throws Exception {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Task Title");
        task.setDescription("Task Description");
        task.setDeleted(false);

        when(taskService.getAllActiveTasks()).thenReturn(Collections.singletonList(task));

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("Task Title")))
                .andExpect(jsonPath("$[0].description", is("Task Description")));
    }

    @Test
    public void testGetAllTasks() throws Exception {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Task Title");
        task.setDescription("Task Description");

        when(taskService.getAllTasks()).thenReturn(Collections.singletonList(task));

        mockMvc.perform(get("/api/tasks/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("Task Title")))
                .andExpect(jsonPath("$[0].description", is("Task Description")));
    }

    @Test
    public void testGetTaskById() throws Exception {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Task Title");
        task.setDescription("Task Description");

        when(taskService.getTaskById(anyLong())).thenReturn(task);

        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Task Title")))
                .andExpect(jsonPath("$.description", is("Task Description")));
    }

    @Test
    public void testUpdateTask() throws Exception {
        CreateTaskDTO createTaskDTO = new CreateTaskDTO();
        createTaskDTO.setTitle("Updated Task Title");
        createTaskDTO.setDescription("Updated Task Description");
        createTaskDTO.setDeleted(false);
        createTaskDTO.setUserId(1L);

        Task task = new Task();
        task.setId(1L);
        task.setTitle("Updated Task Title");
        task.setDescription("Updated Task Description");
        task.setDeleted(false);
        User user = new User();
        user.setId(1L);
        task.setUser(user);

        when(taskService.updateTask(anyLong(), any(CreateTaskDTO.class))).thenReturn(task);

        mockMvc.perform(put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createTaskDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("Updated")));
    }

    @Test
    public void testDeleteTask() throws Exception {
        when(taskService.softDeleteTask(anyLong())).thenReturn(true);

        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("Deleted")));
    }

    @Test
    public void testDeleteTaskNotFound() throws Exception {
        when(taskService.softDeleteTask(anyLong())).thenReturn(false);

        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", is("Task not found")));
    }
}
