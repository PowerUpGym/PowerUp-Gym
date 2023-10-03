package com.example.PowerUpGym.repositories;

import com.example.PowerUpGym.entity.users.UserRoleEntity;
import com.example.PowerUpGym.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRoleEntity,Long> {
    UserRoleEntity findByRole(Role roleName);
}