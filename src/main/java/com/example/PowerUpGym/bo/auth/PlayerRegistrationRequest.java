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
public class PlayerRegistrationRequest {

    private String address;
    private int age;
    private int height;
    private int weight;
    private Long packageId;

}