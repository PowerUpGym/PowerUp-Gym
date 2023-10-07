package com.example.PowerUpGym.repositories;

import com.example.PowerUpGym.entity.payments.PaymentsEntity;
import com.example.PowerUpGym.entity.users.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentsEntityRepository extends JpaRepository<PaymentsEntity,Long> {
    PaymentsEntity findByUserEntity(UserEntity user);
}
