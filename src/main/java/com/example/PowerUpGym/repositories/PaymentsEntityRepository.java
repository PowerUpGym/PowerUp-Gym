package com.example.PowerUpGym.repositories;

import com.example.PowerUpGym.entity.payments.PaymentsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentsEntityRepository extends JpaRepository<PaymentsEntity,Long> {
}
