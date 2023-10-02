package com.example.PowerUpGym.services;

import com.example.PowerUpGym.entity.classesGym.ClassesEntity;
import com.example.PowerUpGym.repositories.ClassesEntityRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ClassService {
    private final ClassesEntityRepository classRepository;

    public ClassService(ClassesEntityRepository classRepository) {
        this.classRepository = classRepository;
    }

    public void addClass(ClassesEntity classEntity) {
        classRepository.save(classEntity);
    }

    public List<ClassesEntity> getAllClasses() {
        return classRepository.findAll();
    }

    public ClassesEntity getClassById(Long id) {
        return classRepository.findById(id).orElse(null);
    }

}
