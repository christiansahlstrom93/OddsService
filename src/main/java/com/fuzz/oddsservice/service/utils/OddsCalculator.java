package com.fuzz.oddsservice.service.utils;

import com.fuzz.oddsservice.service.models.PlayerOdds;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class OddsCalculator {
     void calculate(final List<PlayerOdds> odds) {
         int currentOdds = odds.size();
         for (PlayerOdds odd : odds) {
             odd.setOdds(mergeOdds(odd.getTotalPoints(), currentOdds));
             currentOdds--;
         }
         odds.sort(Comparator.comparing(PlayerOdds::getOdds));
     }

     private float mergeOdds(final int points, final int currentOdds) {
         float odds = currentOdds;
         if (currentOdds <= 1) {
             return odds;
         }
         for (int i = 0; i < points; i+=2) {
             if (odds > 1) {
                 odds -= 0.5;
             }
         }
         return odds;
     }
}
