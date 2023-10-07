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
public class AddPackageRequest {
    private String packageName;
    private int price;
    private int duration;
    private String description;
}
