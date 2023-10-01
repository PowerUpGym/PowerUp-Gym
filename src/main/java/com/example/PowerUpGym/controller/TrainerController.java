package com.example.PowerUpGym.controller;

import com.example.PowerUpGym.entity.users.TrainerEntity;
import com.example.PowerUpGym.entity.users.UserEntity;
import com.example.PowerUpGym.entity.users.UserRoleEntity;
import com.example.PowerUpGym.services.TrainerService;
import com.example.PowerUpGym.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@Controller
public class TrainerController {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    TrainerService trainerService;

    @Autowired
    UserService userService;
//    @GetMapping("/loginTrainer")
//    public String getLoginTrainer(){
//        return "loginTrainer.html";
//    }

    @GetMapping("/signupTrainer")
    public String getSignupTrainer(){
        return "signupTrainer.html";
    }

    @PostMapping("/signupTrainer")
    public RedirectView getSignupTrainer(String fullName, String username, String password, String email, String phoneNumber,int age , String experience){

        TrainerEntity trainerEntity = new TrainerEntity();

        UserEntity user = new UserEntity();
        user.setFullName(fullName);
        user.setUsername(username);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        String encryptedPassword = passwordEncoder.encode(password);
        user.setPassword(encryptedPassword);

        UserRoleEntity trainerRole = new UserRoleEntity();
        trainerRole.setId(2L); // Set the ID of the "Trainer" role
        user.setRole(trainerRole);

        userService.signupUser(user);

        trainerEntity.setAge(age);
        trainerEntity.setExperience(experience);
        trainerEntity.setUser(user);

        trainerService.signupTrainer(trainerEntity);
        authWithHttpServletRequest(username , password);

        return new RedirectView("/index");
    }


    public void authWithHttpServletRequest(String username, String password) {
        try {
            request.login(username, password);
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }

}