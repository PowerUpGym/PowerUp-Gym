package com.example.PowerUpGym.bo.auth.users;

import com.example.PowerUpGym.Validation.FullName;
import com.example.PowerUpGym.Validation.ValidPassword;
import com.example.PowerUpGym.Validation.validPhone.PhoneNumber;
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

    private String email;

    @NotEmpty(message = "Phone Number must not be empty")
    @PhoneNumber(message = "Phone Number must be Correct")
    private String phoneNumber;

    @ValidPassword(message = "Password must contain [A-Z][a-z][0-9] and greater than 7")
    private String password;

    private String image;

    private Role role;
}
