package com.example.PowerUpGym.services;

import com.example.PowerUpGym.entity.classesGym.ClassesEntity;
import com.example.PowerUpGym.entity.users.PlayersEntity;
import com.example.PowerUpGym.entity.users.TrainerEntity;
import com.example.PowerUpGym.entity.users.UserEntity;
import com.example.PowerUpGym.repositories.ClassesEntityRepository;
import com.example.PowerUpGym.repositories.TrainerEntityRepository;
import com.example.PowerUpGym.repositories.UserEntityRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainerService {

    private final TrainerEntityRepository trainerEntityRepository;

    private final UserEntityRepositories userEntityRepositories;

    @Autowired
    private ClassesEntityRepository classesEntityRepository;

    public TrainerService(TrainerEntityRepository trainerEntityRepository,UserEntityRepositories userEntityRepositories) {
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

    public UserEntity findUserByUsername(String username){
        return userEntityRepositories.findByUsername(username);
    }

    public List<ClassesEntity> getClassesForTrainer(TrainerEntity trainer){
        return trainer.getOwnedClasses();
    }

    public ClassesEntity getClassDetails(Long classId) {
        Optional<ClassesEntity> optionalClass = classesEntityRepository.findById(classId);

        if (optionalClass.isPresent()) {
            return optionalClass.get();
        } else {
            throw new RuntimeException("Class not found with ID: " + classId);
        }
    }

    public void updateTrainer(TrainerEntity trainer) {
        trainerEntityRepository.save(trainer);
    }

    public TrainerEntity getTrainerById(Long id) {
        return trainerEntityRepository.findById(id).orElse(null);
    }
}
