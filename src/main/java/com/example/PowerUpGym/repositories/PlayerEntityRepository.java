package com.example.PowerUpGym.repositories;

import com.example.PowerUpGym.entity.users.PlayersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerEntityRepository extends JpaRepository<PlayersEntity,Long> {
    List<PlayersEntity> findByUserUsernameContainingIgnoreCaseOrUserPhoneNumberContainingIgnoreCase(String username, String phoneNumber);

}
