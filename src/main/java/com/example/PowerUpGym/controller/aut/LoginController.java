package com.example.PowerUpGym.controller.aut;

import com.example.PowerUpGym.bo.auth.LoginRequest;
import com.example.PowerUpGym.entity.users.UserEntity;
import com.example.PowerUpGym.entity.users.UserRoleEntity;
import com.example.PowerUpGym.enums.Role;
import com.example.PowerUpGym.repositories.UserEntityRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {

    @Autowired
    UserEntityRepositories userEntityRepositories;
    @Autowired
    private HttpServletRequest request;

/*
I check if the user is authenticated and has authorities
I use authentication.getAuthorities().stream().anyMatch(...) to check if the user has a specific role (authority)
If the user has the role of ADMIN, I redirect to /adminPage
If the user has the role of PLAYER, I redirect to /playerPage
If the user has the role of TRAINER, I redirect to /trainerPage
If none of this match, I return the login page
*/
@GetMapping("/login")
public String getLoginPage() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.isAuthenticated()) {
        if (authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ADMIN") || a.getAuthority().equals("SUPER_ADMIN"))) {
            return "redirect:/adminPage";
        } else if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("PLAYER"))) {
            return "redirect:/playerPage";
        } else if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("TRAINER"))) {
            return "redirect:/trainerPage";
        }
    }
    return "login.html";
}

    @PostMapping("/login")
    public RedirectView login(LoginRequest loginRequest , RedirectAttributes redir) {

        authWithHttpServletRequest(loginRequest.getUsername() , loginRequest.getPassword());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            redir.addFlashAttribute("InvalidUsernameOrPassword", "Invalid Username or Password");
            return new RedirectView("/login?error=Invalid%20Credentials");
        }

        UserEntity authenticatedUser = userEntityRepositories.findByUsername(loginRequest.getUsername());

        if (authenticatedUser != null) {
            UserRoleEntity userRole = authenticatedUser.getRole();

            if (userRole.getRole() == Role.PLAYER) {
                if (!authenticatedUser.getPlayer().isAccountEnabled()) { // Check if the account is enabled
                    redir.addFlashAttribute("disabledAccount", "Your account is disabled, please renew your subscription");
                    return new RedirectView("/login?error=disabled%account");
                }
                return new RedirectView("/playerPage"); // Redirect to the player page
            } else if (userRole.getRole() == Role.TRAINER) {
                return new RedirectView("/trainerPage"); // Redirect to the trainer page
            } else if (userRole.getRole() == Role.ADMIN || userRole.getRole() == Role.SUPER_ADMIN ) {
                return new RedirectView("/adminPage"); // Redirect to the trainer page
            }

        }
        redir.addFlashAttribute("errorMessage", "Something wrong happened");
        return new RedirectView("/login?error=Invalid%20Credentials");
}

    @GetMapping("/logout")
    public String getLogoutPage() {
        return "login.html";
    }

    public void authWithHttpServletRequest(String username, String password) {
        try {
            request.login(username, password);
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public class ResourceNotFoundException extends RuntimeException
    {
        ResourceNotFoundException(String message)
        {
            super(message);
        }
    }

}
