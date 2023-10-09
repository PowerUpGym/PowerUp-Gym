package com.example.PowerUpGym.services.notification;

import com.example.PowerUpGym.entity.notifications.NotificationsEntity;
import java.util.List;

public interface NotificationsService {
    void saveNotification(NotificationsEntity notification);
    List<NotificationsEntity> getNotificationsForPlayer(Long playerId);
}
