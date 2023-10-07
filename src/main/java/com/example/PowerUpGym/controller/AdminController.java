package com.example.PowerUpGym.controller;

import com.example.PowerUpGym.bo.auth.AddClassRequest;
import com.example.PowerUpGym.bo.auth.AddPackageRequest;
import com.example.PowerUpGym.bo.auth.AddPlayerToClassRequest;
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
import com.example.PowerUpGym.services.*;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
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
    public RedirectView postSignupAdmin(@ModelAttribute UserRegistrationRequest userRequest) {
        UserRoleEntity userRole = userRoleService.findRoleByRole(Role.ADMIN);

        if (userRole == null) {
            throw new RuntimeException("Role not found: " + userRequest.getRole());
        }

        UserEntity user = createUserFromRequest(userRequest, userRole.getRole());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setImage("/assets/profileImg.png");

        userService.signupUser(user);

        AdminEntity admin = AdminEntity.builder().user(user).build();
        adminService.signupAdmin(admin);

        return new RedirectView("/adminPage");
    }

    // =============== Method To Update Admin Information's ==================
    @GetMapping("/updateAdmin")
    public String getEditAdminProfile(Principal principal, Model model) {
        if (principal != null) {
            String username = principal.getName();
            UserEntity userEntity = adminService.findAdminByUsername(username);

            if (userEntity != null) {
                model.addAttribute("user", userEntity);
                AdminEntity admin = userEntity.getAdmin();
                model.addAttribute("admin", admin);
                return "adminPages/updateAdmin.html";
            }
        }
        return "redirect:/error";
    }

    @PostMapping("/updateAdmin")
    public RedirectView getUpdateAdmin(UserUpdateRequest userUpdateRequest) {

        UserEntity existingUser = userService.findUserById(userUpdateRequest.getUserId());

        UserEntity updatedUser = updateUser(existingUser, userUpdateRequest);
        userService.saveUser(updatedUser);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UsernamePasswordAuthenticationToken updatedAuthentication = new UsernamePasswordAuthenticationToken(updatedUser.getUsername(), authentication.getCredentials(), authentication.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(updatedAuthentication);

        return new RedirectView("adminProfile");
    }

    // ============== Helper Method To Update User ==============
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

    // ============== Helper Method To Create User From UserRegistrationRequest ==============
    private UserEntity createUserFromRequest(UserRegistrationRequest userRequest, Role role) {
        UserRoleEntity userRole = userRoleService.findRoleByRole(role);

        if (userRole == null) {
            // Handle the case where the role doesn't exist in the database
            throw new RuntimeException("Role not found: " + role);
        }

        return UserEntity.builder()
                .fullName(userRequest.getFullName())
                .username(userRequest.getUsername())
                .email(userRequest.getEmail())
                .phoneNumber(userRequest.getPhoneNumber())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .role(userRole)
                .image(userRequest.getImage())
                .build();
    }


    // ============== Helper Method To Create Trainer From TrainerRegistrationRequest ==============
    private TrainerEntity createTrainerFromRequest(TrainerRegistrationRequest trainerRequest, UserEntity user, String adminUsername) {
        return TrainerEntity.builder()
                .age(trainerRequest.getAge())
                .experience(trainerRequest.getExperience())
                .admin(adminService.getAdminByUsername(adminUsername))
                .user(user)
                .build();
    }

    // ============== Add Trainer To The Database  ==============
    @GetMapping("/signupTrainer")
    public String getSignupTrainer() {
        return "adminPages/signupTrainer.html";
    }

    @PostMapping("/signupTrainer")
    public RedirectView signupTrainer(@ModelAttribute UserRegistrationRequest userRequest, @ModelAttribute TrainerRegistrationRequest trainerRequest, Principal principal) {

        if (userRequest.getImage().isEmpty()) {
            userRequest.setImage("/assets/profileImg.png");
        }

        UserRoleEntity trainerRole = userRoleService.findRoleByRole(Role.TRAINER);
        UserEntity user = createUserFromRequest(userRequest, trainerRole.getRole());

        userService.signupUser(user);

        TrainerEntity trainerEntity = createTrainerFromRequest(trainerRequest, user, principal.getName());

        trainerService.signupTrainer(trainerEntity);

        return new RedirectView("/adminPage");
    }

    // ============== Helper Method To Create Player From PlayerRegistrationRequest ==============
    private PlayersEntity createPlayerFromRequest(PlayerRegistrationRequest playerRequest, UserEntity user, String adminUsername) {
        PackagesEntity selectedPackage = packageService.getPackageById(playerRequest.getPackageId());
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusMonths(selectedPackage.getDuration());

        return PlayersEntity.builder()
                .admin(adminService.getAdminByUsername(adminUsername))
                .user(user)
                .address(playerRequest.getAddress())
                .age(playerRequest.getAge())
                .height(playerRequest.getHeight())
                .weight(playerRequest.getWeight())
                .start_date(startDate)
                .end_date(endDate)
                .selectedPackage(selectedPackage)
                .accountEnabled(true)
                .build();
    }

    // ============== Helper Method To Create Payment ==============
    private PaymentsEntity createPaymentForPlayer(PlayersEntity player, String paymentMethod) {
        return PaymentsEntity.builder()
                .userEntity(player.getUser())
                .amount(player.getSelectedPackage().getPrice())
                .paymentMethod(paymentMethod) // Use the parameter
                .paymentDate(LocalDate.now())
                .paymentStatus(true)
                .build();
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
    public RedirectView signupPlayer(
            @ModelAttribute PlayerRegistrationRequest playerRequest,
            @ModelAttribute UserRegistrationRequest userRequest,
            Principal principal) {

        if (userRequest.getImage().isEmpty()) {
            userRequest.setImage("/assets/profileImg.png");
        }

        UserRoleEntity playerRole = UserRoleEntity.builder().role(Role.PLAYER).build();
        UserEntity user = createUserFromRequest(userRequest, playerRole.getRole());
        PlayersEntity player = createPlayerFromRequest(playerRequest, user, principal.getName());
        PaymentsEntity payment = createPaymentForPlayer(player, playerRequest.getPaymentMethod());

        userService.signupUser(user);
        playerService.signupPlayer(player);
        paymentService.savePayment(payment);
        //    sendPasswordViaSMS(playerRequest.getPhoneNumber(), playerRequest.getPassword());

        return new RedirectView("/adminPage");
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
        PlayersEntity player = playerService.getPlayerById(playerId);
        PackagesEntity newPackage = packageService.getPackageById(newPackageId);

        if (player != null && newPackage != null) {
            int newAmount = newPackage.getPrice();
            player.setSelectedPackage(newPackage);
            LocalDate newEndDate = LocalDate.now().plusMonths(newPackage.getDuration());
            player.setEnd_date(newEndDate);
            playerService.signupPlayer(player);
            PaymentsEntity payment = paymentService.getPaymentByPlayer(player);

            if (payment != null) {
                payment.setAmount(newAmount);
                paymentService.savePayment(payment);
            } else {
                return new RedirectView("/errorNullPayment");
            }
            return new RedirectView("/adminPage/allplayers");
        } else {
            return new RedirectView("/error");
        }
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
        PlayersEntity player = playerService.getPlayerById(playerId);

        if (player != null) { // Update the player's account status based on the selected option
            if ("ENABLED".equals(accountStatus)) {
                player.setAccountEnabled(true);
            } else if ("DISABLED".equals(accountStatus)) {
                player.setAccountEnabled(false);
            }

            playerService.signupPlayer(player);

            return new RedirectView("/adminPage/allplayers");
        } else {
            return new RedirectView("/error");
        }
    }

    // ==========  Add New Package To Database  ====================
    @GetMapping("/addPackage")
    public String getAddPackageForm() {
        return "adminPages/addPackage";
    }

    @PostMapping("/addPackage")
    public RedirectView addPackage(@ModelAttribute AddPackageRequest packageRequest, Principal principal) {
        AdminEntity admin = adminService.getAdminByUsername(principal.getName());
        PackagesEntity packageEntity = createPackage(packageRequest, admin);
        packageService.addPackage(packageEntity);
        return new RedirectView("/adminPage");
    }

    // ==========  Helper Method To Create Package From AddPackageRequest  ====================
    private PackagesEntity createPackage(AddPackageRequest packageRequest, AdminEntity admin) {
        return PackagesEntity.builder()
                .packageName(packageRequest.getPackageName())
                .price(packageRequest.getPrice())
                .duration(packageRequest.getDuration())
                .description(packageRequest.getDescription())
                .admin(admin)
                .build();
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
    public RedirectView addClass(@ModelAttribute AddClassRequest classRequest, Principal principal) {
        TrainerEntity trainer = trainerService.getTrainerById(classRequest.getTrainerId());
        AdminEntity admin = adminService.getAdminByUsername(principal.getName());
        ClassesEntity classEntity = createClass(classRequest, trainer, admin);
        classService.addClass(classEntity);
        return new RedirectView("/adminPage");
    }

    // ==========  Helper Method To Create Class From AddClassRequest  ====================
    private ClassesEntity createClass(AddClassRequest classRequest, TrainerEntity trainer, AdminEntity admin) {
        return ClassesEntity.builder()
                .className(classRequest.getClassName())
                .scheduleDescription(classRequest.getScheduleDescription())
                .description(classRequest.getDescription())
                .trainer(trainer)
                .admin(admin)
                .build();
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
    public RedirectView addPlayerToClass(@ModelAttribute AddPlayerToClassRequest request) {
        Long selectedPlayerId = Long.parseLong(request.getPlayerId());

        PlayersEntity player = playerService.getPlayerById(selectedPlayerId);
        ClassesEntity classEntity = classService.getClassById(request.getClassId());

        PlayerClassEnrollment enrollment = PlayerClassEnrollment.builder()
                .player(player)
                .enrolledClass(classEntity)
                .enrollmentDateTime(LocalDate.now())
                .build();

        playerService.addPlayerClassEnrollment(enrollment);
        return new RedirectView("/adminPage/allClasses");
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


    // ========== Method to send password via SMS using Twilio ============
    private void sendPasswordViaSMS(String phoneNumber, String password) {
        // Your Twilio Account SID and Auth Token
        String ACCOUNT_SID = "AC406561e7beb7c929d6719c7236d85bed";
        String AUTH_TOKEN = "62c0fa1180daf3956b38c4c67e2b6d1f";
        // Initialize Twilio with your credentials
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        // Create a message
        Message message = Message.creator(
                        new PhoneNumber(phoneNumber), // Player's phone number
                        new PhoneNumber("+14345973383"), // Your Twilio phone number
                        "Your password is: " + password)
                .create();
        System.out.println("Password sent via SMS. Message SID: " + message.getSid());
    }

    // =============== Method To Get Admin Information's ==================
    @GetMapping("/adminProfile")
    private String adminProfile(Principal principal, Model model) {
        if (principal != null) {
            String username = principal.getName();
            UserEntity userEntity = adminService.findAdminByUsername(username);
            if (userEntity == null || userEntity.getRole() == null) {
                return "redirect:/error";
            }
            AdminEntity adminEntity = userEntity.getAdmin();
            model.addAttribute("admin", adminEntity);
            return "adminPages/adminProfile.html";
        }
        return "redirect:/error";
    }


}