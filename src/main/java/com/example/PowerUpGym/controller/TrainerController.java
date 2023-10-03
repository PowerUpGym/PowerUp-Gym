package com.example.PowerUpGym.controller;

import com.example.PowerUpGym.entity.classesGym.ClassesEntity;
import com.example.PowerUpGym.entity.classesGym.PlayerClassEnrollment;
import com.example.PowerUpGym.entity.users.*;
import com.example.PowerUpGym.services.TrainerService;
import com.example.PowerUpGym.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.Set;

@Controller
@Secured("TRAINER") // define a list of security configuration attributes for business methods
@RequestMapping("/trainerPage") // base path
public class TrainerController {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    TrainerService trainerService;

    @Autowired
    UserService userService;

//    @GetMapping("/signupTrainer")
//    public String getSignupTrainer(){
//        return "signupTrainer.html";
//    }

    @GetMapping("")
    public String getLoginPageTrainer() {
        return "trainerPages/trainerPage";
    }


//    @PostMapping("/signupTrainer")
//    public RedirectView getSignupTrainer(String fullName, String username, String password, String email, String phoneNumber,int age , String experience){
//
//        TrainerEntity trainerEntity = new TrainerEntity();
//
//        UserEntity user = new UserEntity();
//        user.setFullName(fullName);
//        user.setUsername(username);
//        user.setEmail(email);
//        user.setPhoneNumber(phoneNumber);
//        String encryptedPassword = passwordEncoder.encode(password);
//        user.setPassword(encryptedPassword);
//
//        UserRoleEntity trainerRole = new UserRoleEntity();
//        trainerRole.setId(3L); // Set the ID of the "Trainer" role
//        user.setRole(trainerRole);
//
//        userService.signupUser(user);
//
//        trainerEntity.setAge(age);
//        trainerEntity.setExperience(experience);
//        trainerEntity.setUser(user);
//
//        trainerService.signupTrainer(trainerEntity);
//        authWithHttpServletRequest(username , password);
//
//        return new RedirectView("/index");
//    }

    @GetMapping("/trainerProfile")
    public String getTrainerInfo(Principal principal, Model model){
        System.out.println("yoyo it's working");
        if (principal != null){
            String username = principal.getName();
            UserEntity userEntity = trainerService.findUserByUsername(username);

            if (userEntity != null){
                trainerService.updateTrainerInfo(userEntity);
                model.addAttribute("user", userEntity);
                TrainerEntity trainer = userEntity.getTrainer();
                model.addAttribute("trainer", trainer);
                return "trainerPages/trainerProfile";
            }
        }

        return "index";
    }

    @GetMapping("/trainerClasses")
    public String getTrainerEnrolledClasses(Principal principal, Model model){
        System.out.println("is it working??");
        if (principal != null){
            String username = principal.getName();
            UserEntity userEntity = trainerService.findUserByUsername(username);

            if (userEntity != null){
                TrainerEntity trainer = userEntity.getTrainer();
                List<ClassesEntity> trainerClasses = trainerService.getClassesForTrainer(trainer);

                model.addAttribute("user", userEntity);
                model.addAttribute("trainer", trainer);
                model.addAttribute("trainerClasses", trainerClasses);

                return "trainerPages/trainerClasses";
            }
        }
        return "index";
    }

    @GetMapping("/trainerClassDetails")
    public String getTrainerClassDetails(@RequestParam("classId") Long classId, Model model){
        ClassesEntity classDetails = trainerService.getClassDetails(classId);

        if (classDetails != null){
            Set<PlayerClassEnrollment> enrolledPlayers = classDetails.getRegistrations();

            model.addAttribute("classDetails", classDetails);
            model.addAttribute("enrolledPlayers", enrolledPlayers);

            return "trainerPages/trainerClassesDetailes";
        }

        return "index";
    }

    @GetMapping("/editTrainerProfile")
    public String getEditTrainerProfile(Principal principal, Model model) {
        if (principal != null) {
            String username = principal.getName();
            UserEntity userEntity = trainerService.findUserByUsername(username);

            if (userEntity != null) {
                model.addAttribute("user", userEntity);
                TrainerEntity trainer = userEntity.getTrainer();
                model.addAttribute("trainer", trainer);
                return "trainerPages/editTrainerProfile";
            }
        }

//        return "index";
        return "trainerPages/editTrainerProfile";
    }

    @PostMapping("/updateTrainerProfile")
    public RedirectView updateTrainerProfile(@RequestParam("userId") Long userId,
                                             @RequestParam("fullName") String fullName,
                                             @RequestParam("username") String username,
                                             @RequestParam("email") String email,
                                             @RequestParam("phoneNumber") String phoneNumber,
                                             @RequestParam("password") String password,
                                             @RequestParam("age") int age,
                                             @RequestParam("experience") String experience) {

        UserEntity userEntity = userService.getUserById(userId);

        UserEntity updatedUser = buildUpdatedUser(userId, fullName, username, email, phoneNumber, password, userEntity);

        updateTrainerInfo(updatedUser, age, experience);

        return new RedirectView("/trainerPage/trainerProfile");
    }

    private UserEntity buildUpdatedUser(Long userId, String fullName, String username, String email,
                                        String phoneNumber, String password, UserEntity userEntity) {
        return UserEntity.builder()
                .id(userId)
                .fullName(fullName)
                .username(username)
                .email(email)
                .phoneNumber(phoneNumber)
                .password(password.isEmpty() ? userEntity.getPassword() : passwordEncoder.encode(password))
                .role(userEntity.getRole())
                .player(userEntity.getPlayer())
                .trainer(userEntity.getTrainer())
                .build();
    }

    private void updateTrainerInfo(UserEntity userEntity, int age, String experience) {
        TrainerEntity trainer = TrainerEntity.builder()
                .id(userEntity.getTrainer().getId())
                .age(age)
                .experience(experience)
                .user(userEntity)
                .build();

        userEntity.setTrainer(trainer);

        trainerService.updateTrainerInfo(userEntity);
    }


    public void authWithHttpServletRequest(String username, String password) {
        try {
            request.login(username, password);
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }
}