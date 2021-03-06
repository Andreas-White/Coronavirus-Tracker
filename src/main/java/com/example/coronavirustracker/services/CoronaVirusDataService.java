package com.example.coronavirustracker.services;

import com.example.coronavirustracker.model.LocationStats;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class CoronaVirusDataService {

    private static String CVCASES_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
    private static String CVDEATHS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_deaths_global.csv";
    private static String CVRECOVERED_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_recovered_global.csv";

    private List<LocationStats> casesList = new ArrayList<>();
    private List<LocationStats> deathList = new ArrayList<>();
    private List<LocationStats> recoveredList = new ArrayList<>();

    public List<LocationStats> getDeathList() {
        return deathList;
    }

    public List<LocationStats> getRecoveredList() {
        return recoveredList;
    }

    public List<LocationStats> getCasesList() {
        return casesList;
    }

    @PostConstruct  // is used on a method that needs to be executed after dependency injection is done to
    // perform any initialization. This method MUST be invoked before the class is put into service
    @Scheduled(cron = " 0 0 0/6 * * *")
    // Enables running this method every second, so the data are frequently refreshed.
    // Check "cron" for scheduling specifications
    public void fetchCoronaVirusCasesData() {

        this.casesList = extractData(CVCASES_DATA_URL);
        this.deathList = extractData(CVDEATHS_DATA_URL);
        this.recoveredList = extractData(CVRECOVERED_DATA_URL);
    }

    public List<LocationStats> extractData(String url) {

        // Created a new list for concurrency errors
        List<LocationStats> newStatsList = new ArrayList<>();
        try {
            StringReader in = new StringReader(returnResponse(url).body());
            // Parsing the CSV file to Strings, in order to extract the wanted data
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);
            for (CSVRecord record : records) {
                LocationStats locationStats = new LocationStats();
                locationStats.setState(record.get("Province/State"));
                locationStats.setCountry(record.get("Country/Region"));
                locationStats.setLatestTotalCases(Integer.parseInt(record.get(record.size() - 1)));

                int latestCases = Integer.parseInt(record.get(record.size() - 1));
                int previousDayCases = Integer.parseInt(record.get(record.size() - 2));
                locationStats.setDiffFromPreviousDay(latestCases - previousDayCases);

                newStatsList.add(locationStats);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return newStatsList;
    }

    /**
     * Makes an http request to fetch the data from the link
     *
     * @param url the url of the data source
     * @return a String with the data
     */
    public HttpResponse<String> returnResponse(String url) {
        HttpResponse<String> response = null;
        try {

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}
