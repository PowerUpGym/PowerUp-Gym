package com.example.PowerUpGym.util.validators;

public interface Validator <P, R>{
    R validate(P input);
}