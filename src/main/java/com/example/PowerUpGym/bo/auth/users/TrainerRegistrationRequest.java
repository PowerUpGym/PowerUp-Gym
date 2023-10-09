package com.example.PowerUpGym.bo.auth.users;

import com.example.PowerUpGym.Validation.ValidPassword;
import com.example.PowerUpGym.enums.Role;
import lombok.*;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Validated

public class TrainerRegistrationRequest {
    private String fullName;
    private String username;
    private String email;
    private String phoneNumber;
    @ValidPassword
    private String password;
    private Role role;
    private int age;
    private String experience;
}
