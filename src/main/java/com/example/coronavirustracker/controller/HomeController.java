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

    @GetMapping("/cases")
    public String getConfirmedCases(Model model) {
        List<LocationStats> statsList = service.getCasesList();
        int totalCases = statsList.stream().mapToInt(LocationStats::getLatestTotalCases).sum();
        model.addAttribute("locationStats", statsList);
        model.addAttribute("totalReportedCases",totalCases);
        return "confirmedCases";
    }

    @GetMapping("/deaths")
    public String getDeaths(Model model) {
        List<LocationStats> statsList = service.getDeathList();
        int totalCases = statsList.stream().mapToInt(LocationStats::getLatestTotalCases).sum();
        model.addAttribute("locationStats", statsList);
        model.addAttribute("totalDeaths",totalCases);
        return "confirmedDeaths";
    }

    @GetMapping("/recovered")
    public String getRecovered(Model model) {
        List<LocationStats> statsList = service.getRecoveredList();
        int totalCases = statsList.stream().mapToInt(LocationStats::getLatestTotalCases).sum();
        model.addAttribute("locationStats", statsList);
        model.addAttribute("totalRecovered",totalCases);
        return "recoveredCases";
    }

    @GetMapping("/")
    public String getHome() {
        return "home";
    }
}
