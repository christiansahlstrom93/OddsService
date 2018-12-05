package com.fuzz.oddsservice.service.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PlayerStats {
    private String name;
    private int averageDiff;
    private List<PlayerForm> forms;
}
