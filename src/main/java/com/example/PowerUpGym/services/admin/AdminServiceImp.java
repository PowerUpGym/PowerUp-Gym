package com.example.PowerUpGym.services.admin;

import com.example.PowerUpGym.entity.users.AdminEntity;
import com.example.PowerUpGym.repositories.AdminEntityRepository;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImp implements AdminService{

    private final AdminEntityRepository adminEntityRepository;
    public AdminServiceImp(AdminEntityRepository adminEntityRepository) {
        this.adminEntityRepository = adminEntityRepository;
    }
    @Override
    public AdminEntity signupAdmin(AdminEntity admin) {
        adminEntityRepository.save(admin);
        return admin;
    }
    @Override
    public AdminEntity getAdminById(Long id) {
        return adminEntityRepository.findById(id).
                orElseThrow(()-> new RuntimeException("Admin not found for this ID : " + id));
    }
    @Override
    public AdminEntity getAdminByUsername(String username) {
        return adminEntityRepository.findByUserUsername(username);
    }
//    @Override
//    public UserEntity findUserByUsername(String username) {
//        return null;
//    }

}
