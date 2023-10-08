package com.example.PowerUpGym.controller;

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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Controller
@Secured("TRAINER") // define a list of security configuration attributes for business methods
@RequestMapping("/trainerPage") // base path
public class TrainerController {

    @Autowired
    private HttpServletRequest request;
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
                                             @RequestParam("experience") String experience,
                                             @RequestParam("adminId") Long adminId,
                                             @RequestParam("image") String image) {

        UserEntity userEntity = userService.getUserById(userId);

        UserEntity updateUser = UpdateTrainer(userId, fullName, username, email, phoneNumber, password, userEntity, age, experience , adminId, image);

        userService.saveUser(updateUser);

        return new RedirectView("/trainerPage/trainerProfile");
    }


    private UserEntity UpdateTrainer(Long userId, String fullName, String username, String email, String phoneNumbeer, String password, UserEntity userEntity, int age, String experience,Long adminId , String image) {

        AdminEntity admin = adminService.getAdminById(adminId);
        TrainerEntity trainer = TrainerEntity.builder()
                .id(userEntity.getTrainer().getId())
                .age(age)
                .experience(experience)
                .user(userEntity)
                .admin(admin)
                .build();

        userEntity.setTrainer(trainer);

        return UserEntity.builder()
                .id(userId)
                .fullName(fullName)
                .username(username)
                .email(email)
                .phoneNumber(phoneNumbeer)
                .password(password.isEmpty() ? userEntity.getPassword() : passwordEncoder.encode(password))
                .role(userEntity.getRole())
                .player(userEntity.getPlayer())
                .trainer(userEntity.getTrainer())
                .image(image)
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
    public RedirectView sendMessage(@RequestParam("receiverId") Long receiverId, @RequestParam String message, Principal principal) {
        String senderUsername = principal.getName();
        UserEntity sender = userService.findUserByUsername(senderUsername);

        UserEntity receiver = userService.findUserById(receiverId);

        NotificationsEntity notification = new NotificationsEntity();
        notification.setMessage(message);
        notification.setSender(sender);
        notification.setReceiver(receiver);
        notification.setTimeStamp(LocalDateTime.now());
        notificationService.saveNotification(notification);

        return new RedirectView("trainerClasses");
    }

// ==============================

    @GetMapping("/sendToAllPlayers/{classId}")
    public String sendToAllPlayersForm(@PathVariable Long classId, Model model) {
        model.addAttribute("classId", classId);
        return "trainerPages/sendToAllPlayers";
    }
    @PostMapping("/sendMessageToAllPlayers")
    public RedirectView sendMessageToAllPlayers(@RequestParam("classId") Long classId, @RequestParam("message") String message, Principal principal) {
        // Retrieve the class details and enrolled players
        ClassesEntity classDetails = trainerService.getClassDetails(classId);
        Set<PlayerClassEnrollment> enrolledPlayers = classDetails.getRegistrations();

        String senderUsername = principal.getName();
        UserEntity sender = userService.findUserByUsername(senderUsername);

        LocalDateTime now = LocalDateTime.now();

        enrolledPlayers.stream()
                .map(enrollment -> enrollment.getPlayer().getUser()) // Use lambda expression here
                .map(receiver -> {
                    NotificationsEntity notification = new NotificationsEntity();
                    notification.setMessage(message);
                    notification.setSender(sender);
                    notification.setReceiver(receiver);
                    notification.setTimeStamp(now);
                    return notification;
                })
                .forEach(notificationService::saveNotification);

        return new RedirectView("/trainerPage/trainerClasses");
    }

}