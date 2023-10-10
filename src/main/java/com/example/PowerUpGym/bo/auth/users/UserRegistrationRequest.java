package com.example.PowerUpGym.bo.auth.users;

import com.example.PowerUpGym.enums.Role;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class UserRegistrationRequest {


    private String fullName;

    private String username;

    private String email;

    private String phoneNumber;

    private String password;

    private String image;

    private Role role;
}
