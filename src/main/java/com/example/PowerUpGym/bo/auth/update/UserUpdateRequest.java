package com.example.PowerUpGym.bo.auth.update;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserUpdateRequest {
    private Long userId;
    private String fullName;
    private String username;
    private String email;
    private String phoneNumber;
    private String password;
}
