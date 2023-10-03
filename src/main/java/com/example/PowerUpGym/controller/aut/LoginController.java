package com.example.PowerUpGym.controller.aut;

import com.example.PowerUpGym.entity.users.AdminEntity;
import com.example.PowerUpGym.entity.users.TrainerEntity;
import com.example.PowerUpGym.entity.users.UserEntity;
import com.example.PowerUpGym.entity.users.UserRoleEntity;
import com.example.PowerUpGym.enums.Role;
import com.example.PowerUpGym.repositories.UserEntityRepositories;
import com.example.PowerUpGym.services.AdminService;
import com.example.PowerUpGym.services.TrainerService;
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

//    @Autowired
//    TrainerService trainerService;

    @Autowired
    UserEntityRepositories userEntityRepositories;
    @Autowired
    private HttpServletRequest request;

    @Autowired
    PasswordEncoder passwordEncoder;

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

//    @PostMapping("/login")
//    public RedirectView login(HttpServletRequest request) {
//        String username = request.getParameter("username");
//        String password = request.getParameter("password");
//
//        // Authenticate the user.
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if (authentication == null) {
//            // The user is not authenticated.
//            return new RedirectView("/login?error=true");
//        } else {
//            // The user is authenticated.
//
//            // Get the user's role.
//            String role = authentication.getAuthorities().stream().findFirst().map(GrantedAuthority::getAuthority).orElse("");
//
//            // Redirect the user to the appropriate page based on their role.
//            if (role.equals("ADMIN")) {
//                return new RedirectView("/adminPage");
//            } else if (role.equals("TRAINER")) {
//                return new RedirectView("/trainerPage");
//            } else if (role.equals("PLAYER")) {
//                return new RedirectView("/playerPage");
//            } else {
//                return new RedirectView("/login?error=true");
//            }
//        }
//    }
    @PostMapping("/login")
    public RedirectView login(String username, String password) {
        // Authenticate the user
        authWithHttpServletRequest(username, password);
        // Check the role of the authenticated user
        UserEntity authenticatedUser = userEntityRepositories.findByUsername(username);

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


//    @GetMapping("/signupTrainer")
//    public String getSignupTrainer(){
//        return "signupTrainer";
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

//    @PostMapping("/signupTrainer")
//    public RedirectView getSignupTrainer(String fullName, String username, String password, String email, String phoneNumber,int age , String experience){
//
//        TrainerEntity trainerEntity = new TrainerEntity();
//
//        UserEntity user = new UserEntity();
//        user.setFullName(fullName);
//        user.setUsername(username);
//        user.setEmail(email);
//        user.setPhoneNumber(phoneNumber);
//        String encryptedPassword = passwordEncoder.encode(password);
//        user.setPassword(encryptedPassword);
//
//        UserRoleEntity trainerRole = new UserRoleEntity();
//        trainerRole.setId(3L); // Set the ID of the "Trainer" role
//        user.setRole(trainerRole);
//
//        userService.signupUser(user);
//
//        trainerEntity.setAge(age);
//        trainerEntity.setExperience(experience);
//
//        //////////////// this is temporary
//
//        trainerEntity.setAdmin(adminService.getAllAdmins().get(0));
//
//        ////////////////
//        trainerEntity.setUser(user);
//
//        trainerService.signupTrainer(trainerEntity);
//        authWithHttpServletRequest(username , password);
//
//        return new RedirectView("/index");
//    }

    public void authWithHttpServletRequest(String username, String password) {
        try {
            request.login(username, password);
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }


}
