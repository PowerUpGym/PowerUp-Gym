package com.example.PowerUpGym.bo.auth.update;

import com.example.PowerUpGym.entity.classesGym.PlayerClassEnrollment;
import com.example.PowerUpGym.entity.users.AdminEntity;
import com.example.PowerUpGym.entity.users.TrainerEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClassUpdateRequest {
    private Long id;
    private String scheduleDescription;
    private String description;
    private String className;
    private AdminEntity admin;
    private TrainerEntity trainer;
}
