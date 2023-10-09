package com.example.PowerUpGym.services;

import com.example.PowerUpGym.services.player.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class PlayerSubscriptionService {

    @Autowired
    private PlayerService playerService;

/*

0 in the first position: Specifies the minute (0-59).
0 in the second position: Specifies the hour (0-23).
* in the third position: Specifies the day of the month (1-31).
* in the fourth position: Specifies the month (1-12).
* in the fifth position: Specifies the day of the week (0-7, where both 0 and 7 represent Sunday).th position: This is a special character used in cron expressions for "no specific value." In some cron expressions used in Quartz Scheduler, you would use ? to represent the "no specific value" for the day of the week. However, in standard cron expressions, you'd use * for the day of the week.

* */

    @Scheduled(cron = "0 0 * * *") // Daily check at midnight
    public void checkPlayerSubscriptions() {
        LocalDate currentDate = LocalDate.now();

        playerService.getAllPlayers().stream()
                .filter(player -> player.getEnd_date().isBefore(currentDate))
                .forEach(player -> {
                    player.setAccountEnabled(false);
                    playerService.signupPlayer(player);
                });
    }

}
