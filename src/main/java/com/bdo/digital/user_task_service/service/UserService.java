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

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User createuser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User getUserById(long id){
        return userRepository.findById(id).orElse(null);
    }

    public List<User> getAllActiveUsers(){
        return userRepository.findAllActiveUsers();
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User updateUser(long id, User updatedUser) {
        User user = userRepository.findById(id).orElse(null);
        if(user != null){
            user.setName(updatedUser.getName());
            user.setEmail(updatedUser.getEmail());
            user.setPassword(updatedUser.getPassword());
            user.setAddress(updatedUser.getAddress());
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            // Merge tasks
            if (updatedUser.getTasks() != null) {
                Map<Long, Task> existingTasksMap = user.getTasks().stream()
                        .collect(Collectors.toMap(Task::getId, task -> task));

                for (Task task : updatedUser.getTasks()) {
                    if (existingTasksMap.containsKey(task.getId())) {
                        Task existingTask = existingTasksMap.get(task.getId());
                        existingTask.setTitle(task.getTitle());
                        existingTask.setDescription(task.getDescription());
                        existingTask.setDeleted(task.isDeleted());
                    } else {
                        user.getTasks().add(task);
                    }
                }
            }
            return userRepository.save(user);
        }
        else{
            return null;
        }
    }

    public boolean softDeleteUser(long id){
       User user = userRepository.findById(id).orElse(null);
       if(user == null || user.isDeleted()){
           return false;
       }else{
           user.setDeleted(true);
           for (Task task : user.getTasks()) {
               task.setDeleted(true);
           }
           userRepository.save(user);
           return true;
       }
    }
}
