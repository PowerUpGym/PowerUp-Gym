package com.example.PowerUpGym.services;

import com.example.PowerUpGym.entity.classesGym.PlayerClassEnrollment;
import com.example.PowerUpGym.entity.users.PlayersEntity;
import com.example.PowerUpGym.repositories.PlayerClassEnrollmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassEnrollmentService {
    private final PlayerClassEnrollmentRepository playerClassEnrollmentRepository;

    public ClassEnrollmentService(PlayerClassEnrollmentRepository playerClassEnrollmentRepository) {
        this.playerClassEnrollmentRepository = playerClassEnrollmentRepository;
    }

    public List<PlayerClassEnrollment> findByPlayer(PlayersEntity player) {
        return playerClassEnrollmentRepository.findByPlayer(player);
    }
}
