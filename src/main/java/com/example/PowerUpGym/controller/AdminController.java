package com.example.PowerUpGym.controller;

import com.example.PowerUpGym.bo.auth.AddClassRequest;
import com.example.PowerUpGym.bo.auth.AddPackageRequest;
import com.example.PowerUpGym.bo.auth.AddPlayerToClassRequest;
import com.example.PowerUpGym.bo.auth.update.ClassUpdateRequest;
import com.example.PowerUpGym.bo.auth.update.UserUpdateRequest;
import com.example.PowerUpGym.bo.auth.users.PlayerRegistrationRequest;
import com.example.PowerUpGym.bo.auth.users.TrainerRegistrationRequest;
import com.example.PowerUpGym.bo.auth.users.UserRegistrationRequest;
import com.example.PowerUpGym.entity.classesGym.ClassesEntity;
import com.example.PowerUpGym.entity.classesGym.PlayerClassEnrollment;
import com.example.PowerUpGym.entity.notifications.NotificationsEntity;
import com.example.PowerUpGym.entity.packagesGym.PackagesEntity;
import com.example.PowerUpGym.entity.payments.PaymentsEntity;
import com.example.PowerUpGym.entity.users.*;
import com.example.PowerUpGym.enums.Role;
import com.example.PowerUpGym.services.admin.AdminService;
import com.example.PowerUpGym.services.classes.ClassService;
import com.example.PowerUpGym.services.notification.NotificationsService;
import com.example.PowerUpGym.services.packagee.PackageService;
import com.example.PowerUpGym.services.payment.PaymentService;
import com.example.PowerUpGym.services.player.PlayerService;
import com.example.PowerUpGym.services.roles.UserRoleService;
import com.example.PowerUpGym.services.trainer.TrainerService;
import com.example.PowerUpGym.services.users.UserService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@Secured("ADMIN") // define a list of security configuration attributes for business methods
@RequestMapping("/adminPage") // base path (Home path)
public class AdminController {
    @Autowired
    AdminService adminService;
    @Autowired
    ClassService classService;
    @Autowired
    PackageService packageService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserService userService;
    @Autowired
    TrainerService trainerService;
    @Autowired
    PlayerService playerService;
    @Autowired
    UserRoleService userRoleService;
    @Autowired
    PaymentService paymentService;
    @Autowired
    NotificationsService notificationService;


    @GetMapping("")
    public String getAdminPage() {
        return "adminPages/adminPage.html";
    }

    // ================== Add new Admin (Only SUPER_ADMIN Can Do This) ===================
    @GetMapping("signupAdmin")
    public String getSignUpAdminPage() {
        return "adminPages/signupAdmin.html";
    }

    @PostMapping("/signupAdmin")
    public String postSignupAdmin(@Valid UserRegistrationRequest userRequest, BindingResult bindingResult,Model model) {
       return adminService.postSignupAdmin(userRequest, bindingResult,model);
    }

    // =============== Method To Update Admin Information's ==================
    @GetMapping("/updateAdmin")
    public String getEditAdminProfile(Principal principal, Model model) {
       return adminService.getEditAdminProfile(principal , model);
    }

    @PostMapping("/updateAdmin")
    public RedirectView getUpdateAdmin(@Valid UserUpdateRequest userUpdateRequest,BindingResult bindingResult) {
        return adminService.getUpdateAdmin(userUpdateRequest, bindingResult);
    }


    // ============== Add Trainer To The Database  ==============
    @GetMapping("/signupTrainer")
    public String getSignupTrainer() {
        return "adminPages/signupTrainer.html";
    }

    @PostMapping("/signupTrainer")
    public RedirectView signupTrainer(@Valid UserRegistrationRequest userRequest,  TrainerRegistrationRequest trainerRequest, Principal principal,BindingResult bindingResult) {
        return adminService.signupTrainer(userRequest,trainerRequest,principal,bindingResult);
    }

    // ============== Add Player To The Database  ==============
    @GetMapping("/signupPlayer")
    public String getSignupPlayer(Model model) {
        List<PackagesEntity> availablePackages = packageService.getAllPackages();
        model.addAttribute("availablePackages", availablePackages);
        model.addAttribute("paymentMethods", Arrays.asList("Cash", "Visa"));
        return "adminPages/signupPlayer";
    }

    @PostMapping("/signupPlayer")
    public String signupPlayer(@Valid PlayerRegistrationRequest playerRequest,
                                    UserRegistrationRequest userRequest,
                                    Principal principal,BindingResult bindingResult,Model model) {
       return adminService.signupPlayer(playerRequest, userRequest, principal,bindingResult, model);
    }


    // ========== Resubscribe a player to a new package (OR The Same Package) ====================
    @GetMapping("/renewSubscription")
    public String getRenewSubscriptionForm(Model model) {
        List<PlayersEntity> players = playerService.getAllPlayers();
        List<PackagesEntity> availablePackages = packageService.getAllPackages();
        model.addAttribute("players", players);
        model.addAttribute("availablePackages", availablePackages);

        return "adminPages/renewSubscription";
    }

    @PostMapping("/renewSubscription")
    public RedirectView renewSubscription(@RequestParam(name = "playerId") Long playerId,
                                          @RequestParam(name = "newPackageId") Long newPackageId) {

        return adminService.renewSubscription(playerId,newPackageId);
    }


    // ==========  Update the player's account status (And It Will Check Automatically Every Day In Midnight --> PlayerSubscriptionService ) ====================
    @GetMapping("/updateAccountStatus")
    public String getUpdateAccountStatusForm(Model model) {
        List<PlayersEntity> players = playerService.getAllPlayers();
        model.addAttribute("players", players);
        return "adminPages/updateAccountStatus";
    }

