package com.example.PowerUpGym.controller.aut;

import com.example.PowerUpGym.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {

    @Autowired
    private PlayerService playerService;


    @GetMapping("/login")
    public String getLoginPage() {
        return "login.html";
    }

    @GetMapping("/playerPage")
    public String getLoginPagePlayer() {
        return "playerPage.html";
    }

    @PostMapping("/login")
    public RedirectView login(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Authenticate the user.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            // The user is not authenticated.
            return new RedirectView("/login?error=true");
        } else {
            // The user is authenticated.

            // Get the user's role.
            String role = authentication.getAuthorities().stream().findFirst().map(GrantedAuthority::getAuthority).orElse("");

            // Redirect the user to the appropriate page based on their role.
            if (role.equals("ADMIN")) {
                return new RedirectView("/adminPage");
            } else if (role.equals("TRAINER")) {
                return new RedirectView("/trainerPage");
            } else if (role.equals("PLAYER")) {
                return new RedirectView("/playerPage");
            } else {
                return new RedirectView("/login?error=true");
            }
        }
    }
//    @PostMapping("/login")
//    public RedirectView login(String username, String password) {
//        // Authenticate the user
//        authWithHttpServletRequest(username, password);
//
//        // Check the role of the authenticated user
//        UserEntity authenticatedUser = userEntityRepositories.findByUsername(username);
//
//        if (authenticatedUser != null) {
//            UserRoleEntity userRole = authenticatedUser.getRole();
//
//            // Check the role and redirect accordingly
//            if (userRole.getRole() == Role.PLAYER) {
//                // Redirect to the player dashboard
//                return new RedirectView("/playerPage");
//            } else if (userRole.getRole() == Role.TRAINER) {
//                // Redirect to the trainer dashboard
//                return new RedirectView("/trainerPage");
//            }
//        }
//
//        // If the role doesn't match the expected roles, show an error message
//        return new RedirectView("/login?error=Invalid role");
//    }
@Autowired
private HttpServletRequest request;
    public void authWithHttpServletRequest(String username, String password) {
        try {
            request.login(username, password);
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }


}
