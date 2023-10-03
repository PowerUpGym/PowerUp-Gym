package com.example.PowerUpGym.entity.users;


import com.example.PowerUpGym.entity.classesGym.ClassesEntity;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "mst_trainer")
public class TrainerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "age", nullable = false)
    private int age;

    @Column(name = "experience", nullable = false)
    private String experience;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL)
    private List<ClassesEntity> ownedClasses;

    @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL)
    private List<PlayersEntity> enrolledPlayers;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private AdminEntity admin;

    @Builder
    public TrainerEntity(int age, String experience, UserEntity user, List<ClassesEntity> ownedClasses, List<PlayersEntity> enrolledPlayers, AdminEntity admin) {
        this.age = age;
        this.experience = experience;
        this.user = user;
        this.ownedClasses = ownedClasses;
        this.enrolledPlayers = enrolledPlayers;
        this.admin = admin;
    }
}
