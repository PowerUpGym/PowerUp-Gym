package com.example.PowerUpGym.bo.auth.update;

import com.example.PowerUpGym.entity.users.AdminEntity;
import com.example.PowerUpGym.entity.users.TrainerEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TrainerUpdateRequest {
    private Long userId;
    private String fullName;
    private String username;
    private String email;
    private String phoneNumber;
    private String password;
    private int age;
    private String experience;
    private Long adminId;
    private String image;
}