package com.example.PowerUpGym.bo.auth.update;

import com.example.PowerUpGym.Validation.FullName;
import com.example.PowerUpGym.Validation.ValidPassword;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Validated
public class UserUpdateRequest {
    private Long userId;
    @NotEmpty(message = "Full name must not be empty")
    @FullName(message = "Full name must be between 3-20")
    private String fullName;
    private String username;

    private String email;
    private String phoneNumber;
    @ValidPassword
    private String password;
}
