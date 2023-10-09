package com.example.PowerUpGym.controller;

import com.example.PowerUpGym.bo.auth.NotificationRequest;
import com.example.PowerUpGym.bo.auth.update.TrainerUpdateRequest;
import com.example.PowerUpGym.entity.classesGym.ClassesEntity;
import com.example.PowerUpGym.entity.classesGym.PlayerClassEnrollment;
import com.example.PowerUpGym.entity.users.TrainerEntity;
import com.example.PowerUpGym.entity.users.UserEntity;
import com.example.PowerUpGym.services.trainer.TrainerService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import java.security.Principal;
import java.util.List;
import java.util.Set;

@Controller
@Secured("TRAINER") // define a list of security configuration attributes for business methods
@RequestMapping("/trainerPage") // base path
public class TrainerController {


    private final TrainerService trainerService;

    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @GetMapping("")
    public String getLoginPageTrainer() {
        return "trainerPages/trainerPage";
    }

    @GetMapping("/trainerProfile")
    public String getTrainerInfo(Principal principal, Model model) {
        if (principal != null) {
            String username = principal.getName();
            UserEntity userEntity = trainerService.findUserByUsername(username);
            if (userEntity != null) {
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
    public String getTrainerEnrolledClasses(Principal principal, Model model) {
        if (principal != null) {
            String username = principal.getName();
            UserEntity userEntity = trainerService.findUserByUsername(username);
            if (userEntity != null) {
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
    public String getTrainerClassDetails(@RequestParam("classId") Long classId, Model model) {
        ClassesEntity classDetails = trainerService.getClassDetails(classId);
        if (classDetails != null) {
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
        return "trainerPages/editTrainerProfile";
    }

    @PostMapping("/updateTrainerProfile")
    public RedirectView updateTrainerProfile(TrainerUpdateRequest updateRequest) {
        return trainerService.updateTrainerProfile(updateRequest);
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
       return trainerService.sendMessage(notificationRequest,principal);
    }

    @GetMapping("/sendToAllPlayers/{classId}")
    public String sendToAllPlayersForm(@PathVariable Long classId, Model model) {
        model.addAttribute("classId", classId);
        return "trainerPages/sendToAllPlayers";
    }

    @PostMapping("/sendMessageToAllPlayers")
    public RedirectView sendMessageToAllPlayers(@RequestParam("classId") Long classId, NotificationRequest notificationRequest, Principal principal) {
        return trainerService.sendMessageToAllPlayers(classId,notificationRequest,principal);
    }

}