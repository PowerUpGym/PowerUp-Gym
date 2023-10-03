package com.example.PowerUpGym.repositories;

import com.example.PowerUpGym.entity.classesGym.ClassesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClassesEntityRepository extends JpaRepository<ClassesEntity,Long> {
    Optional<ClassesEntity> findById(Long id);
}
