package com.example.PowerUpGym.bo.auth.users;

import com.example.PowerUpGym.enums.Role;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationRequest {
    private String fullName;
    private String username;
    private String email;
    private String phoneNumber;
    private String password;
    private String image;
    private Role role;
}
