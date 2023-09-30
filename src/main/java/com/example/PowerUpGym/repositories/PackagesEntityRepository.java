package com.example.PowerUpGym.repositories;

import com.example.PowerUpGym.entity.packagesGym.PackagesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PackagesEntityRepository extends JpaRepository<PackagesEntity,Long> {
}
