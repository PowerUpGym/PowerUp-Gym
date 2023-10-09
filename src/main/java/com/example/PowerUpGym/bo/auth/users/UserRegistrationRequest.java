package com.example.PowerUpGym.bo.auth.users;

import com.example.PowerUpGym.Validation.FullName;
import com.example.PowerUpGym.enums.Role;
import lombok.*;
import org.springframework.validation.annotation.Validated;
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

    @NotEmpty(message = "username must not be empty")
    @FullName(message = "username must be between 3-20")
    private String username;

    @NotEmpty(message = "email must not be empty")
    @FullName(message = "email must be between 3-20")
    private String email;

    @NotEmpty(message = "phoneNumber must not be empty")
    @FullName(message = "phoneNumber must be between 3-20")
    private String phoneNumber;

    @NotEmpty(message = "password must not be empty")
    @FullName(message = "password must be between 3-20")
    private String password;

    private String image;
    private Role role;
}
