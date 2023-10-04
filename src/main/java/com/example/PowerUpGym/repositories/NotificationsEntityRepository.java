package com.example.PowerUpGym.repositories;

import com.example.PowerUpGym.entity.notifications.NotificationsEntity;
import com.example.PowerUpGym.entity.packagesGym.PackagesEntity;
import com.example.PowerUpGym.entity.users.PlayersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationsEntityRepository extends JpaRepository<NotificationsEntity, Long> {
    List<NotificationsEntity> findByReceiverId(Long playerId);
}

