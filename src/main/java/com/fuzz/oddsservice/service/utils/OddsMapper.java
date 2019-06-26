package com.fuzz.oddsservice.service.utils;

import com.fuzz.oddsservice.service.models.GameLink;
import com.fuzz.oddsservice.service.models.OddsCalcEntity;
import com.fuzz.oddsservice.service.models.PlayerForm;
import com.fuzz.oddsservice.service.models.PlayerOdds;
import com.fuzz.oddsservice.service.models.PlayerStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class OddsMapper {
    @Autowired
    private EntityHelper entityHelper;

    @Autowired
    private OddsCalculator oddsCalculator;

    public Map<String, Float> getOdds(final String statsVo,
                                      final String p1,
                                      final String p2,
                                      final String p3,
                                      final String p4) throws IOException {
        final List<PlayerStats> stats = filterStats(entityHelper.mapPlayerStats(statsVo), p1, p2, p3, p4);
        if (stats.size() == 2) {
            return getOddsOneOnOne(statsVo, stats.get(0).getName(), stats.get(1).getName());
        }
        stats.sort(Comparator.comparing(PlayerStats::getAverageDiff).reversed());
        return convertToMap(getOdds(getCommonGames(stats)));
    }

    public Map<String, Float> getOddsOneOnOne(final String statsVo,
                                      final String p1,
                                      final String p2) throws IOException {
        final List<PlayerStats> stats = filterStats(entityHelper.mapPlayerStats(statsVo), p1, p2, "", "");
        stats.sort(Comparator.comparing(PlayerStats::getAverageDiff).reversed());
        return convertToMap(getOddsByStats(stats));
    }

    private Map<String, Float> convertToMap(final List<PlayerOdds> odds){
       return odds.stream()
               .collect(Collectors.toMap(PlayerOdds::getName, PlayerOdds::getOdds));
    }

    private List<PlayerOdds> getOddsByStats(final List<PlayerStats> stats) {
        final List<PlayerOdds> playerOdds = new ArrayList<>();
        stats.forEach(stat -> {
            final PlayerOdds odds = new PlayerOdds();
            odds.setName(stat.getName());
            odds.setTotalPoints(stat.getAverageDiff());
            playerOdds.add(odds);
        });
        return oddsCalculator.calculateOneOnOne(playerOdds);
    }

    private List<PlayerOdds> getOdds(final List<OddsCalcEntity> oddsCalcEntities) {
        final List<PlayerOdds> playerOdds = new ArrayList<>();
        oddsCalcEntities.forEach(oddsCalcEntity -> {
            final PlayerOdds po = new PlayerOdds();
            int totalOddsPoint = 0;
            po.setName(oddsCalcEntity.getName());
            for(GameLink gameLink : oddsCalcEntity.getAllCommon()) {
                if (didWin(gameLink)) {
                    totalOddsPoint += gameLink.getPlayerForms().size() * gameLink.getPoints();
                }
            }
            po.setTotalPoints(totalOddsPoint);
            playerOdds.add(po);
        });
        oddsCalculator.calculate(playerOdds);
        return playerOdds;
    }

    private boolean didWin(final GameLink gameLink) {
        return gameLink.getPlayerForms().stream().filter(form -> form.getPoints() > gameLink.getPoints()).collect(Collectors.toList()).size() == 0;
    }

    private List<OddsCalcEntity>    getCommonGames(final List<PlayerStats> stats) {
        final List<OddsCalcEntity> oddsCalcEntities = new ArrayList<>();
        stats.forEach(stat -> {
            final OddsCalcEntity oddsCalcEntity = new OddsCalcEntity();
            oddsCalcEntity.setAllCommon(new ArrayList<>(new ArrayList<>()));
            oddsCalcEntity.setName(stat.getName());
            final List<PlayerStats> excluded = getExcludedStats(stats, stat.getName());
            stat.getForms().forEach(form -> {
                final List<PlayerForm> commonForm = getCommonGames(excluded, form.getGameKey());
                if (commonForm.size() > 0) {
                    final GameLink gameLink = new GameLink();
                    gameLink.setPlayerForms(commonForm);
                    gameLink.setPoints(form.getPoints());
                    oddsCalcEntity.getAllCommon().add(gameLink);
                }
            });
            oddsCalcEntities.add(oddsCalcEntity);
        });
        return oddsCalcEntities;
    }

    private List<PlayerForm> getCommonGames(final List<PlayerStats> stats, final String gameKey) {
        List<PlayerForm> forms = new ArrayList<>();
        stats.forEach(s -> {
            final List<PlayerForm> f1 = s.getForms().stream().filter(f -> f.getGameKey().equals(gameKey)).collect(Collectors.toList());
            if (f1.size() > 0) {
                forms.add(f1.get(0));
            }
        });
        return forms;
    }

    private List<PlayerStats> getExcludedStats(final List<PlayerStats> stats, final String name) {
        return stats.stream().filter(stat -> !stat.getName().equals(name)).collect(Collectors.toList());
    }

    private List<PlayerStats> filterStats(final List<PlayerStats> stats,
                                          final String p1,
                                          final String p2,
                                          final String p3,
                                          final String p4) {
        return stats.stream().filter(stat -> p1.equals(stat.getName()) ||
                                             p2.equals(stat.getName()) ||
                                             p3.equals(stat.getName()) ||
                                             p4.equals(stat.getName())).collect(Collectors.toList());
    }
}
