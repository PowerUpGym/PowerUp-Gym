package com.example.PowerUpGym.services;

import com.example.PowerUpGym.entity.users.PlayersEntity;
import com.example.PowerUpGym.repositories.PlayerEntityRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PlayerService {

    private final PlayerEntityRepository playerRepository;

    public PlayerService(PlayerEntityRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public List<PlayersEntity> getAllPlayers() {
        return playerRepository.findAll();
    }

    public PlayersEntity signupPlayer(PlayersEntity player) {
        playerRepository.save(player);
        return player;
    }
}