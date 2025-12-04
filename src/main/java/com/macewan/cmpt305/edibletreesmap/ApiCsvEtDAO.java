package com.macewan.cmpt305.edibletreesmap;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


public class ApiCsvEtDAO implements EdibleTreeDAO{
    private final String endpoint;
    private final String APP_TOKEN = "25zGMa63Gh9IFkA8xUv47Zzyy";
    public ApiCsvEtDAO(String endpoint) {this.endpoint = endpoint;}
    private String makeQuery(String soqlQuery) {

        String encodedSoqlQuery = URLEncoder.encode(soqlQuery, StandardCharsets.UTF_8);

        String query = endpoint + "?query=" + encodedSoqlQuery;

        System.out.println("Query: " + query);

        return query;
    }
    @Override
    public EdibleTree getById(int id) {

            //String query = this.endpoint+"?id="+id;
//        String soqlQuery = "SELECT * WHERE (`bears_edible_fruit` = true) AND (`ID` = " + id + ")";
//        String query = makeQuery(soqlQuery);
//
//        try {
//            List<EdibleTree> results = this.getResults(query);
//            if (!results.isEmpty()) { return results.getFirst(); }
//            return null;
//        }
//        catch (IOException | InterruptedException e) {
//            return null;
//        }
        return null;

    }
    @Override
    public List<EdibleTree> getResults(String query) throws IOException, InterruptedException {

        List<EdibleTree> results = new ArrayList<>();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(query))
                .header("X-App-Token", APP_TOKEN)
                .GET()
                .build();
        try {
            HttpResponse<java.io.InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
            try (Reader reader = new BufferedReader(new InputStreamReader(response.body(), StandardCharsets.UTF_8));
                CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT
                        .builder().setHeader().setSkipHeaderRecord(true).build())) {
                for (CSVRecord record : parser) {
                    EdibleTree newTree = new EdibleTree(record);
                    results.add(newTree);
//                    System.out.println(record.toString());
                }
//                System.out.println("Number of entries: " + results.size());
                return results;


            }
            catch (IOException e) {
                System.out.println("Error parsing response: " + e.getMessage());
            }

        }
        catch (IOException | InterruptedException e) { //exceptions thrown by send
            System.out.println("Error no response: " + e.getMessage());
        }
        return results;

    }
    @Override
    public List<EdibleTree> getAll() throws IOException, InterruptedException {
        String soqlQuery = "SELECT * WHERE (`bears_edible_fruit` = true)";
        String query = makeQuery(soqlQuery);
        return this.getResults(query);

    }

    @Override
    public List<EdibleTree> getByName(String name) {
        return List.of();
    }

    @Override
    public List<EdibleTree> getByLocation(double latitude, double longitude) {
        return List.of();
    }

    @Override
    public List<EdibleTree> getByNeighbourhood(String neighbourhood) {
        return List.of();
    }

    @Override
    public List<EdibleTree> getByGenus(String genus) {
        return List.of();
    }
}
