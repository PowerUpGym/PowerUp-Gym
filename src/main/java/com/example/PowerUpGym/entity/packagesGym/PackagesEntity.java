package com.example.PowerUpGym.entity.packagesGym;

import com.example.PowerUpGym.entity.users.AdminEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "mst_packages")
public class PackagesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "package_name", nullable = false)
    private String packageName;

    @Column(name = "price", nullable = false)
    private int price;

    @Column(name = "duration", nullable = false) // Duration in months
    private int duration;

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private AdminEntity admin;

}
