<<<<<<< Updated upstream
package com.example.PowerUpGym.controller;

import com.example.PowerUpGym.entity.users.AdminEntity;
import com.example.PowerUpGym.entity.users.UserEntity;
import com.example.PowerUpGym.entity.users.UserRoleEntity;
import com.example.PowerUpGym.repositories.UserEntityRepositories;
import com.example.PowerUpGym.services.AdminService;
import com.example.PowerUpGym.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@Controller
public class AdminController {
    @Autowired
    AdminService adminService;
    @Autowired
    UserEntityRepositories userEntityRepositories;
    @Autowired
    private HttpServletRequest request;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserService userService;


    @GetMapping("/loginAdmin")
    public String getLoginTrainer(){
        return "loginAdmin.html";
    }
    @GetMapping("/signupAdmin")
    public String getSignupTrainer(){
        return "signupAdmin.html";
    }

    @PostMapping("/signupAdmin")
    public RedirectView getSignupTrainer(String fullName, String username, String password, String email, String phoneNumber){

        AdminEntity admin = new AdminEntity();

        UserEntity user = new UserEntity();
        user.setFullName(fullName);
        user.setUsername(username);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        String encryptedPassword = passwordEncoder.encode(password);
        user.setPassword(encryptedPassword);

        UserRoleEntity adminRole = new UserRoleEntity();
        adminRole.setId(1L); // Set the ID of the "ADMIN" role
        user.setRole(adminRole);

        userService.signupUser(user);

        adminService.signupAdmin(admin);
        authWithHttpServletRequest(username , password);

        return new RedirectView("/index");
    }

    public void authWithHttpServletRequest(String username, String password) {
        try {
            request.login(username, password);
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }
=======
package com.example.PowerUpGym.controller;

import com.example.PowerUpGym.entity.users.*;
import com.example.PowerUpGym.repositories.UserEntityRepositories;
import com.example.PowerUpGym.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;

@Controller
@Secured("ADMIN") // define a list of security configuration attributes for business methods
//@RequestMapping("/adminPage") // base path
public class AdminController {
    @Autowired
    AdminService adminService;
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


        @GetMapping("/signupAdmin")
    public String getSignupAdmin(){
        return "signupAdmin.html";
    }


    @PostMapping("/signupAdmin")
    public RedirectView getSignupAdmin(String fullName, String username, String password, String email, String phoneNumber){
        // Create a new Admin object
        AdminEntity admin = new AdminEntity();

        // Create a new UserEntity object and set its properties
        UserEntity user = new UserEntity();
        user.setFullName(fullName);
        user.setUsername(username);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        String encryptedPassword = passwordEncoder.encode(password);
        user.setPassword(encryptedPassword);

        // Set the user role to "ADMIN"
        UserRoleEntity adminRole = new UserRoleEntity();
        adminRole.setId(2L); // Set the ID of the "ADMIN" role
        user.setRole(adminRole);
        userService.signupUser(user);
        admin.setUser(user);
        adminService.signupAdmin(admin);
//        authWithHttpServletRequest(username , password);

        return new RedirectView("/loginAdmin");
    }
    @GetMapping("/loginAdmin")
    public String getLoginAdmin(){
        return "loginAdmin.html";
    }
    @PostMapping("/loginAdmin")
    public RedirectView postLoginAdmin(String username,String password){
        authWithHttpServletRequest(username,password);
        HttpSession session = request.getSession();
        List<PlayersEntity> allPlayers = playerService.getAllPlayers();
        List<TrainerEntity> allTrainers = trainerService.getAllTrainer();
        List<UserEntity> allUsers=userService.getAllUsers();
        session.setAttribute("allPlayers", allPlayers);
        session.setAttribute("allTrainers", allTrainers);
        session.setAttribute("allUsers", allUsers);
        return new RedirectView("/adminpage");
    }


    @GetMapping("/signupTrainer")
    public String getSignupTrainer(){
        return "adminPages/signupTrainer.html";
    }

    @PostMapping("/signupTrainer")
    public RedirectView getSignupTrainer(String fullName, String username, String password, String email, String phoneNumber,int age , String experience){

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

        trainerEntity.setAdmin(adminService.getAllAdmins().get(0));

        ////////////////
        trainerEntity.setUser(user);

        trainerService.signupTrainer(trainerEntity);

        return new RedirectView("/adminPage");
    }
    @PostMapping("/signupAdmin")
    public RedirectView getSignupAdmin(String fullName, String username, String password, String email, String phoneNumber){

        AdminEntity admin = new AdminEntity();

        UserEntity user = new UserEntity();
        user.setFullName(fullName);
        user.setUsername(username);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        String encryptedPassword = passwordEncoder.encode(password);
        user.setPassword(encryptedPassword);

        UserRoleEntity adminRole = new UserRoleEntity();
        adminRole.setId(1L); // Set the ID of the "ADMIN" role
        user.setRole(adminRole);
        admin.setUser(user);
        userService.signupUser(user);

        adminService.signupAdmin(admin);
//        authWithHttpServletRequest(username , password);

        return new RedirectView("/index");
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
    @GetMapping("/managePlayer")
    public String getManagePlayer() {
        return "adminPages/managePlayer.html";
    }
    @GetMapping("/manageTrainer")
    public String getManageTrainer() {
        return "adminPages/manageTrainer.html";
    }


    @GetMapping("/addPackage")
    public String getAddPackageForm() {
        return "adminPages/addPackage.html"; // Return the name of your HTML form
    }

//    @PostMapping("/addPackage")
//    public RedirectView addPackage(String packageName, int price, String description, Long id) {
//        // Create a new PackagesEntity object
//        PackagesEntity packageEntity = new PackagesEntity();
//        packageEntity.setPackageName(packageName);
//        packageEntity.setPrice(price);
//        packageEntity.setDescription(description);
//
//        // Set the admin for the package (you can retrieve the admin as needed)
//
//        AdminEntity admin = adminService.getAdminById(adminId); // Replace with actual logic to get the admin
//        packageEntity.setAdmin(admin);
//
//        // Save the package to the database
//        packageService.addPackage(packageEntity);
//
//        return new RedirectView("/adminpage");
//    }
public void authWithHttpServletRequest(String username, String password) {
    try {
        request.login(username, password);
    } catch (ServletException e) {
        e.printStackTrace();
    }
}


>>>>>>> Stashed changes
}