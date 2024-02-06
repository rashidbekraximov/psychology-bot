package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    public void save(User user){
            userRepository.save(user);
    }



    public boolean getUserById(long id){
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()){
            return false;
        }else{
            return true;
        }
    }

    public List<User> getUserByRegion(String region){
        return userRepository.findAllByRegion(region);
    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public User getById(long id){
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }
}
