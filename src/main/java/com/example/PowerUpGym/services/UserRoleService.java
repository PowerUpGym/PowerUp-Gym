package com.example.PowerUpGym.services;

import com.example.PowerUpGym.entity.users.UserRoleEntity;
import com.example.PowerUpGym.enums.Role;
import com.example.PowerUpGym.repositories.UserRoleRepository;
import org.springframework.stereotype.Service;

@Service
public class UserRoleService {

   private final UserRoleRepository userRoleRepository;

    public UserRoleService(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    public UserRoleEntity getUserRoleByName(Role roleName) {
        return userRoleRepository.findByRole(roleName);
    }
    public UserRoleEntity saveUserRole(UserRoleEntity userRole) {
        return userRoleRepository.save(userRole);
    }

    public UserRoleEntity findRoleByRole(Role role) {
        return userRoleRepository.findByRole(role);
    }

}
