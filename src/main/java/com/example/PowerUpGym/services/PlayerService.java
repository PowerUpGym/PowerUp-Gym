package com.example.PowerUpGym.services;

import com.example.PowerUpGym.entity.classesGym.ClassesEntity;
import com.example.PowerUpGym.entity.classesGym.PlayerClassEnrollment;
import com.example.PowerUpGym.entity.users.PlayersEntity;
import com.example.PowerUpGym.entity.users.UserEntity;
import com.example.PowerUpGym.repositories.PlayerClassEnrollmentRepository;
import com.example.PowerUpGym.repositories.PlayerEntityRepository;
import com.example.PowerUpGym.repositories.UserEntityRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class PlayerService {

    @Autowired
    private PlayerClassEnrollmentRepository playerClassEnrollmentRepository;
    @Autowired
    private ClassService classService;

    private final PlayerEntityRepository playerRepository;

    private final UserEntityRepositories userEntityRepositories;

    public PlayerService(PlayerEntityRepository playerRepository, UserEntityRepositories userEntityRepositories) {
        this.playerRepository = playerRepository;
        this.userEntityRepositories = userEntityRepositories;
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