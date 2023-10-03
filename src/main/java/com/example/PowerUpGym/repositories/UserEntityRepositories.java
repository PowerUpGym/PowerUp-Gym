package com.example.PowerUpGym.repositories;

import com.example.PowerUpGym.entity.users.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserEntityRepositories extends JpaRepository<UserEntity, Long> {

    UserEntity findByUsername(String username);
}
