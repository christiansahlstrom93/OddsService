package com.fuzz.oddsservice.service.endpoints;

import com.fuzz.oddsservice.service.http.HttpClient;
import com.fuzz.oddsservice.service.models.PlayerOdds;
import com.fuzz.oddsservice.service.utils.OddsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;


@Controller
public class OddsController {
    @Autowired
    private OddsMapper oddsMapper;

    @GetMapping("/odds/{p1}/{p2}/{p3}/{p4}")
    @ResponseBody
    public List<PlayerOdds> getOdds(@PathVariable("p1") String p1,
                                    @PathVariable("p2") String p2,
                                    @PathVariable("p3") String p3,
                                    @PathVariable("p4") String p4) throws IOException {
        return oddsMapper.getOdds(new HttpClient().getStats(), p1, p2, p3, p4);
    }
}