package com.example.PowerUpGym.controller;

import com.example.PowerUpGym.bo.auth.NotificationRequest;
import com.example.PowerUpGym.bo.auth.update.TrainerUpdateRequest;
import com.example.PowerUpGym.entity.classesGym.ClassesEntity;
import com.example.PowerUpGym.entity.classesGym.PlayerClassEnrollment;
import com.example.PowerUpGym.entity.notifications.NotificationsEntity;
import com.example.PowerUpGym.entity.users.*;
import com.example.PowerUpGym.services.admin.AdminService;
import com.example.PowerUpGym.services.notification.NotificationsService;
import com.example.PowerUpGym.services.trainer.TrainerService;
import com.example.PowerUpGym.services.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Controller
@Secured("TRAINER") // define a list of security configuration attributes for business methods
@RequestMapping("/trainerPage") // base path
public class TrainerController {

    @Autowired
    private NotificationsService notificationService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    TrainerService trainerService;

    @Autowired
    AdminService adminService;

    @Autowired
    UserService userService;

    @GetMapping("")
    public String getLoginPageTrainer() {
        return "trainerPages/trainerPage";
    }

    @GetMapping("/trainerProfile")
    public String getTrainerInfo(Principal principal, Model model){
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
        return "trainerPages/editTrainerProfilesss";
    }

    @PostMapping("/updateTrainerProfile")
    public RedirectView updateTrainerProfile(TrainerUpdateRequest updateRequest) {
        UserEntity userEntity = userService.getUserById(updateRequest.getUserId());
        UserEntity updateUser = UpdateTrainer(updateRequest, userEntity);
        userService.saveUser(updateUser);

        // Update the username in the principal
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // retrieves the current authentication information from the security context using SecurityContextHolder. It provides access to the current user's authentication details
        UsernamePasswordAuthenticationToken updatedAuthentication = new UsernamePasswordAuthenticationToken(updateUser.getUsername(), authentication.getCredentials(), authentication.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(updatedAuthentication);

        return new RedirectView("/trainerPage/trainerProfile");
    }


    private UserEntity UpdateTrainer(TrainerUpdateRequest updateRequest, UserEntity userEntity) {
        AdminEntity admin = adminService.getAdminById(updateRequest.getAdminId());
        TrainerEntity trainer = TrainerEntity.builder()
                .id(userEntity.getTrainer().getId())
                .age(updateRequest.getAge())
                .experience(updateRequest.getExperience())
                .user(userEntity)
                .admin(admin)
                .build();

        userEntity.setTrainer(trainer);

        return UserEntity.builder()
                .id(updateRequest.getUserId())
                .fullName(updateRequest.getFullName())
                .username(updateRequest.getUsername())
                .email(updateRequest.getEmail())
                .phoneNumber(updateRequest.getPhoneNumber())
                .password(updateRequest.getPassword().isEmpty() ? userEntity.getPassword() : passwordEncoder.encode(updateRequest.getPassword()))
                .role(userEntity.getRole())
                .player(userEntity.getPlayer())
                .trainer(userEntity.getTrainer())
                .image(updateRequest.getImage())
                .build();
    }


    @GetMapping("/allplayersenrollment/{id}")
    public String sendMessageToUser(@PathVariable Long id, Model model) {
        model.addAttribute("receiverId", id);
        return "trainerPages/sendMessage";
    }

    @GetMapping("/sendMessage")
    public String getSendMessageForm(@RequestParam Long receiverId, Model model) {
        model.addAttribute("receiverId", receiverId);
        return "trainerPages/sendMessage";
    }

    @PostMapping("/sendMessage")
    public RedirectView sendMessage(NotificationRequest notificationRequest, Principal principal) {
        String senderUsername = principal.getName();
        UserEntity sender = userService.findUserByUsername(senderUsername);
        UserEntity receiver = userService.findUserById(notificationRequest.getReceiverId());
        NotificationsEntity notification = createNotification(notificationRequest, sender, receiver);
        notificationService.saveNotification(notification);
        return new RedirectView("trainerClasses");
    }


    @GetMapping("/sendToAllPlayers/{classId}")
    public String sendToAllPlayersForm(@PathVariable Long classId, Model model) {
        model.addAttribute("classId", classId);
        return "trainerPages/sendToAllPlayers";
    }
    @PostMapping("/sendMessageToAllPlayers")
    public RedirectView sendMessageToAllPlayers(@RequestParam("classId") Long classId,NotificationRequest notificationRequest, Principal principal) {
        ClassesEntity classDetails = trainerService.getClassDetails(classId);
        Set<PlayerClassEnrollment> enrolledPlayers = classDetails.getRegistrations();
        String senderUsername = principal.getName();
        UserEntity sender = userService.findUserByUsername(senderUsername);

        LocalDateTime now = LocalDateTime.now();

        enrolledPlayers.stream()
                .map(enrollment -> {
                    NotificationRequest request = new NotificationRequest();
                    request.setMessage(notificationRequest.getMessage());
                    request.setSenderId(sender.getId());
                    request.setReceiverId(enrollment.getPlayer().getUser().getId());
                    return request;
                })
                .forEach(request -> {
                    UserEntity receiver = userService.findUserById(request.getReceiverId());
                    NotificationsEntity notification = createNotificationForAllPlayers(request, sender, receiver, now);
                    notificationService.saveNotification(notification);
                });

        return new RedirectView("/trainerPage/trainerClasses");
    }
    private NotificationsEntity createNotification(NotificationRequest notificationRequest, UserEntity sender, UserEntity receiver) {
        return NotificationsEntity.builder()
                .message(notificationRequest.getMessage())
                .sender(sender)
                .receiver(receiver)
                .timeStamp(LocalDateTime.now())
                .build();
    }

    private NotificationsEntity createNotificationForAllPlayers(NotificationRequest notificationRequest, UserEntity sender, UserEntity receiver, LocalDateTime timeStamp) {
        return NotificationsEntity.builder()
                .message(notificationRequest.getMessage())
                .sender(sender)
                .receiver(receiver)
                .timeStamp(timeStamp)
                .build();
    }


}