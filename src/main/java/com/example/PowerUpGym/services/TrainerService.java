package com.example.PowerUpGym.services;

import com.example.PowerUpGym.entity.users.PlayersEntity;
import com.example.PowerUpGym.entity.users.TrainerEntity;
import com.example.PowerUpGym.repositories.TrainerEntityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainerService {

    private final TrainerEntityRepository trainerEntityRepository;

    public TrainerService(TrainerEntityRepository trainerEntityRepository) {
        this.trainerEntityRepository = trainerEntityRepository;
    }


    public List<TrainerEntity> getAllTrainer() {
        return trainerEntityRepository.findAll();
    }

    public TrainerEntity signupTrainer(TrainerEntity trainerEntity) {
        trainerEntityRepository.save(trainerEntity);
        return trainerEntity;
    }

    public TrainerEntity getTrainerById(Long id) {
        return trainerEntityRepository.findById(id).orElse(null);
    }
}
