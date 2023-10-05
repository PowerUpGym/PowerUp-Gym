package com.example.PowerUpGym.controller;

//import com.example.PowerUpGym.entity.classesGym.PlayerClassEnrollment;

import com.example.PowerUpGym.entity.classesGym.ClassesEntity;
import com.example.PowerUpGym.entity.notifications.NotificationsEntity;
import com.example.PowerUpGym.entity.users.PlayersEntity;
import com.example.PowerUpGym.entity.users.UserEntity;
import com.example.PowerUpGym.repositories.UserEntityRepositories;
import com.example.PowerUpGym.services.NotificationsService;
import com.example.PowerUpGym.services.PlayerService;
import com.example.PowerUpGym.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@Controller
@Secured("PLAYER") // define a list of security configuration attributes for business methods
@RequestMapping("/playerPage") // base path
public class PlayerController {

    @Autowired
    PlayerService playerService;
    @Autowired
    UserService userService;
    @Autowired
    UserEntityRepositories userEntityRepositories;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private NotificationsService notificationService;
    @Autowired
    private HttpServletRequest request;

    @GetMapping("")  // Home Page ("/playerPage")
    public String getLoginPagePlayer() {
        return "playerPages/playerPage.html";
    }


    @GetMapping("/index")
    public String index() {
        return "index.html";
    }


    @GetMapping("/playerInfo")
    public String getMyInfo(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }
        String username = principal.getName();
        UserEntity userEntity = playerService.findUserByUsername(username);

        if (userEntity == null || userEntity.getRole() == null) {
            return "redirect:/error";
        }
        model.addAttribute("user", userEntity);
        PlayersEntity player = userEntity.getPlayer();
        model.addAttribute("player", player);

        return "playerPages/playerInfo.html";
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
        return "playerPages/playerInfo.html";
    }

    @PostMapping("/updatePlayer")
    public RedirectView updatePlayer(Long userId, Long playerId, String address, Integer age, Integer height, Integer weight,
                                     String fullName, String username, String email, String phoneNumber, String password) {

        PlayersEntity existingPlayer = playerService.getPlayerById(playerId);
        UserEntity existingUser = userService.findUserById(userId);

        String newPassword = (password != null && !password.isEmpty()) ? passwordEncoder.encode(password) : existingUser.getPassword();

        UserEntity updatedUser = updateUser(existingUser, fullName, username, email, phoneNumber, newPassword);
        userService.saveUser(updatedUser);

        PlayersEntity updatedPlayer = updatePlayer(existingPlayer, address, age, height, weight, updatedUser);
        playerService.signupPlayer(updatedPlayer);

        return new RedirectView("playerInfo");
    }

    private UserEntity updateUser(UserEntity existingUser, String fullName, String username, String email, String phoneNumber, String password) {
        return UserEntity.builder()
                .id(existingUser.getId())
                .fullName(fullName)
                .username(username)
                .email(email)
                .phoneNumber(phoneNumber)
                .password(password)
                .role(existingUser.getRole())
                .image(existingUser.getImage())
                .build();
    }

    private PlayersEntity updatePlayer(PlayersEntity existingPlayer, String address, Integer age, Integer height, Integer weight, UserEntity updatedUser) {
        return PlayersEntity.builder()
                .id(existingPlayer.getId())
                .address(address)
                .age(age)
                .height(height)
                .weight(weight)
                .start_date(existingPlayer.getStart_date())
                .end_date(existingPlayer.getEnd_date())
                .user(updatedUser)
                .admin(existingPlayer.getAdmin())
                .selectedPackage(existingPlayer.getSelectedPackage())
                .accountEnabled(existingPlayer.isAccountEnabled())
                .build();
    }


    @GetMapping("/enrollments")
    public String getMyClasses(Principal principal, Model model) {
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