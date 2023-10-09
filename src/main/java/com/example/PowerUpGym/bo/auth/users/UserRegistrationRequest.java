package com.example.PowerUpGym.bo.auth.users;

import com.example.PowerUpGym.Validation.FullName;
import com.example.PowerUpGym.Validation.ValidPassword;
import com.example.PowerUpGym.enums.Role;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class UserRegistrationRequest {
    @NotEmpty(message = "Full name must not be empty")
    @FullName(message = "Full name must be between 3-20")
    private String fullName;
    private String username;
    private String email;
    private String phoneNumber;
    @ValidPassword
    private String password;
    private String image;
    private Role role;
}
