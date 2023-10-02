package com.example.PowerUpGym.controller;

import com.example.PowerUpGym.entity.classesGym.PlayerClassEnrollment;
import com.example.PowerUpGym.entity.users.PlayersEntity;
import com.example.PowerUpGym.entity.users.UserEntity;
import com.example.PowerUpGym.entity.users.UserRoleEntity;
import com.example.PowerUpGym.repositories.UserEntityRepositories;
import com.example.PowerUpGym.services.ClassEnrollmentService;
import com.example.PowerUpGym.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Controller
@Secured("PLAYER")
public class PlayerController {

    @Autowired
    PlayerService playerService;
    @Autowired ClassEnrollmentService classEnrollmentService;

    @Autowired
    UserEntityRepositories userEntityRepositories;
    @Autowired
    private HttpServletRequest request;

    @Autowired
    PasswordEncoder passwordEncoder;


    @GetMapping("/signup")
    public String getSignupPage() {
        return "signup.html";
    }

    @GetMapping("/index")
    public String index() {
        return "index.html";
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

        // Authenticate the user
        authWithHttpServletRequest(username, password);

        // If the user account is successfully created, redirect to the home page
        // Otherwise, redirect to the signup page with an error message
        return new RedirectView("/playerInfo");
    }





//    @PostMapping("/signup")
//    public RedirectView signup(Model model, String fullName, String username, String password, String email, String phoneNumber, String address, int age, int height, int weight, String image, @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start_date, @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end_date) {
//        // Create a new PlayersEntity object
//        PlayersEntity player = new PlayersEntity();
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
//        userEntityRepositories.save(user);
//
//        // Set the user for the player
//        player.setUser(user);
//
//
//
//        player.setAddress(address);
//        player.setAge(age); // Assuming there's a setAge method in your PlayersEntity
//        player.setHeight(height);
//        player.setWeight(weight);
//        player.setEnd_date(end_date);
//        player.setStart_date(start_date);
//        player.setImage(image);
//
//        // Sign up the user
//        playerService.signupPlayer(player);
//
//        // Authenticate the user
//        authWithHttpServletRequest(username, password);
//
//        // If the user account is successfully created, redirect to the home page
//        // Otherwise, redirect to the signup page with an error message
//        return new RedirectView("/players");
//    }

    @GetMapping("/playerPage")
    public String getLoginPagePlayer() {
        return "playerPage.html";
    }



    public void authWithHttpServletRequest(String username, String password) {
        try {
            request.login(username, password);
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }


    @GetMapping("/playerInfo")
    public String getMyInfo(Principal principal, Model model) {
        if (principal != null) {
            String userName = principal.getName();
            UserEntity userEntity = playerService.findUserByUsername(userName);
//            && userEntity.getRole().getId() == 1
            if (userEntity != null && userEntity.getRole() != null) {
                model.addAttribute("user", userEntity);
                PlayersEntity player = userEntity.getPlayer();
                model.addAttribute("player", player);
                List<PlayerClassEnrollment> enrollments = classEnrollmentService.findByPlayer(player);
                model.addAttribute("enrollments", enrollments);
                return "playerInfo.html";
            }
        }
        return "index.html";
    }


//    @GetMapping("/calculateBMI")
//    public String calculateBMI(@RequestParam("weight") double weight, @RequestParam("height") double height, Model model) {
//        if (weight <= 0 || height <= 0) {
//            model.addAttribute("bmiResult", "Invalid input. Please enter valid values.");
//            model.addAttribute("bmiExplanation", "");
//        } else {
//            // Calculate BMI
//            double bmi = weight / (height * height);
//            model.addAttribute("bmiResult", "Your BMI is: " + String.format("%.2f", bmi));
//
//            String explanation = "";
//            if (bmi < 18.5) {
//                explanation = "You are underweight.";
//            } else if (bmi >= 18.5 && bmi < 24.9) {
//                explanation = "You have a normal weight.";
//            } else if (bmi >= 25 && bmi < 29.9) {
//                explanation = "You are overweight.";
//            } else {
//                explanation = "You are obese.";
//            }
//            model.addAttribute("bmiExplanation", explanation);
//        }
//
//
//
//        return "playerInfo";
//    }



}