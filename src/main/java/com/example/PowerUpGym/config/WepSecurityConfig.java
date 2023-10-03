package com.example.PowerUpGym.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WepSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .cors().disable()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/" , "/index" , "/login" , "/signupAdmin").permitAll()
                // "/signup" ,"/signupTrainer", "/loginTrainer","/signupAdmin","/loginAdmin"
                .antMatchers("/Css/**", "/Js/**").permitAll()
                .antMatchers("/adminPage/**").hasAuthority("ADMIN")
                .antMatchers("/trainerPage/**").hasAuthority("TRAINER") // hasRole("TRAINER")
                .antMatchers("/playerPage/**").hasAuthority("PLAYER")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .loginProcessingUrl("/perform_login")
                .defaultSuccessUrl("/playerInfo", true)
                .failureUrl("/login?error=true")
                .and()
                .logout()
//                    .logoutUrl("/perform_logout")
                .logoutSuccessUrl("/login")
                .deleteCookies("JSESSIONID");
//                .and()
//                .sessionManagement()
//                    .invalidSessionUrl("/sessionTimeout");
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


}