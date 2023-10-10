package com.example.PowerUpGym.services.roles;

import com.example.PowerUpGym.entity.users.UserRoleEntity;
import com.example.PowerUpGym.enums.Role;
import com.example.PowerUpGym.repositories.UserRoleRepository;
import org.springframework.stereotype.Service;

@Service
public class UserRoleServiceImp implements UserRoleService{

   private final UserRoleRepository userRoleRepository;

    public UserRoleServiceImp(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public UserRoleEntity findRoleByRole(Role role) {
        return userRoleRepository.findByRole(role);
    }

    public UserRoleEntity saveUserRole(UserRoleEntity userRole) {
        return userRoleRepository.save(userRole);
    }
}