    @PostMapping("/updateAccountStatus")
    public RedirectView updateAccountStatus(
            @RequestParam(name = "playerId") Long playerId,
            @RequestParam(name = "accountStatus") String accountStatus) {
        return adminService.updateAccountStatus(playerId,accountStatus);
    }

    // ==========  Add New Package To Database  ====================
    @GetMapping("/addPackage")
    public String getAddPackageForm() {
        return "adminPages/addPackage";
    }

    @PostMapping("/addPackage")
    public RedirectView addPackage( AddPackageRequest packageRequest, Principal principal) {
        return adminService.addPackage(packageRequest , principal);
    }

    // ==========  Add New Class To Database And Assign The Class To Specific Trainer  ====================
    @GetMapping("/addClass")
    public String getAddClassForm(Model model) {
        List<TrainerEntity> trainers = trainerService.getAllTrainer();
        model.addAttribute("trainers", trainers);
        model.addAttribute("classEntity", new ClassesEntity());
        return "adminPages/addClass";
    }

    @PostMapping("/addClass")
    public RedirectView addClass( AddClassRequest classRequest, Principal principal) {
        return adminService.addClass(classRequest , principal);
    }



    // ==========  Add Player To Specific Class ====================
    @GetMapping("/addPlayerToClass")
    public String getAddPlayerToClassForm(Model model) {
        List<PlayersEntity> players = playerService.getAllPlayers();
        List<ClassesEntity> classes = classService.getAllClasses();

        model.addAttribute("players", players);
        model.addAttribute("classes", classes);

        return "adminPages/addPlayerToClass";
    }

    @PostMapping("/addPlayerToClass")
    public RedirectView addPlayerToClass(AddPlayerToClassRequest request) {
        return adminService.addPlayerToClass(request);
    }


    // ==========  Get All Classes In The Database  ====================
    @GetMapping("/allClasses")
    public String getAllClasses(Model model) {
        List<ClassesEntity> classes = classService.getAllClasses();
        model.addAttribute("classes", classes);
        return "adminPages/allClasses";
    }

    // ==========  Get Details For Specific Class  ====================
    @GetMapping("/allClasses/classDetails/{id}")
    public String getClassDetails(@PathVariable Long id, Model model) {
        ClassesEntity classEntity = classService.getClassById(id);
        model.addAttribute("classEntity", classEntity);

        Set<PlayerClassEnrollment> registrations = classEntity.getRegistrations();

        List<PlayersEntity> enrolledPlayers =
                registrations
                        .stream()
                        .map(PlayerClassEnrollment::getPlayer) // takes each PlayerClassEnrollment object from the Stream and applies the getPlayer method to it.
                        .collect(Collectors.toList());

        model.addAttribute("enrolledPlayers", enrolledPlayers);

        return "adminPages/classDetails";
    }

    // ==========  Get All Players In The Database (And Can Search  By UserName OR PhoneNumber) ====================
    @GetMapping("/allplayers")
    public String searchPlayers(@RequestParam(value = "search", required = false) String searchTerm, Model model) {
        List<PlayersEntity> players;

        if (searchTerm != null && !searchTerm.isEmpty()) {
            players = playerService.searchPlayersByUsernameOrPhoneNumber(searchTerm);
        } else {
            players = playerService.getAllPlayers();
        }

        model.addAttribute("players", players);

        return "adminPages/allplayers";
    }

    @GetMapping("/allplayers/{id}")
    public String sendMessageToUser(@PathVariable Long id, Model model) {
        model.addAttribute("receiverId", id);
        return "adminPages/sendMessage";
    }

    // ==========  Admin Can Send Notification (OR Message) To Specific Player  ====================
    @GetMapping("/sendMessage")
    public String getSendMessageForm(@RequestParam Long receiverId, Model model) {
        model.addAttribute("receiverId", receiverId);
        return "adminPages/sendMessage";
    }

    @PostMapping("/sendMessage")
    public RedirectView sendMessage(@RequestParam Long receiverId, @RequestParam String message, Principal principal) {
        String senderUsername = principal.getName();
        UserEntity sender = userService.findUserByUsername(senderUsername);
        UserEntity receiver = userService.findUserById(receiverId);

        NotificationsEntity notification = NotificationsEntity.builder()
                .message(message)
                .sender(sender)
                .receiver(receiver)
                .timeStamp(LocalDateTime.now())
                .build();
        notificationService.saveNotification(notification);
        return new RedirectView("/adminPage/allplayers");
    }

    // =============== Method To Get Admin Information's ==================
    @GetMapping("/adminProfile")
    private String adminProfile(Principal principal, Model model) {
        if (principal != null) {
            String username = principal.getName();
            UserEntity userEntity = userService.findUserByUsername(username);
            if (userEntity == null || userEntity.getRole() == null) {
                return "redirect:/error";
            }
            AdminEntity adminEntity = userEntity.getAdmin();
            model.addAttribute("admin", adminEntity);
            return "adminPages/adminProfile.html";
        }
        return "redirect:/error";
    }


    @GetMapping("/editClassDetails/{id}")
    public String getEditClassDetails(@PathVariable Long id, Model model) {
        ClassesEntity classEntity = classService.getClassById(id);
        List<TrainerEntity> trainers = trainerService.getAllTrainer();
        model.addAttribute("classEntity", classEntity);
        model.addAttribute("trainers", trainers);
        return "adminPages/editClassDetails";
    }
    @PostMapping("/editClassDetails/{id}")
    public RedirectView postEditClassDetails(ClassUpdateRequest classUpdateRequest , Principal principal, @RequestParam Long trainerId) {
        return adminService.postEditClassDetails(classUpdateRequest, principal, trainerId);
    }


}