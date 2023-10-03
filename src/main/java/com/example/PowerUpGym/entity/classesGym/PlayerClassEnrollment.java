package com.example.PowerUpGym.entity.classesGym;

import com.example.PowerUpGym.entity.users.PlayersEntity;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "mst_class_enrollment")
public class PlayerClassEnrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private PlayersEntity player;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private ClassesEntity enrolledClass;

    @Column(name = "enrollment_date")
    private LocalDate enrollmentDateTime;

}
