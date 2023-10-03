package com.example.PowerUpGym.services;

import com.example.PowerUpGym.entity.classesGym.ClassesEntity;
import com.example.PowerUpGym.entity.users.UserEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AddUserToClassService {

    private final UserService userService;
    private final ClassService classService;

    public AddUserToClassService(UserService userService, ClassService classService) {
        this.userService = userService;
        this.classService = classService;
    }

    @Transactional
    public void addUserToClass(Long userId, Long classId) {
        // Retrieve the selected user and class by their IDs
        UserEntity user = userService.getUserById(userId);
        ClassesEntity classEntity = classService.getClassById(classId);

        // Add player to class
        classEntity.getEnrolledPlayers().add(user.getPlayer());

        // Add class to player (if necessary)
        user.getPlayer().getEnrolledClasses().add(classEntity);

        // Save the changes
        classService.addClass(classEntity);
        userService.signupUser(user); // If you made changes to the player's enrolledClasses, save the user

        // Optionally, you can throw an exception or handle errors if needed
    }

}
