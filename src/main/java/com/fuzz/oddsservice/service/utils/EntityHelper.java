package com.fuzz.oddsservice.service.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fuzz.oddsservice.service.models.PlayerForm;
import com.fuzz.oddsservice.service.models.PlayerStats;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class EntityHelper {
     List<PlayerStats> mapPlayerStats(final String statsVo) throws IOException {
        final List<PlayerStats> stats = new ArrayList<>();
        final ObjectMapper mapper = new ObjectMapper();
        final JsonNode node = mapper.readTree(statsVo);
        node.forEach(n -> {
            final PlayerStats playerStats = new PlayerStats();
            final List<PlayerForm> playerForms = new ArrayList<>();
            final String name = n.get("name").asText();
            n.get("forms").forEach(form -> {
                final PlayerForm playerForm = new PlayerForm();
                playerForm.setGameKey(form.get("gameKey").asText());
                playerForm.setOutcomes(form.findValuesAsText("outcomes"));
                playerForm.setPoints(form.get("points").asInt());
                playerForm.setPlayerName(name);
                playerForms.add(playerForm);
            });
            mapDiff(playerStats, n.get("player").get("stats").get("averageRoundDiff").asText());
            playerStats.setName(name);
            playerStats.setForms(playerForms);
            stats.add(playerStats);
        });
        stats.sort(Comparator.comparing(PlayerStats::getAverageDiff));
        return stats;
    }

    private void mapDiff(final PlayerStats playerStats, final String diff) {
         playerStats.setAverageDiff(Integer.parseInt(diff.replace("%", "")));
    }
}
