package com.example.PowerUpGym;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class PowerUpGymApplication {

	public static void main(String[] args) {
		SpringApplication.run(PowerUpGymApplication.class, args);
	}

}
