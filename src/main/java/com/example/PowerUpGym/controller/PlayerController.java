package com.example.PowerUpGym.controller;

import com.example.PowerUpGym.bo.auth.update.PlayerUpdateRequest;
import com.example.PowerUpGym.bo.auth.update.UserUpdateRequest;
import com.example.PowerUpGym.entity.classesGym.ClassesEntity;
import com.example.PowerUpGym.entity.notifications.NotificationsEntity;
import com.example.PowerUpGym.entity.users.PlayersEntity;
import com.example.PowerUpGym.entity.users.UserEntity;
import com.example.PowerUpGym.services.NotificationsService;
import com.example.PowerUpGym.services.PlayerService;
import com.example.PowerUpGym.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;
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
    PasswordEncoder passwordEncoder;
    @Autowired
    private NotificationsService notificationService;

    @GetMapping("")  // Home Page ("/playerPage")
    public String getLoginPagePlayer() {
        return "playerPages/playerPage.html";
    }

    @GetMapping("/index")
    public String index() {
        return "index.html";
    }

    // ========== Method to Get All Player Information's ==========
    @GetMapping("/playerInfo")
    public String getPlayerInfo(Principal principal, Model model) {
        if (principal != null) {
            String username = principal.getName();
            UserEntity userEntity = playerService.findUserByUsername(username);

            if (userEntity != null) {
                model.addAttribute("user", userEntity);
                PlayersEntity player = userEntity.getPlayer();
                model.addAttribute("player", player);
                return "playerPages/playerInfo.html";
            }
        }
        return "redirect:/login";
    }



    // ========== Helper Method to update UserEntity Using UserUpdateRequest ==========
    private UserEntity updateUser(UserEntity existingUser, UserUpdateRequest userUpdateRequest) {
        String newPassword = (userUpdateRequest.getPassword() != null && !userUpdateRequest.getPassword().isEmpty())
                ? passwordEncoder.encode(userUpdateRequest.getPassword())
                : existingUser.getPassword();

        return UserEntity.builder()
                .id(existingUser.getId())
                .fullName(userUpdateRequest.getFullName())
                .username(userUpdateRequest.getUsername())
                .email(userUpdateRequest.getEmail())
                .phoneNumber(userUpdateRequest.getPhoneNumber())
                .password(newPassword)
                .role(existingUser.getRole())
                .image(existingUser.getImage())
                .build();
    }

    // ========== Helper Method to update PlayersEntity Using PlayerUpdateRequest ==========
    private PlayersEntity updatePlayer(PlayersEntity existingPlayer, PlayerUpdateRequest playerUpdateRequest, UserEntity updatedUser) {
        return PlayersEntity.builder()
                .id(existingPlayer.getId())
                .address(playerUpdateRequest.getAddress())
                .age(playerUpdateRequest.getAge())
                .height(playerUpdateRequest.getHeight())
                .weight(playerUpdateRequest.getWeight())
                .start_date(existingPlayer.getStart_date())
                .end_date(existingPlayer.getEnd_date())
                .user(updatedUser)
                .admin(existingPlayer.getAdmin())
                .selectedPackage(existingPlayer.getSelectedPackage())
                .accountEnabled(existingPlayer.isAccountEnabled())
                .build();
    }

    // ==========  Method To Update Player From playerUpdateRequest  ====================
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
    public RedirectView updatePlayer(PlayerUpdateRequest playerUpdateRequest, UserUpdateRequest userUpdateRequest) {

        PlayersEntity existingPlayer = playerService.getPlayerById(playerUpdateRequest.getPlayerId());
        UserEntity existingUser = userService.findUserById(userUpdateRequest.getUserId());

        UserEntity updatedUser = updateUser(existingUser, userUpdateRequest);
        userService.saveUser(updatedUser);

        // Update the username in the principal
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UsernamePasswordAuthenticationToken updatedAuthentication = new UsernamePasswordAuthenticationToken(updatedUser.getUsername(), authentication.getCredentials(), authentication.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(updatedAuthentication);

        PlayersEntity updatedPlayer = updatePlayer(existingPlayer, playerUpdateRequest, updatedUser);
        playerService.signupPlayer(updatedPlayer);

        return new RedirectView("playerInfo");
    }

    // ========== Method To Get Classes(With Details) That The Player Enrolment In ====================
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

    // ========== Method To Get All The Notifications (From Admin and Trainer) ====================
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

}