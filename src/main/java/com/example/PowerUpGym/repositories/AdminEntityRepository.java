package com.example.PowerUpGym.repositories;

import com.example.PowerUpGym.entity.users.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminEntityRepository extends JpaRepository<AdminEntity,Long> {
}
