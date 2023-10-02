package com.example.PowerUpGym.services;

import com.example.PowerUpGym.entity.users.PlayersEntity;
import com.example.PowerUpGym.entity.users.UserEntity;
import com.example.PowerUpGym.repositories.PlayerEntityRepository;
import com.example.PowerUpGym.repositories.UserEntityRepositories;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PlayerService {

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

}