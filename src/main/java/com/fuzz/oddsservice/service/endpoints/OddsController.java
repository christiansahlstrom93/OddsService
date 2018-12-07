package com.fuzz.oddsservice.service.endpoints;

import com.fuzz.oddsservice.service.http.HttpClient;
import com.fuzz.oddsservice.service.utils.OddsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Map;


@Controller
public class OddsController {
    @Autowired
    private OddsMapper oddsMapper;

    @GetMapping("/")
    @ResponseBody
    public String index() {
        return "Success";
    }

    @GetMapping("/odds/{p1}/{p2}/{p3}/{p4}")
    @ResponseBody
    public Map<String, Float> getOdds(@PathVariable("p1") String p1,
                                      @PathVariable("p2") String p2,
                                      @PathVariable("p3") String p3,
                                      @PathVariable("p4") String p4,
                                      @RequestParam(value = "limit", defaultValue = "50") String limit) throws IOException {
        return oddsMapper.getOdds(new HttpClient().getStats(Integer.parseInt(limit)), p1, p2, p3, p4);
    }
}