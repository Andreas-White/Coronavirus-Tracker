package com.example.coronavirustracker.controller;

import com.example.coronavirustracker.model.LocationStats;
import com.example.coronavirustracker.services.CoronaVirusDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    CoronaVirusDataService service;

    @Autowired
    public void setService(CoronaVirusDataService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<LocationStats> statsList = service.getStatsList();
        int totalCases = statsList.stream().mapToInt(LocationStats::getLatestTotalCases).sum();
        model.addAttribute("locationStats", statsList);
        model.addAttribute("totalReportedCases",totalCases);
        return "home";
    }
}
