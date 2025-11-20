package com.macewan.cmpt305.edibletreesmap;

import java.io.IOException;
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
    @Override
    public EdibleTree getById(int id) {
        try {
            //String query = this.endpoint+"?id="+id;
            String soqlQuery = "SELECT * WHERE (`bears_edible_fruit` = true) AND (`ID` = " + id + ")";
            String encodedSoqlQuery = URLEncoder.encode(soqlQuery, StandardCharsets.UTF_8);

            String query = endpoint + "?query=" + encodedSoqlQuery;

            System.out.println("Query: " + query);

            List<EdibleTree> results = getResults(query);
            if (!results.isEmpty()) { return results.getFirst(); }
            return null;
        }
        catch (IOException | InterruptedException e) {
            return null;
        }

    }

    private List<EdibleTree> getResults(String query) throws IOException, InterruptedException {
        List<EdibleTree> results = new ArrayList<>();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(query))
                .header("X-App-Token", APP_TOKEN)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString()); // response converted to string
            System.out.println(response.statusCode());
            System.out.println(response.body());
            return results;
        }
        catch (IOException | InterruptedException e) { //exceptions thrown by send
            e.printStackTrace();
        }
        return results;

    }
    @Override
    public List<EdibleTree> getAll() {
        return List.of();
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
