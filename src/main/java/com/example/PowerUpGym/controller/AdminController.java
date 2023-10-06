package com.example.PowerUpGym.controller;

import com.example.PowerUpGym.entity.classesGym.ClassesEntity;
import com.example.PowerUpGym.entity.classesGym.PlayerClassEnrollment;
import com.example.PowerUpGym.entity.notifications.NotificationsEntity;
import com.example.PowerUpGym.entity.packagesGym.PackagesEntity;
import com.example.PowerUpGym.entity.payments.PaymentsEntity;
import com.example.PowerUpGym.entity.users.AdminEntity;
import com.example.PowerUpGym.entity.users.PlayersEntity;
import com.example.PowerUpGym.entity.users.TrainerEntity;
import com.example.PowerUpGym.entity.users.UserEntity;
import com.example.PowerUpGym.enums.Role;
import com.example.PowerUpGym.repositories.UserEntityRepositories;
import com.example.PowerUpGym.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.annotation.Secured;
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
    UserEntityRepositories userEntityRepositories;
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
    private NotificationsService notificationService;

    @GetMapping("")
    public String getLoginPageAdmin() {
        return "adminPages/adminPage.html";
    }

//    @GetMapping("/signupAdmin")
//    public String getSignupAdmin(){
//        return "signupAdmin";
//    }


//    @PostMapping("/signupAdmin")
//    public RedirectView getSignupTrainer(String fullName, String username, String password, String email, String phoneNumber){
//        // Create a new Admin object
//        AdminEntity admin = new AdminEntity();
//
//        // Create a new UserEntity object and set its properties
//        UserEntity user = new UserEntity();
//        user.setFullName(fullName);
//        user.setUsername(username);
//        user.setEmail(email);
//        user.setPhoneNumber(phoneNumber);
//        String encryptedPassword = passwordEncoder.encode(password);
//        user.setPassword(encryptedPassword);
//
//        // Set the user role to "ADMIN"
//        UserRoleEntity adminRole = new UserRoleEntity();
//        adminRole.setId(2L); // Set the ID of the "ADMIN" role
//        user.setRole(adminRole);
//
//        userService.signupUser(user);
//
//        admin.setUser(user);
//
//        adminService.signupAdmin(admin);
//        authWithHttpServletRequest(username , password);
//
//        return new RedirectView("/index");
//    }

    private UserEntity createUser(String fullName, String username, String email,
                                  String phoneNumber,String image, String password, Role role) {

        UserEntity user = UserEntity.builder()
                .fullName(fullName)
                .username(username)
                .email(email)
                .phoneNumber(phoneNumber)
                .image(image)
                .password(passwordEncoder.encode(password))
                .role(userRoleService.getUserRoleByName(role))
                .build();

        userService.signupUser(user);

        return user;
    }

    @GetMapping("/signupTrainer")
    public String getSignupTrainer() {
        return "adminPages/signupTrainer.html";
    }

    @PostMapping("/signupTrainer")
    public RedirectView signupTrainer(String fullName, String username, String email, String phoneNumber,
                                      String image, String password, int age, String experience, Principal principal) {

        UserEntity user = createUser(fullName, username, email, phoneNumber,image, password, Role.TRAINER);

        TrainerEntity trainerEntity = TrainerEntity.builder()
                .age(age)
                .experience(experience)
                .admin(adminService.getAdminByUsername(principal.getName()))
                .user(user)
                .build();

        trainerService.signupTrainer(trainerEntity);

        return new RedirectView("/adminPage");
    }


    @GetMapping("/signupPlayer")
    public String getSignupPlayer(Model model) {
        List<PackagesEntity> availablePackages = packageService.getAllPackages();
        model.addAttribute("availablePackages", availablePackages);
        model.addAttribute("paymentMethods", Arrays.asList("Cash", "Visa"));
        return "adminPages/signupPlayer";
    }

    @PostMapping("/signupPlayer")
    public RedirectView signupPlayer(
            String fullName, String username, String email, String phoneNumber, String image,
            String password, String address, int age, int height, int weight,
            @RequestParam Long packageId,@RequestParam String paymentMethod, Principal principal) {

        if (principal != null) {
            UserEntity user = createUser(fullName, username, email, phoneNumber, image, password, Role.PLAYER);
            PackagesEntity selectedPackage = packageService.getPackageById(packageId);
            LocalDate startDate = LocalDate.now();
            LocalDate endDate = startDate.plusMonths(selectedPackage.getDuration());
            PlayersEntity player = PlayersEntity.builder()
                    .admin(adminService.getAdminByUsername(principal.getName()))
                    .user(user)
                    .address(address)
                    .age(age)
                    .height(height)
                    .weight(weight)
                    .start_date(startDate)
                    .end_date(endDate)
                    .selectedPackage(selectedPackage)
                    .accountEnabled(true)
                    .build();


            playerService.signupPlayer(player);


            PaymentsEntity payment = PaymentsEntity.builder()
                    .userEntity(player.getUser())
                    .amount(selectedPackage.getPrice())
                    .paymentMethod(paymentMethod)
                    .paymentDate(LocalDate.now())
                    .paymentStatus(true)
                    .build();

            paymentService.savePayment(payment);

            return new RedirectView("/adminPage");
        } else {
            return new RedirectView("/error");
        }
    }



////////////////////////////////////////////////////////////////////////////////

    @GetMapping("/renewSubscription")
    public String getRenewSubscriptionForm(Model model) {
        List<PlayersEntity> players = playerService.getAllPlayers();
        List<PackagesEntity> availablePackages = packageService.getAllPackages();
        model.addAttribute("players", players);
        model.addAttribute("availablePackages", availablePackages);

        return "adminPages/renewSubscription";
    }


