package com.example.PowerUpGym.bo.auth.users;

import com.example.PowerUpGym.enums.Role;
import lombok.*;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class PlayerRegistrationRequest {

    private String address;

    private int age;

    private int height;

    private int weight;

    private Long packageId;

    private String paymentMethod;
}
