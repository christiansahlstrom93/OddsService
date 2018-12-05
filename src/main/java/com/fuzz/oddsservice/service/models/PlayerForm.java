package com.fuzz.oddsservice.service.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PlayerForm {
    private String playerName;
    private String gameKey;
    private int points;
    private List<String> outcomes;
}
