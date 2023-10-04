package com.example.PowerUpGym.services;

import com.example.PowerUpGym.entity.notifications.NotificationsEntity;
import com.example.PowerUpGym.entity.users.PlayersEntity;
import com.example.PowerUpGym.entity.users.UserEntity;
import com.example.PowerUpGym.repositories.NotificationsEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationsService {
    @Autowired
    private NotificationsEntityRepository notificationsEntityRepository;

    public void saveNotification(NotificationsEntity notification) {
        notificationsEntityRepository.save(notification);
    }



    public List<NotificationsEntity> getNotificationsForPlayer(Long playerId) {
        return notificationsEntityRepository.findByReceiverId(playerId);
    }
}
