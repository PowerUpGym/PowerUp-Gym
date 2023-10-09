package com.example.PowerUpGym.bo.auth;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationRequest {
    private String message;
    private Long senderId;
    private Long receiverId;
}
