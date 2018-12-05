package com.fuzz.oddsservice.service.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class GameLink {
    private List<PlayerForm> playerForms;
    private int points;
}
