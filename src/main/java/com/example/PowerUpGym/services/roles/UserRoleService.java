package com.example.PowerUpGym.services.roles;

import com.example.PowerUpGym.entity.users.UserRoleEntity;
import com.example.PowerUpGym.enums.Role;

public interface UserRoleService {
    UserRoleEntity findRoleByRole(Role role);
}