// ========== Resubscribe a player to a new package ====================
@PostMapping("/renewSubscription")
public RedirectView resubscribePlayer(@RequestParam(name = "playerId") Long playerId,
                                      @RequestParam(name = "newPackageId") Long newPackageId) {
    PlayersEntity player = playerService.getPlayerById(playerId);
    PackagesEntity newPackage = packageService.getPackageById(newPackageId);

    if (player != null && newPackage != null) {
        LocalDate newEndDate = LocalDate.now().plusMonths(newPackage.getDuration());

        player.setSelectedPackage(newPackage);
        player.setEnd_date(newEndDate);
        player.setAccountEnabled(true);
        playerService.signupPlayer(player);

        return new RedirectView("/adminPage/allplayers");
    } else {
        return new RedirectView("/error");
    }
}
// ========== Resubscribe a player to a new package ====================


// ==========  Update the player's account status  ====================
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

        if (player != null) {
            // Update the player's account status based on the selected option
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
// ==========  Update the player's account status  ====================

    @GetMapping("/addPackage")
    public String getAddPackageForm() {
        return "adminPages/addPackage";
    }

    @PostMapping("/addPackage")
    public RedirectView addPackage(
            String packageName, int price, int duration, String description, Principal principal) {

        AdminEntity admin = adminService.getAdminByUsername(principal.getName());
        PackagesEntity packageEntity = createPackage(packageName, price, duration, description, admin);

        packageService.addPackage(packageEntity);

        return new RedirectView("/adminPage");
    }

    private PackagesEntity createPackage(String packageName, int price, int duration, String description, AdminEntity admin) {
        return PackagesEntity.builder()
                .packageName(packageName)
                .price(price)
                .duration(duration)
                .description(description)
                .admin(admin)
                .build();
    }

    @GetMapping("/addClass")
    public String getAddClassForm(Model model) {
        List<TrainerEntity> trainers = trainerService.getAllTrainer();
        model.addAttribute("trainers", trainers);
        model.addAttribute("classEntity", new ClassesEntity());
        return "adminPages/addClass";
    }

    @PostMapping("/addClass")
    public RedirectView addClass(String className, @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate schedule
            , String description, Long trainerId, Principal principal) {

        TrainerEntity trainer = trainerService.getTrainerById(trainerId);
        AdminEntity admin = adminService.getAdminByUsername(principal.getName());
        ClassesEntity classEntity = createClass(className, schedule, description, trainer, admin);

        classService.addClass(classEntity);

        return new RedirectView("/adminPage");
    }

    private ClassesEntity createClass(String className, LocalDate schedule, String description,
                                      TrainerEntity trainer, AdminEntity admin) {

        return ClassesEntity.builder()
                .className(className)
                .schedule(schedule)
                .description(description)
                .trainer(trainer)
                .admin(admin)
                .build();
    }

//    @PostMapping("/addClass") // Way 2 :  Retrieve the form fields from Model Object
//    public RedirectView addClass(@ModelAttribute("classEntity") ClassesEntity classEntity) {
//        TrainerEntity trainer = trainerService.getTrainerById(classEntity.getTrainer().getId());
//        classEntity.setTrainer(trainer);
//        classService.addClass(classEntity);
//        return new RedirectView("/adminPage");
//    }

    @GetMapping("/addPlayerToClass")
    public String getAddPlayerToClassForm(Model model) {
        List<PlayersEntity> players = playerService.getAllPlayers();
        List<ClassesEntity> classes = classService.getAllClasses();

        model.addAttribute("players", players);
        model.addAttribute("classes", classes);

        return "adminPages/addPlayerToClass";
    }

    @PostMapping("/addPlayerToClass")
    public RedirectView addPlayerToClass(@RequestParam Long playerId, @RequestParam Long classId) {

        PlayersEntity player = playerService.getPlayerById(playerId);
        ClassesEntity classEntity = classService.getClassById(classId);

        PlayerClassEnrollment enrollment = PlayerClassEnrollment.builder()
                .player(player)
                .enrolledClass(classEntity)
                .enrollmentDateTime(LocalDate.now())
                .build();

        playerService.addPlayerClassEnrollment(enrollment);
        return new RedirectView("/adminPage/allClasses");
    }

//    private PlayerClassEnrollment createPlayerClassEnrollment(PlayersEntity player, ClassesEntity classEntity) {
//        PlayerClassEnrollment enrollment = new PlayerClassEnrollment();
//        enrollment.setPlayer(player);
//        enrollment.setEnrolledClass(classEntity);
//        enrollment.setEnrollmentDateTime(LocalDate.now());
//        return enrollment;
//    }

    @GetMapping("/allClasses")
    public String getAllClasses(Model model) {
        List<ClassesEntity> classes = classService.getAllClasses();
        model.addAttribute("classes", classes);
        return "adminPages/allClasses";
    }

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

    @GetMapping("/allplayers")
    public String getManagePlayer(Model model) {
        List<PlayersEntity> players = playerService.getAllPlayers();
        model.addAttribute("players", players);
        return "adminPages/allplayers";
    }

    @GetMapping("/allplayers/{id}")
    public String sendMessageToUser(@PathVariable Long id, Model model) {
        model.addAttribute("receiverId", id);
        return "adminPages/sendMessage";
    }

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

//    private NotificationsEntity createNotification(String message, UserEntity sender, UserEntity receiver) {
//        NotificationsEntity notification = new NotificationsEntity();
//        notification.setMessage(message);
//        notification.setSender(sender);
//        notification.setReceiver(receiver);
//        notification.setTimeStamp(LocalDate.now());
//        return notification;
//    }

}