package com.example.PowerUpGym.services.classes;

import com.example.PowerUpGym.entity.classesGym.ClassesEntity;
import java.util.List;

public interface ClassService {
    void addClass(ClassesEntity classEntity);
    List<ClassesEntity> getAllClasses();
    ClassesEntity getClassById(Long id);
}
