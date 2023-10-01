//package com.example.PowerUpGym.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
//
//    @Autowired
//    private SimpleUrlAuthenticationSuccessHandler successHandler;
//
//    @Bean
//    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
//        return new CustomAuthenticationSuccessHandler();
//    }
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
//
//        // Get the user's role.
//        String role = authentication.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .findFirst()
//                .orElse(null);
//
//        // Redirect the user to the appropriate page based on their role.
//        if ("PLAYER".equals(role)) {
//            successHandler.setDefaultTargetUrl("/playerPage");
//        } else if ("TRAINER".equals(role)) {
//            successHandler.setDefaultTargetUrl("/trainerPage");
//        } else if ("ADMIN".equals(role)) {
//            successHandler.setDefaultTargetUrl("/adminPage");
//        } else {
//            successHandler.setDefaultTargetUrl("/");
//        }
//
//        successHandler.onAuthenticationSuccess(request, response, authentication);
//    }
//}
