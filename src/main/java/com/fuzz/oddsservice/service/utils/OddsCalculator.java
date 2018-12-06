package com.fuzz.oddsservice.service.utils;

import com.fuzz.oddsservice.service.models.PlayerOdds;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class OddsCalculator {

    private static final float MAX_ODDS = 10;

     void calculate(final List<PlayerOdds> odds) {
         multipleWithRoundDiff(odds);
         for (PlayerOdds odd : odds) {
             final float diff = diff(odd.getTotalPoints(), odds);
             odd.setOdds(getOdds(diff, odds));
         }
         odds.sort(Comparator.comparing(PlayerOdds::getOdds));
     }

     private float diff(final float points, final List<PlayerOdds> odds) {
         return points - getMedian(odds);
     }

     private float getMedian(final List<PlayerOdds> odds) {
         final float tot = (float) odds.stream().mapToDouble(PlayerOdds::getTotalPoints).sum();
         return tot / odds.size();
     }


    private float getPercentage(final float diff, final float median) {
         return (diff * 100.0f) / median;
    }

    private void multipleWithRoundDiff(final List<PlayerOdds> odds) {
         float multiple = 1.8F;
        for (PlayerOdds odd : odds) {
            odd.setTotalPoints(odd.getTotalPoints() * multiple);
            multiple -= 0.3;
            if (multiple < 1) {
                multiple = 1;
            }
        }
    }

    private float getOdds(final float diff, final List<PlayerOdds> odds) {
        final float percentage = getPercentage(diff, getMedian(odds));
        if (percentage > 100) {
            return 1;
        } else if (percentage > 80) {
            return 1.2F;
        } else if (percentage > 60) {
            return 1.5F;
        } else if (percentage > 40) {
            return 1.8F;
        } else if (percentage > 20) {
            return 2.8F;
        } else if (percentage > 0) {
            return 3.3F;
        } else if (percentage > -20) {
            return 4F;
        } else if (percentage > -40) {
            return 5.5F;
        } else if (percentage > -60) {
            return 7.5F;
        } else if (percentage > -80) {
            return 8F;
        }
        return MAX_ODDS;
    }
}
