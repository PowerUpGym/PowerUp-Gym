package com.example.PowerUpGym.services.player;

import com.example.PowerUpGym.entity.classesGym.ClassesEntity;
import com.example.PowerUpGym.entity.classesGym.PlayerClassEnrollment;
import com.example.PowerUpGym.entity.users.PlayersEntity;
import com.example.PowerUpGym.entity.users.UserEntity;
import java.util.List;

    public interface PlayerService {
        UserEntity findUserByUsername(String username);
        List<PlayersEntity> getAllPlayers();
        PlayersEntity signupPlayer(PlayersEntity player);
        PlayersEntity getPlayerById(Long playerId);
        void addPlayerClassEnrollment(PlayerClassEnrollment enrollment);
        List<ClassesEntity> getPlayerEnrollment(PlayersEntity player);
        List<PlayersEntity> searchPlayersByUsernameOrPhoneNumber(String searchTerm);
    }