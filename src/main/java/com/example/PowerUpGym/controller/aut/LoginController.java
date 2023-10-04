package com.example.PowerUpGym.controller.aut;

import com.example.PowerUpGym.bo.auth.LoginRequest;
import com.example.PowerUpGym.entity.users.AdminEntity;
import com.example.PowerUpGym.entity.users.UserEntity;
import com.example.PowerUpGym.entity.users.UserRoleEntity;
import com.example.PowerUpGym.enums.Role;
import com.example.PowerUpGym.repositories.UserEntityRepositories;
import com.example.PowerUpGym.services.AdminService;
import com.example.PowerUpGym.services.UserRoleService;
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
public class LoginController {

    @Autowired
    private UserService userService;
    @Autowired
    AdminService adminService;
    @Autowired
    UserEntityRepositories userEntityRepositories;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRoleService userRoleService;

    @GetMapping("/login")
    public String getLoginPage() {
        return "login.html";
    }

    @GetMapping("/logout")
    public String getLogoutPage() {
        return "login.html";
    }

    @GetMapping("/signupAdmin")
    public String getSignupAdmin(){
        return "signupAdmin";
    }

    @PostMapping("/login")
    public RedirectView login(LoginRequest loginRequest) {

        authWithHttpServletRequest(loginRequest.getUsername() , loginRequest.getPassword());

        UserEntity authenticatedUser = userEntityRepositories.findByUsername(loginRequest.getUsername());

        if (authenticatedUser != null) {
            UserRoleEntity userRole = authenticatedUser.getRole();

            // Check the role and redirect accordingly
            if (userRole.getRole() == Role.PLAYER) {
                // Redirect to the player page
                return new RedirectView("/playerPage");
            } else if (userRole.getRole() == Role.TRAINER) {
                // Redirect to the trainer page
                return new RedirectView("/trainerPage");
            } else if (userRole.getRole() == Role.ADMIN) {
                // Redirect to the trainer page
                return new RedirectView("/adminPage");
            }
        }

        // If the role doesn't match the expected roles, show an error message
        return new RedirectView("/login?error=Invalid Role");
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

        // Set the user role to "ADMIN"
        UserRoleEntity trainerRole = userRoleService.getUserRoleByName(Role.ADMIN);
        user.setRole(trainerRole);

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
