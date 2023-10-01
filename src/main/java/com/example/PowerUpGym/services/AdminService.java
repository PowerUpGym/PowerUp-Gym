package com.example.PowerUpGym.services;

import com.example.PowerUpGym.entity.users.AdminEntity;
import com.example.PowerUpGym.entity.users.PlayersEntity;
import com.example.PowerUpGym.repositories.AdminEntityRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service

public class AdminService {
    private final AdminEntityRepository adminEntityRepository;

    public AdminService(AdminEntityRepository adminEntityRepository) {
        this.adminEntityRepository = adminEntityRepository;
    }
    public List<AdminEntity> getAlladmins(){return adminEntityRepository.findAll();}
    public AdminEntity signupAdmin(AdminEntity admin) {
        adminEntityRepository.save(admin);
        return admin;
    }


}