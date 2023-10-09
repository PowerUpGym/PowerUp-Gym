package com.example.PowerUpGym.services.users;

import com.example.PowerUpGym.entity.users.UserEntity;
import com.example.PowerUpGym.repositories.UserEntityRepositories;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImp implements UserService{

    private final UserEntityRepositories userEntityRepositories;
    public UserServiceImp(UserEntityRepositories userEntityRepositories) {
        this.userEntityRepositories = userEntityRepositories;
    }

    @Override
    public List<UserEntity> getAllUsers() {
        return userEntityRepositories.findAll();
    }

    @Override
    public UserEntity signupUser(UserEntity user) {
        userEntityRepositories.save(user);
        return user;
    }

    @Override
    public UserEntity getUserById(Long userId) {
        Optional<UserEntity> userOptional = userEntityRepositories.findById(userId);
        return userOptional.orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }

    @Override
    public UserEntity findUserById(Long userId) {
        return userEntityRepositories.findById(userId).orElse(null);
    }

    @Override
    public void saveUser(UserEntity user) {
        userEntityRepositories.save(user);
    }

    @Override
    public UserEntity findUserByUsername(String senderUsername) {
        return userEntityRepositories.findByUsername(senderUsername);
    }

}
