package com.example.PowerUpGym.services;

import com.example.PowerUpGym.entity.classesGym.ClassesEntity;
import com.example.PowerUpGym.entity.users.PlayersEntity;
import com.example.PowerUpGym.entity.users.TrainerEntity;
import com.example.PowerUpGym.entity.users.UserEntity;
import com.example.PowerUpGym.repositories.TrainerEntityRepository;
import com.example.PowerUpGym.repositories.UserEntityRepositories;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainerService {

    private final TrainerEntityRepository trainerEntityRepository;

    private final UserEntityRepositories userEntityRepositories;

    public TrainerService(TrainerEntityRepository trainerEntityRepository, UserEntityRepositories userEntityRepositories) {
        this.trainerEntityRepository = trainerEntityRepository;
        this.userEntityRepositories = userEntityRepositories;
    }

    public List<TrainerEntity> getAllTrainer() {
        return trainerEntityRepository.findAll();
    }

    public TrainerEntity signupTrainer(TrainerEntity trainerEntity) {
        trainerEntityRepository.save(trainerEntity);
        return trainerEntity;
    }

    public UserEntity findUserByUsername(String username) {
        return userEntityRepositories.findByUsername(username);
    }

    public List<ClassesEntity> getClassesForTrainer(TrainerEntity trainer) {
        return trainer.getOwnedClasses();
    }
}
