package com.bdo.digital.user_task_service.service;

import com.bdo.digital.user_task_service.model.Task;
import com.bdo.digital.user_task_service.model.User;
import com.bdo.digital.user_task_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public User updateUser(long id, User user) {
        if (userRepository.existsById(id)) {
            user.setId(id);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        }
        return null;
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
