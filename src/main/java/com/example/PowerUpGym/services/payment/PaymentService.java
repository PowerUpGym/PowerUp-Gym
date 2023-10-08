package com.example.PowerUpGym.services.payment;

import com.example.PowerUpGym.entity.payments.PaymentsEntity;
import com.example.PowerUpGym.entity.users.PlayersEntity;

public interface PaymentService {
    PaymentsEntity savePayment(PaymentsEntity payment);
    PaymentsEntity getPaymentByPlayer(PlayersEntity player);
}
