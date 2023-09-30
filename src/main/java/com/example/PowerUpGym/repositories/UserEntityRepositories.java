package com.example.PowerUpGym.repositories;

import com.example.PowerUpGym.entity.users.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserEntityRepositories extends JpaRepository<UserEntity,Long> {
    UserEntity findByUsername(String username);
}
