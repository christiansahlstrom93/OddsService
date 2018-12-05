package com.fuzz.oddsservice.service.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OddsCalcEntity {
    private String name;
    private List<GameLink> allCommon;
}
