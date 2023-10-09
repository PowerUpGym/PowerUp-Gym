package com.example.PowerUpGym.services.admin;

import com.example.PowerUpGym.entity.users.AdminEntity;
import com.example.PowerUpGym.entity.users.UserEntity;

public interface AdminService {

    AdminEntity signupAdmin(AdminEntity admin);

    AdminEntity getAdminById(Long id);

    AdminEntity getAdminByUsername(String username);

//    UserEntity findUserByUsername(String username);
}
