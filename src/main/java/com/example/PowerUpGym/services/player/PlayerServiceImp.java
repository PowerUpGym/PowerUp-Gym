package com.example.PowerUpGym.services.player;

import com.example.PowerUpGym.entity.classesGym.ClassesEntity;
import com.example.PowerUpGym.entity.classesGym.PlayerClassEnrollment;
import com.example.PowerUpGym.entity.users.PlayersEntity;
import com.example.PowerUpGym.entity.users.UserEntity;
import com.example.PowerUpGym.repositories.PlayerClassEnrollmentRepository;
import com.example.PowerUpGym.repositories.PlayerEntityRepository;
import com.example.PowerUpGym.repositories.UserEntityRepositories;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlayerServiceImp implements PlayerService {

    private final PlayerEntityRepository playerRepository;
    private final UserEntityRepositories userEntityRepositories;
    private final PlayerClassEnrollmentRepository playerClassEnrollmentRepository;

    public PlayerServiceImp(PlayerEntityRepository playerRepository, UserEntityRepositories userEntityRepositories, PlayerClassEnrollmentRepository playerClassEnrollmentRepository) {
        this.playerRepository = playerRepository;
        this.userEntityRepositories = userEntityRepositories;
        this.playerClassEnrollmentRepository = playerClassEnrollmentRepository;
    }

    @Override
    public UserEntity findUserByUsername(String username) {
        return userEntityRepositories.findByUsername(username);
    }

    @Override
    public List<PlayersEntity> getAllPlayers() {
        return playerRepository.findAll();
    }

    @Override
    public PlayersEntity signupPlayer(PlayersEntity player) {
        playerRepository.save(player);
        return player;
    }

    @Override
    public PlayersEntity getPlayerById(Long playerId) {
        return playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found with ID: " + playerId));
    }

    @Override
    public void addPlayerClassEnrollment(PlayerClassEnrollment enrollment) {
        playerClassEnrollmentRepository.save(enrollment);
    }

    @Override
    public List<ClassesEntity> getPlayerEnrollment(PlayersEntity player) {
        List<PlayerClassEnrollment> enrollments = playerClassEnrollmentRepository.findByPlayer(player);
        return enrollments.stream()
                .map(PlayerClassEnrollment::getEnrolledClass)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlayersEntity> searchPlayersByUsernameOrPhoneNumber(String searchTerm) {
        return playerRepository.findByUserUsernameContainingIgnoreCaseOrUserPhoneNumberContainingIgnoreCase(searchTerm, searchTerm);
    }

}