package com.fuzz.oddsservice.service.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerOdds {
    private String name;
    private float odds;
    @JsonIgnore
    private int totalPoints;
}
