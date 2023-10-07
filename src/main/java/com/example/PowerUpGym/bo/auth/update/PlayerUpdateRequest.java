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
public class PlayerUpdateRequest {
    private Long userId;
    private Long playerId;
    private String address;
    private Integer age;
    private Integer height;
    private Integer weight;
}
