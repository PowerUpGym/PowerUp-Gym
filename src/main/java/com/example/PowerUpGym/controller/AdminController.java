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


//    @GetMapping("/loginAdmin")
//    public String getLoginTrainer(){
//        return "loginAdmin.html";
//    }

    @GetMapping("/adminPage")
    public String getLoginPagePlayer() {
        return "adminPage.html";
    }
    @GetMapping("/signupAdmin")
    public String getSignupTrainer(){
        return "signupAdmin.html";
    }

    @PostMapping("/signupAdmin")
    public RedirectView getSignupTrainer(String fullName, String username, String password, String email, String phoneNumber){
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
}