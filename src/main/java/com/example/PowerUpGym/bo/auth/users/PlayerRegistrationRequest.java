package com.example.PowerUpGym.bo.auth.users;

import com.example.PowerUpGym.enums.Role;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerRegistrationRequest {
    private String fullName;
    private String username;
    private String email;
    private String phoneNumber;
    private String password;
    private Role role;
    private String address;
    private int age;
    private int height;
    private int weight;
    private Long packageId;
    private String paymentMethod;
}
