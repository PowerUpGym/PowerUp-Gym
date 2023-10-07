package com.example.PowerUpGym.bo.auth.users;

import com.example.PowerUpGym.enums.Role;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerRegistrationRequest {
    private String fullName;
    private String username;
    private String email;
    private String phoneNumber;
    private String password;
    private Role role;
    private int age;
    private String experience;
}
