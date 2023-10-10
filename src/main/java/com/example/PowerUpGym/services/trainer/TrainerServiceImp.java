package com.example.PowerUpGym.services.trainer;

import com.example.PowerUpGym.bo.auth.NotificationRequest;
import com.example.PowerUpGym.bo.auth.update.TrainerUpdateRequest;
import com.example.PowerUpGym.entity.classesGym.ClassesEntity;
import com.example.PowerUpGym.entity.classesGym.PlayerClassEnrollment;
import com.example.PowerUpGym.entity.notifications.NotificationsEntity;
import com.example.PowerUpGym.entity.users.AdminEntity;
import com.example.PowerUpGym.entity.users.TrainerEntity;
import com.example.PowerUpGym.entity.users.UserEntity;
import com.example.PowerUpGym.repositories.ClassesEntityRepository;
import com.example.PowerUpGym.repositories.TrainerEntityRepository;
import com.example.PowerUpGym.repositories.UserEntityRepositories;
import com.example.PowerUpGym.services.admin.AdminService;
import com.example.PowerUpGym.services.notification.NotificationsService;
import com.example.PowerUpGym.services.users.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TrainerServiceImp implements TrainerService {

    private final TrainerEntityRepository trainerEntityRepository;
    private final UserEntityRepositories userEntityRepositories;
    private final ClassesEntityRepository classesEntityRepository;
//    private final UserService userService;
//    private final NotificationsService notificationsService;
//    private final AdminService adminService;
//    private final PasswordEncoder passwordEncoder;

    public TrainerServiceImp(TrainerEntityRepository trainerEntityRepository, UserEntityRepositories userEntityRepositories, ClassesEntityRepository classesEntityRepository/*, UserService userService, NotificationsService notificationsService, AdminService adminService, PasswordEncoder passwordEncoder*/) {
        this.trainerEntityRepository = trainerEntityRepository;
        this.userEntityRepositories = userEntityRepositories;
        this.classesEntityRepository = classesEntityRepository;
//        this.userService = userService;
//        this.notificationsService = notificationsService;
//        this.adminService = adminService;
//        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<TrainerEntity> getAllTrainer() {
        return trainerEntityRepository.findAll();
    }

    @Override
    public TrainerEntity signupTrainer(TrainerEntity trainerEntity) {
        trainerEntityRepository.save(trainerEntity);
        return trainerEntity;
    }

    @Override
    public UserEntity findUserByUsername(String username){
        return userEntityRepositories.findByUsername(username);    }


    @Override
    public List<ClassesEntity> getClassesForTrainer(TrainerEntity trainer){
        return trainer.getOwnedClasses();
    }

    @Override
    public ClassesEntity getClassDetails(Long classId) {
        Optional<ClassesEntity> optionalClass = classesEntityRepository.findById(classId);

        if (optionalClass.isPresent()) {
            return optionalClass.get();
        } else {
            throw new RuntimeException("Class not found with ID: " + classId);
        }
    }

    @Override
    public TrainerEntity getTrainerById(Long id) {
        return trainerEntityRepository.findById(id).orElse(null);
    }

    @Override
    public void updateTrainerInfo(UserEntity userEntity) {
        TrainerEntity trainerEntity = userEntity.getTrainer();

        if (trainerEntity != null) {
            trainerEntity.setAge(userEntity.getTrainer().getAge());
            trainerEntity.setExperience(userEntity.getTrainer().getExperience());

            trainerEntityRepository.save(trainerEntity);
        }
    }

//    public RedirectView updateTrainerProfile(TrainerUpdateRequest updateRequest) {
//        UserEntity userEntity = userService.getUserById(updateRequest.getUserId());
//        UserEntity updateUser = UpdateTrainer(updateRequest, userEntity);
//        userService.saveUser(updateUser);
//        return new RedirectView("/trainerPage/trainerProfile");
//    }
//
//    private UserEntity UpdateTrainer(TrainerUpdateRequest updateRequest, UserEntity userEntity) {
//        AdminEntity admin = adminService.getAdminById(updateRequest.getAdminId());
//        TrainerEntity trainer = TrainerEntity.builder()
//                .id(userEntity.getTrainer().getId())
//                .age(updateRequest.getAge())
//                .experience(updateRequest.getExperience())
//                .user(userEntity)
//                .admin(admin)
//                .build();
//
//        userEntity.setTrainer(trainer);
//
//        return UserEntity.builder()
//                .id(updateRequest.getUserId())
//                .fullName(updateRequest.getFullName())
//                .username(updateRequest.getUsername())
//                .email(updateRequest.getEmail())
//                .phoneNumber(updateRequest.getPhoneNumber())
//                .password(updateRequest.getPassword().isEmpty() ? userEntity.getPassword() : passwordEncoder.encode(updateRequest.getPassword()))
//                .role(userEntity.getRole())
//                .player(userEntity.getPlayer())
//                .trainer(userEntity.getTrainer())
//                .image(updateRequest.getImage())
//                .build();
//    }
//
//    public RedirectView sendMessage(NotificationRequest notificationRequest, Principal principal) {
//        String senderUsername = principal.getName();
//        UserEntity sender = userService.findUserByUsername(senderUsername);
//
//        UserEntity receiver = userService.findUserById(notificationRequest.getReceiverId());
//
//        NotificationsEntity notification = createNotification(notificationRequest, sender, receiver);
//        notificationsService.saveNotification(notification);
//
//        return new RedirectView("trainerClasses");
//    }
//    private NotificationsEntity createNotification(NotificationRequest notificationRequest, UserEntity sender, UserEntity receiver) {
//        return NotificationsEntity.builder()
//                .message(notificationRequest.getMessage())
//                .sender(sender)
//                .receiver(receiver)
//                .timeStamp(LocalDateTime.now())
//                .build();
//    }
//
//
//    public RedirectView sendMessageToAllPlayers(@RequestParam("classId") Long classId, NotificationRequest notificationRequest, Principal principal) {
//        ClassesEntity classDetails = getClassDetails(classId);
//        Set<PlayerClassEnrollment> enrolledPlayers = classDetails.getRegistrations();
//
//        String senderUsername = principal.getName();
//        UserEntity sender = userService.findUserByUsername(senderUsername);
//
//        LocalDateTime now = LocalDateTime.now();
//
//        enrolledPlayers.stream()
//                .map(enrollment -> {
//                    NotificationRequest request = new NotificationRequest();
//                    request.setMessage(notificationRequest.getMessage());
//                    request.setSenderId(sender.getId());
//                    request.setReceiverId(enrollment.getPlayer().getUser().getId());
//                    return request;
//                })
//                .forEach(request -> {
//                    UserEntity receiver = userService.findUserById(request.getReceiverId());
//                    NotificationsEntity notification = createNotificationForAllUsers(request, sender, receiver, now);
//                    notificationsService.saveNotification(notification);
//                });
//
//        return new RedirectView("/trainerPage/trainerClasses");
//    }
//    private NotificationsEntity createNotificationForAllUsers(NotificationRequest notificationRequest, UserEntity sender, UserEntity receiver, LocalDateTime timeStamp) {
//        return NotificationsEntity.builder()
//                .message(notificationRequest.getMessage())
//                .sender(sender)
//                .receiver(receiver)
//                .timeStamp(timeStamp)
//                .build();
//    }
}
