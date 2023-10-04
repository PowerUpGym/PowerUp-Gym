package com.example.PowerUpGym.controller;

//import com.example.PowerUpGym.entity.classesGym.PlayerClassEnrollment;
import com.example.PowerUpGym.entity.classesGym.ClassesEntity;
import com.example.PowerUpGym.entity.notifications.NotificationsEntity;
import com.example.PowerUpGym.entity.users.AdminEntity;
import com.example.PowerUpGym.entity.users.PlayersEntity;
import com.example.PowerUpGym.entity.users.UserEntity;
import com.example.PowerUpGym.entity.users.UserRoleEntity;
import com.example.PowerUpGym.repositories.UserEntityRepositories;
//import com.example.PowerUpGym.services.ClassEnrollmentService;
import com.example.PowerUpGym.services.NotificationsService;
import com.example.PowerUpGym.services.PlayerService;
import com.example.PowerUpGym.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@Secured("PLAYER") // define a list of security configuration attributes for business methods
@RequestMapping("/playerPage") // base path
public class PlayerController {

    @Autowired
    PlayerService playerService;
@Autowired
    UserService userService;
    @Autowired
    private NotificationsService notificationService;
    @Autowired
    UserEntityRepositories userEntityRepositories;
    @Autowired
    private HttpServletRequest request;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("")  // Home Page ("/playerPage")
    public String getLoginPagePlayer() {
        return "playerPages/playerPage.html";
    }


    @GetMapping("/index")
    public String index() {
        return "index.html";
    }

    public void authWithHttpServletRequest(String username, String password) {
        try {
            request.login(username, password);
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }


    @GetMapping("/playerInfo")
    public String getMyInfo(Principal principal, Model model) {
        return Optional.ofNullable(principal)
                .map(Principal::getName)
                .flatMap(userName -> {
                    UserEntity userEntity = playerService.findUserByUsername(userName);
                    return Optional.ofNullable(userEntity)
                            .filter(entity -> entity.getRole() != null)
                            .map(entity -> {
                                model.addAttribute("user", entity);
                                PlayersEntity player = entity.getPlayer();
                                model.addAttribute("player", player);
                                return "playerPages/playerInfo.html";
                            });
                })
                .orElse("index.html");
    }


    @GetMapping("/updatePlayerProfile")
    public String getEditPlayerProfile(Principal principal, Model model) {
        if (principal != null) {
            String username = principal.getName();
            UserEntity userEntity = playerService.findUserByUsername(username);

            if (userEntity != null) {
                model.addAttribute("user", userEntity);
                PlayersEntity player = userEntity.getPlayer();
                model.addAttribute("player", player);
                return "playerPages/editprofile.html";

            }
        }
         return "updatePlayer.html";
     }

    @PostMapping("/updatePlayer")
    public RedirectView updatePlayer(Long userId,Long playerId,String address, int age, int height, int weight, String image, String fullName, String username, String email, String phoneNumber) {
        UserEntity user = userService.findUserById(userId);
        PlayersEntity player = playerService.getPlayerById(playerId);

        player.setAddress(address);
        player.setAge(age);
        player.setHeight(height);
        player.setWeight(weight);
//        player.setImage(image);

        user.setFullName(fullName);
        user.setUsername(username);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        user.setImage(image);

        playerService.signupPlayer(player);
        userService.saveUser(user);

        return new RedirectView("playerInfo");
    }

    @GetMapping("/enrollments")
    public String getMyclasses(Principal principal, Model model) {
        if (principal != null) {
            String userName = principal.getName();
            UserEntity userEntity = playerService.findUserByUsername(userName);
            if (userEntity != null && userEntity.getRole() != null) {
                PlayersEntity player = userEntity.getPlayer();

                List<ClassesEntity> enrollments = playerService.getPlayerEnrolment(player);

                model.addAttribute("enrollments", enrollments);

                return "playerPages/enrollments.html";
            }
        }
        return "index.html";
    }




//    @GetMapping("/calculateBMI")
//    public String calculateBMI(@RequestParam("weight") double weight, @RequestParam("height") double height, Model model) {
//        if (weight <= 0 || height <= 0) {
//            model.addAttribute("bmiResult", "Invalid input. Please enter valid values.");
//            model.addAttribute("bmiExplanation", "");
//        } else {
//            // Calculate BMI
//            double bmi = weight / (height * height);
//            model.addAttribute("bmiResult", "Your BMI is: " + String.format("%.2f", bmi));
//
//            String explanation = "";
//            if (bmi < 18.5) {
//                explanation = "You are underweight.";
//            } else if (bmi >= 18.5 && bmi < 24.9) {
//                explanation = "You have a normal weight.";
//            } else if (bmi >= 25 && bmi < 29.9) {
//                explanation = "You are overweight.";
//            } else {
//                explanation = "You are obese.";
//            }
//            model.addAttribute("bmiExplanation", explanation);
//        }
//
//
//
//        return "playerInfo";
//    }

    @GetMapping("/notifications")
    public String getNotifications(Principal principal, Model model) {
        String userName = principal.getName();
        UserEntity userEntity = userService.findUserByUsername(userName);

        if (userEntity != null && userEntity.getPlayer() != null) {
            Long playerId = userEntity.getId();
            List<NotificationsEntity> notifications = notificationService.getNotificationsForPlayer(playerId);
            model.addAttribute("notifications", notifications);
        }

        return "playerPages/notifications.html";
    }




}