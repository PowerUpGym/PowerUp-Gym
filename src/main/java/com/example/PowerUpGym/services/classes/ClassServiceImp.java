package com.example.PowerUpGym.services.classes;

import com.example.PowerUpGym.entity.classesGym.ClassesEntity;
import com.example.PowerUpGym.repositories.ClassesEntityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassServiceImp implements ClassService {
    private final ClassesEntityRepository classesEntityRepository;

    public ClassServiceImp(ClassesEntityRepository classesEntityRepository) {
        this.classesEntityRepository = classesEntityRepository;
    }

    @Override
    public void addClass(ClassesEntity classEntity) {
        classesEntityRepository.save(classEntity);
    }

    @Override
    public List<ClassesEntity> getAllClasses() {
        return classesEntityRepository.findAll();
    }

    @Override
    public ClassesEntity getClassById(Long id) {
        return classesEntityRepository.findById(id).orElseThrow(()->new RuntimeException("Class not found for this ID : " + id));
    }
}
