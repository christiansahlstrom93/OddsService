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
            multiple -= 0.2;
            if (multiple < 1) {
                multiple = 1;
            }
        }
    }

    private float getOdds(final float diff, final List<PlayerOdds> odds) {
        return getLadder(getPercentage(diff, getMedian(odds)));
    }

    List<PlayerOdds> calculateOneOnOne(List<PlayerOdds> playerOdds) {
        final float playerOnePoints = playerOdds.get(0).getTotalPoints();
        final float playerTwoPoints = playerOdds.get(1).getTotalPoints();
        playerOdds.get(0).setOdds(getLadder(playerOnePoints - playerTwoPoints));
        playerOdds.get(1).setOdds(getLadder(playerTwoPoints - playerOnePoints));
        return playerOdds;
    }

    private float getLadder(final float diff) {
        if (diff > 100) {
            return 1.2F;
        } else if (diff > 80) {
            return 1.5F;
        } else if (diff > 40) {
            return 1.8F;
        } else if (diff > 0) {
            return 2F;
        } else if (diff > -30) {
            return 2.8F;
        } else if (diff > -60) {
            return 3.3F;
        } else if (diff > -90) {
            return 4F;
        } else if (diff > -110) {
            return 5.5F;
        } else if (diff > -140) {
            return 7.5F;
        } else if (diff > -180) {
            return 8F;
        }
        return MAX_ODDS;
    }
}
