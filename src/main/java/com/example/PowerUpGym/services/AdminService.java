package com.example.PowerUpGym.services;

import com.example.PowerUpGym.entity.users.AdminEntity;
import com.example.PowerUpGym.entity.users.UserEntity;
import com.example.PowerUpGym.repositories.AdminEntityRepository;
import com.example.PowerUpGym.repositories.UserEntityRepositories;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AdminService {
    private final AdminEntityRepository adminEntityRepository;
    private final UserEntityRepositories userEntityRepositories;

    public AdminService(AdminEntityRepository adminEntityRepository, UserEntityRepositories userEntityRepositories) {
        this.adminEntityRepository = adminEntityRepository;
        this.userEntityRepositories = userEntityRepositories;
    }

    public List<AdminEntity> getAllAdmins(){
        return adminEntityRepository.findAll();
    }

    public AdminEntity signupAdmin(AdminEntity admin) {
        adminEntityRepository.save(admin);
        return admin;
    }

    public AdminEntity getAdminById(Long id) {
        return adminEntityRepository.findById(id).orElse(null);
    }

    public AdminEntity getAdminByUsername(String username) {
        return adminEntityRepository.findByUserUsername(username);
    }

    public UserEntity findAdminByUsername(String username) {
        return userEntityRepositories.findByUsername(username);
    }
}
