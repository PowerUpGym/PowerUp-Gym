package com.example.PowerUpGym.repositories;

import com.example.PowerUpGym.entity.users.PlayersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerEntityRepository extends JpaRepository<PlayersEntity,Long> {
}
