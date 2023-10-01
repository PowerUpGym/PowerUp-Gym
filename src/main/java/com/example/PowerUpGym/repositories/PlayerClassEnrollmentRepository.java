package com.example.PowerUpGym.repositories;

import com.example.PowerUpGym.entity.classesGym.PlayerClassEnrollment;
import com.example.PowerUpGym.entity.users.PlayersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerClassEnrollmentRepository extends JpaRepository<PlayerClassEnrollment,Long> {

    List<PlayerClassEnrollment> findByPlayer(PlayersEntity player);

}
