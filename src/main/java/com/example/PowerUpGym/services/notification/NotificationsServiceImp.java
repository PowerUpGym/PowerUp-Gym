package com.example.PowerUpGym.services.notification;

import com.example.PowerUpGym.entity.notifications.NotificationsEntity;
import com.example.PowerUpGym.repositories.NotificationsEntityRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NotificationsServiceImp implements NotificationsService{
    private final NotificationsEntityRepository notificationsEntityRepository;
    public NotificationsServiceImp(NotificationsEntityRepository notificationsEntityRepository) {
        this.notificationsEntityRepository = notificationsEntityRepository;
    }
    @Override
    public void saveNotification(NotificationsEntity notification) {
        notificationsEntityRepository.save(notification);
    }
    @Override
    public List<NotificationsEntity> getNotificationsForPlayer(Long playerId) {
        return notificationsEntityRepository.findByReceiverId(playerId);
    }
}
