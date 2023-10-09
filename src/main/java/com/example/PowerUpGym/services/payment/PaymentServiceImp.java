package com.example.PowerUpGym.services.payment;

import com.example.PowerUpGym.entity.payments.PaymentsEntity;
import com.example.PowerUpGym.entity.users.PlayersEntity;
import com.example.PowerUpGym.entity.users.UserEntity;
import com.example.PowerUpGym.repositories.PaymentsEntityRepository;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImp implements PaymentService{

    private final PaymentsEntityRepository paymentsEntityRepository;

    public PaymentServiceImp(PaymentsEntityRepository paymentsEntityRepository) {
        this.paymentsEntityRepository = paymentsEntityRepository;
    }

    @Override
    public PaymentsEntity savePayment(PaymentsEntity payment) {
        return paymentsEntityRepository.save(payment);
    }

    @Override
    public PaymentsEntity getPaymentByPlayer(PlayersEntity player) {
        UserEntity user = player.getUser();
        return paymentsEntityRepository.findByUserEntity(user);
    }

}
