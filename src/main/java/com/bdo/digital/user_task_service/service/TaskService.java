package com.bdo.digital.user_task_service.service;

import com.bdo.digital.user_task_service.dto.CreateTaskDTO;
import com.bdo.digital.user_task_service.model.Task;
import com.bdo.digital.user_task_service.model.User;
import com.bdo.digital.user_task_service.repository.TaskRepository;
import com.bdo.digital.user_task_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    @Autowired
    TaskRepository taskRepository;

    @Autowired
    UserRepository userRepository;

    public Task createTask(CreateTaskDTO createTaskDTO){
        User user = userRepository.findById(createTaskDTO.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        Task task = new Task();
        task.setTitle(createTaskDTO.getTitle());
        task.setDescription(createTaskDTO.getDescription());
        task.setDeleted(createTaskDTO.isDeleted());
        task.setUser(user);
        return taskRepository.save(task);
    }

    public Task getTaskById(long id){
        return taskRepository.findById(id).orElse(null);
    }

    public List<Task> getAllActiveTasks(){
        return taskRepository.findAllActiveTasks();
    }

    public List<Task> getAllTasks(){
        return taskRepository.findAll();
    }

    public Task updateTask(long id, CreateTaskDTO createTaskDTO) {
        if (taskRepository.existsById(id)) {
            User user = userRepository.findById(createTaskDTO.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
            Task task = new Task();
            task.setTitle(createTaskDTO.getTitle());
            task.setDescription(createTaskDTO.getDescription());
            task.setUser(user);
            task.setDeleted(createTaskDTO.isDeleted());
            task.setId(id);
            return taskRepository.save(task);
        }
        return null;
    }

    public boolean softDeleteTask(long id){
        Task task = taskRepository.findById(id).orElse(null);
        if(task == null || task.isDeleted()){
            return false;
        }else{
            task.setDeleted(true);
            taskRepository.save(task);
            return true;
        }
    }
}
