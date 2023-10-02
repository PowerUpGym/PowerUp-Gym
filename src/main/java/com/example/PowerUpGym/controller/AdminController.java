package com.example.PowerUpGym.controller;

import com.example.PowerUpGym.entity.classesGym.ClassesEntity;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Controller
@Secured("ADMIN") // define a list of security configuration attributes for business methods
@RequestMapping("/adminPage") // base path
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
    private HttpServletRequest request;
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

        // Retrieve the admin entity by username
        AdminEntity admin = adminService.getAdminByUsername(loggedInAdminUsername);

        // Associate the admin with the package
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
    public RedirectView signup(String fullName, String username, String password, String email, String phoneNumber, String address, int age, int height, int weight, String image, @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start_date, @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end_date) {
        // Create a new PlayersEntity object
        PlayersEntity player = new PlayersEntity();

        // Create a new UserEntity object and set its properties
        UserEntity user = new UserEntity();
        user.setFullName(fullName);
        user.setUsername(username);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        String encryptedPassword = passwordEncoder.encode(password);
        user.setPassword(encryptedPassword);

        // Set the user role to "PLAYER"
        UserRoleEntity playerRole = new UserRoleEntity();
        playerRole.setId(1L); // Set the ID of the "PLAYER" role
        user.setRole(playerRole);

        userEntityRepositories.save(user);

        // Set the user for the player
        player.setUser(user);
        player.setAddress(address);
        player.setAge(age);
        player.setHeight(height);
        player.setWeight(weight);
        player.setEnd_date(end_date);
        player.setStart_date(start_date);
        player.setImage(image);

        // Sign up the user
        playerService.signupPlayer(player);

        // If the user account is successfully created, redirect to the home page
        // Otherwise, redirect to the signup page with an error message
        return new RedirectView("/adminPage");
    }

    @GetMapping("/addPackage")
    public String getAddPackageForm() {
        return "adminPages/addPackage"; // Return the name of your HTML form
    }

    @PostMapping("/addPackage")
    public RedirectView addPackage(String packageName, int price, String description, Principal principal) {
        // Create a new PackagesEntity object
        PackagesEntity packageEntity = new PackagesEntity();
        packageEntity.setPackageName(packageName);
        packageEntity.setPrice(price);
        packageEntity.setDescription(description);

        // Get the username of the logged-in admin
        String loggedInAdminUsername = principal.getName();

        // Retrieve the admin entity by username
        AdminEntity admin = adminService.getAdminByUsername(loggedInAdminUsername);

        // Associate the admin with the package
        packageEntity.setAdmin(admin);

        // Save the package to the database
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
        // Retrieve the selected trainer by ID
        TrainerEntity trainer = trainerService.getTrainerById(classEntity.getTrainer().getId());

        // Set the selected trainer for the class
        classEntity.setTrainer(trainer);

        // Save the class to the database
        classService.addClass(classEntity);

        return new RedirectView("/adminPage");
    }


}