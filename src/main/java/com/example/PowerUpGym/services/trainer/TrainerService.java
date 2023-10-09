package com.example.PowerUpGym.services.trainer;

import com.example.PowerUpGym.bo.auth.NotificationRequest;
import com.example.PowerUpGym.bo.auth.update.TrainerUpdateRequest;
import com.example.PowerUpGym.entity.classesGym.ClassesEntity;
import com.example.PowerUpGym.entity.users.TrainerEntity;
import com.example.PowerUpGym.entity.users.UserEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

public interface TrainerService{
    List<TrainerEntity> getAllTrainer();
    TrainerEntity signupTrainer(TrainerEntity trainerEntity);
    UserEntity findUserByUsername(String username);
    List<ClassesEntity> getClassesForTrainer(TrainerEntity trainer);
    ClassesEntity getClassDetails(Long classId);
    TrainerEntity getTrainerById(Long id);
    void updateTrainerInfo(UserEntity userEntity);
//    RedirectView updateTrainerProfile(TrainerUpdateRequest updateRequest);
//    RedirectView sendMessage(NotificationRequest notificationRequest, Principal principal);
//    RedirectView sendMessageToAllPlayers(@RequestParam("classId") Long classId, NotificationRequest notificationRequest, Principal principal);

}
