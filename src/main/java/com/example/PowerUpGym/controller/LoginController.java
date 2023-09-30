package com.example.PowerUpGym.controller;


import com.example.PowerUpGym.entity.users.PlayersEntity;
import com.example.PowerUpGym.entity.users.UserEntity;
import com.example.PowerUpGym.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import java.time.LocalDate;

@Controller
public class LoginController {

//    @Autowired
//    private PlayerService playerService;
//    @Autowired
//    private UserEntity userEntity;


//    @GetMapping("/login")
//    public String showLogin(Model model) {
//        return "login.html";
//    }
//
//    @PostMapping("/login")
//    public String login(Model model, String username, String password) {
//        // Authenticate the user
//        // If the user is authenticated, redirect to the home page
//        // Otherwise, redirect to the login page with an error message
//        return "redirect:/home";
//    }
//
//    @GetMapping("/signup")
//    public String showSignup(Model model) {
//        return "signup";
//    }

//    @PostMapping("/signup")
//    public RedirectView signup(Model model, String fullName, String username, String password,   String email, String phoneNumber , String address , String age , int height , int weight , String image , LocalDate start_date, LocalDate end_date ) {
//        // Create a new user account
//        PlayersEntity player = new PlayersEntity();
//        player.getUser().setFullName(fullName);
//        player.getUser().setUsername(username);
//        player.getUser().setEmail(email);
//        player.getUser().setEmail(email);
//        player.getUser().setPhoneNumber(phoneNumber);
//        player.getUser().setPassword(password);
////        String encryptedPassword = passwordEncoder.encode(password);
//        player.setAddress(address);
//        player.setAddress(age);
//        player.setHeight(height);
//        player.setWeight(weight);
//        player.setEnd_date(end_date);
//        player.setStart_date(start_date);
//        player.setImage(image);
//
//        playerService.signupPlayer(player);
//        // If the user account is successfully created, redirect to the home page
//        // Otherwise, redirect to the signup page with an error message
//        return new RedirectView("players");
//    }


}
