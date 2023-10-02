//package com.example.PowerUpGym.entity.classesGym;
//
//import com.example.PowerUpGym.entity.packagesGym.PackagesEntity;
//import com.example.PowerUpGym.entity.users.PlayersEntity;
//import lombok.*;
//
//import javax.persistence.*;
//import java.time.LocalDate;
//
//@Entity
//@Setter
//@Getter
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//@Table(name = "mst_class_enrollment")
//public class PlayerClassEnrollment {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id", nullable = false)
//    private Long id;
//
//    @Column(name = "enrollment_date")
//    private LocalDate enrollmentDateTime;
//    @ManyToOne
//    @JoinColumn(name = "class_id")
//    private ClassesEntity enrolledClass;
//
//    @ManyToOne
//    @JoinColumn(name = "player_id")
//    private PlayersEntity player;
//
//    @JoinColumn(name = "package")
//    @ManyToOne
//    private PackagesEntity packagesEntity;
//
//
//}
