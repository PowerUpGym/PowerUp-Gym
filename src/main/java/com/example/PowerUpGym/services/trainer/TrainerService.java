package com.example.PowerUpGym.services.trainer;

import com.example.PowerUpGym.entity.classesGym.ClassesEntity;
import com.example.PowerUpGym.entity.users.TrainerEntity;
import com.example.PowerUpGym.entity.users.UserEntity;

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

}
