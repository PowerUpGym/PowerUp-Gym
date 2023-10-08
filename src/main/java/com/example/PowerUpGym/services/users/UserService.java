package com.example.PowerUpGym.services.users;

import com.example.PowerUpGym.entity.users.UserEntity;
import java.util.List;

public interface UserService {

    List<UserEntity> getAllUsers();

    UserEntity signupUser(UserEntity user);

    UserEntity getUserById(Long userId);

    UserEntity findUserById(Long userId);
    UserEntity findUserByUsername(String senderUsername);
    void saveUser(UserEntity user);
}
