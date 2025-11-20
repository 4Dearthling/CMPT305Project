package com.macewan.cmpt305.edibletreesmap;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class ApiCsvEtDAO implements EdibleTreeDAO{
    private final String endpoint;
    public ApiCsvEtDAO(String endpoint) {this.endpoint = endpoint;}
    @Override
    public EdibleTree getById(int id) {
        try {
            String query = this.endpoint+"?id="+id;
            List<EdibleTree> results = getResults(query);
            if (results.isEmpty()) { return results.getFirst(); }
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
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString()); // response converted to string
            //System.out.println(response.status());
            System.out.println(response.body());
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
