package com.example.PowerUpGym.services.trainer;

import com.example.PowerUpGym.entity.classesGym.ClassesEntity;
import com.example.PowerUpGym.entity.users.TrainerEntity;
import com.example.PowerUpGym.entity.users.UserEntity;
import com.example.PowerUpGym.repositories.ClassesEntityRepository;
import com.example.PowerUpGym.repositories.TrainerEntityRepository;
import com.example.PowerUpGym.repositories.UserEntityRepositories;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainerServiceImp implements TrainerService {

    private final TrainerEntityRepository trainerEntityRepository;
    private final UserEntityRepositories userEntityRepositories;
    private final ClassesEntityRepository classesEntityRepository;
    public TrainerServiceImp(TrainerEntityRepository trainerEntityRepository, UserEntityRepositories userEntityRepositories, ClassesEntityRepository classesEntityRepository) {
        this.trainerEntityRepository = trainerEntityRepository;
        this.userEntityRepositories = userEntityRepositories;
        this.classesEntityRepository = classesEntityRepository;
    }

    @Override
    public List<TrainerEntity> getAllTrainer() {
        return trainerEntityRepository.findAll();
    }

    @Override
    public TrainerEntity signupTrainer(TrainerEntity trainerEntity) {
        trainerEntityRepository.save(trainerEntity);
        return trainerEntity;
    }

    @Override
    public UserEntity findUserByUsername(String username){
        return userEntityRepositories.findByUsername(username);    }


    @Override
    public List<ClassesEntity> getClassesForTrainer(TrainerEntity trainer){
        return trainer.getOwnedClasses();
    }

    @Override
    public ClassesEntity getClassDetails(Long classId) {
        Optional<ClassesEntity> optionalClass = classesEntityRepository.findById(classId);

        if (optionalClass.isPresent()) {
            return optionalClass.get();
        } else {
            throw new RuntimeException("Class not found with ID: " + classId);
        }
    }

    @Override
    public TrainerEntity getTrainerById(Long id) {
        return trainerEntityRepository.findById(id).orElse(null);
    }

    @Override
    public void updateTrainerInfo(UserEntity userEntity) {
        TrainerEntity trainerEntity = userEntity.getTrainer();

        if (trainerEntity != null) {
            trainerEntity.setAge(userEntity.getTrainer().getAge());
            trainerEntity.setExperience(userEntity.getTrainer().getExperience());

            trainerEntityRepository.save(trainerEntity);
        }
    }

}
