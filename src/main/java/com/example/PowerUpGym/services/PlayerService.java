package com.example.PowerUpGym.services;

import com.example.PowerUpGym.entity.classesGym.PlayerClassEnrollment;
import com.example.PowerUpGym.entity.users.PlayersEntity;
import com.example.PowerUpGym.entity.users.UserEntity;
import com.example.PowerUpGym.repositories.PlayerClassEnrollmentRepository;
import com.example.PowerUpGym.repositories.PlayerEntityRepository;
import com.example.PowerUpGym.repositories.UserEntityRepositories;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class PlayerService {

    private final PlayerEntityRepository playerRepository;

    private final UserEntityRepositories userEntityRepositories;

    private final PlayerClassEnrollmentRepository playerClassEnrollmentRepository;


    public PlayerService(PlayerEntityRepository playerRepository, UserEntityRepositories userEntityRepositories, PlayerClassEnrollmentRepository playerClassEnrollmentRepository) {
        this.playerRepository = playerRepository;
        this.userEntityRepositories = userEntityRepositories;
        this.playerClassEnrollmentRepository = playerClassEnrollmentRepository;
    }

    public UserEntity findUserByUsername(String username) {
        return userEntityRepositories.findByUsername(username);
    }
    public List<PlayersEntity> getAllPlayers() {
        return playerRepository.findAll();
    }

    public PlayersEntity signupPlayer(PlayersEntity player) {
        playerRepository.save(player);
        return player;
    }

    public PlayersEntity getPlayerById(Long playerId) {
        return playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found with ID: " + playerId));
    }

    @Transactional
    public void addPlayerClassEnrollment(PlayerClassEnrollment enrollment) {
        playerClassEnrollmentRepository.save(enrollment);
    }

}