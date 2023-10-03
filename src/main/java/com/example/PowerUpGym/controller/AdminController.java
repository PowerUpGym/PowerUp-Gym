package com.example.PowerUpGym.controller;

import com.example.PowerUpGym.entity.classesGym.ClassesEntity;
import com.example.PowerUpGym.entity.classesGym.PlayerClassEnrollment;
import com.example.PowerUpGym.entity.packagesGym.PackagesEntity;
import com.example.PowerUpGym.entity.users.*;
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
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.time.LocalDate;
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


    @GetMapping("/signupTrainer")
    public String getSignupTrainer(){
        return "adminPages/signupTrainer.html";
    }

    @PostMapping("/signupTrainer")
    public RedirectView getSignupTrainer(String fullName, String username, String password, String email, String phoneNumber,int age , String experience , Principal principal){

        TrainerEntity trainerEntity = new TrainerEntity();

        UserEntity user = new UserEntity();
        user.setFullName(fullName);
        user.setUsername(username);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        String encryptedPassword = passwordEncoder.encode(password);
        user.setPassword(encryptedPassword);

        UserRoleEntity trainerRole = new UserRoleEntity();
        trainerRole.setId(3L); // Set the ID of the "Trainer" role
        user.setRole(trainerRole);

        userService.signupUser(user);

        trainerEntity.setAge(age);
        trainerEntity.setExperience(experience);

        //////////////// this is temporary
        String loggedInAdminUsername = principal.getName();
        AdminEntity admin = adminService.getAdminByUsername(loggedInAdminUsername);
        trainerEntity.setAdmin(admin);
        ////////////////

        trainerEntity.setUser(user);

        trainerService.signupTrainer(trainerEntity);

        return new RedirectView("/adminPage");
    }

    @GetMapping("/signup")
    public String getSignupPage() {
        return "adminPages/signup.html";
    }

    @PostMapping("/signup")
    public RedirectView signup(String fullName, String username, String password, String email, String phoneNumber, String address, int age, int height, int weight, String image, @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start_date, @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end_date , Principal principal) {

        PlayersEntity player = new PlayersEntity();

        UserEntity user = new UserEntity();
        user.setFullName(fullName);
        user.setUsername(username);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        String encryptedPassword = passwordEncoder.encode(password);
        user.setPassword(encryptedPassword);

        UserRoleEntity playerRole = new UserRoleEntity();
        playerRole.setId(1L); // Set the ID of the "PLAYER" role
        user.setRole(playerRole);

        userEntityRepositories.save(user);

        /////////////////////////////////////////////
        String loggedInAdminUsername = principal.getName();
        AdminEntity admin = adminService.getAdminByUsername(loggedInAdminUsername);
        player.setAdmin(admin);
        /////////////////////////////

        player.setUser(user);
        player.setAddress(address);
        player.setAge(age);
        player.setHeight(height);
        player.setWeight(weight);
        player.setEnd_date(end_date);
        player.setStart_date(start_date);
        player.setImage(image);

        playerService.signupPlayer(player);

        return new RedirectView("/adminPage");
    }

    @GetMapping("/addPackage")
    public String getAddPackageForm() {
        return "adminPages/addPackage";
    }

    @PostMapping("/addPackage")
    public RedirectView addPackage(String packageName, int price, String description, Principal principal) {
        // Create a new PackagesEntity object
        PackagesEntity packageEntity = new PackagesEntity();
        packageEntity.setPackageName(packageName);
        packageEntity.setPrice(price);
        packageEntity.setDescription(description);

        String loggedInAdminUsername = principal.getName();

        AdminEntity admin = adminService.getAdminByUsername(loggedInAdminUsername);

        packageEntity.setAdmin(admin);

        packageService.addPackage(packageEntity);

        return new RedirectView("/adminPage");
    }

    @GetMapping("/addClass")
    public String getAddClassForm(Model model) {
        List<TrainerEntity> trainers = trainerService.getAllTrainer();
        model.addAttribute("trainers", trainers);
        model.addAttribute("classEntity", new ClassesEntity());
        return "adminPages/addClass";
    }

    @PostMapping("/addClass")
    public RedirectView addClass(@ModelAttribute("classEntity") ClassesEntity classEntity) {
        TrainerEntity trainer = trainerService.getTrainerById(classEntity.getTrainer().getId());

        classEntity.setTrainer(trainer);

        classService.addClass(classEntity);

        return new RedirectView("/adminPage");
    }

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

        PlayerClassEnrollment enrollment = new PlayerClassEnrollment();
        enrollment.setPlayer(player);
        enrollment.setEnrolledClass(classEntity);
        enrollment.setEnrollmentDateTime(LocalDate.now());

        playerService.addPlayerClassEnrollment(enrollment);

        return new RedirectView("/adminPage/allClasses");
    }





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

        List<PlayersEntity> enrolledPlayers = registrations
                .stream()
                .map(PlayerClassEnrollment::getPlayer)
                .collect(Collectors.toList());

        model.addAttribute("enrolledPlayers", enrolledPlayers);

        return "adminPages/classDetails";
    }




}