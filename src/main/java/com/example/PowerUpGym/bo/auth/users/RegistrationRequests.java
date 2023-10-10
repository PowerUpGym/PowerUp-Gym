package com.example.PowerUpGym.bo.auth.users;

import com.example.PowerUpGym.enums.Role;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class RegistrationRequests {
    private String address;

    private String fullName;

    private String username;

    private String email;

    private String phoneNumber;

    private String password;

    private String image;

    private Role role;

    private int age;

    private int height;

    private int weight;

    private Long packageId;

    private String paymentMethod;
}
