package com.example.PowerUpGym.repositories;

import com.example.PowerUpGym.entity.classesGym.ClassesEntity;
import com.example.PowerUpGym.entity.users.TrainerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrainerEntityRepository extends JpaRepository<TrainerEntity,Long> {
//    Optional<ClassesEntity> findById(Long id);
}
