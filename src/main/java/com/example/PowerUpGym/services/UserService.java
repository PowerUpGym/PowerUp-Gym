package com.example.PowerUpGym.services;

import com.example.PowerUpGym.entity.users.PlayersEntity;
import com.example.PowerUpGym.entity.users.UserEntity;
import com.example.PowerUpGym.repositories.UserEntityRepositories;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserService {

    private final UserEntityRepositories userEntityRepositories;

    public UserService(UserEntityRepositories userEntityRepositories) {
        this.userEntityRepositories = userEntityRepositories;
    }


    public List<UserEntity> getAllUsers() {
        return userEntityRepositories.findAll();
    }

    public UserEntity signupUser(UserEntity user) {
        userEntityRepositories.save(user);
        return user;
    }

    public UserEntity getUserById(Long userId) {
        return userEntityRepositories.findById(userId).orElse(null);
    }

    public UserEntity findUserById(Long userId) {
        return userEntityRepositories.findById(userId).orElse(null);
    }

    public void saveUser(UserEntity user) {
        userEntityRepositories.save(user);
    }
}
