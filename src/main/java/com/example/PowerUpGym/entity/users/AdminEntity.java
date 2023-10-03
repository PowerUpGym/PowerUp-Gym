package com.example.PowerUpGym.entity.users;


import com.example.PowerUpGym.entity.classesGym.ClassesEntity;
import com.example.PowerUpGym.entity.packagesGym.PackagesEntity;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "mst_admin")
public class AdminEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;


    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL)
    private List<PlayersEntity> signedPlayers;

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL)
    private List<TrainerEntity> signedTrainers;

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL)
    private List<ClassesEntity> createdClasses;

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL)
    private List<PackagesEntity> createdPackages;

}


