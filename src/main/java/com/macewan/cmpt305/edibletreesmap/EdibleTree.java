package com.macewan.cmpt305.edibletreesmap;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class EdibleTree {
    private String id;



    public EdibleTree(String csvLine) {
        this.addTree(csvLine);

    }

    public void addTree(String csvLine) {
        String[] tokens = csvLine.split(",");
        List<String> cleanedTokens = new ArrayList<>();
        for (String token : tokens) {
            cleanedTokens.add(token.replace("\"", ""));
        }

        PlantLocation location = new PlantLocation(cleanedTokens.get(1).isBlank() ? "" : cleanedTokens.get(1),
                cleanedTokens.get(15).isBlank() ? BigDecimal.ZERO : new BigDecimal(cleanedTokens.get(15)),
                cleanedTokens.get(16).isBlank() ? BigDecimal.ZERO : new BigDecimal(cleanedTokens.get(16)));

        PlantBiology biology = new PlantBiology(cleanedTokens.get(3).isBlank() ? "" : cleanedTokens.get(3),
                cleanedTokens.get(4).isBlank() ? "" : cleanedTokens.get(4),
                cleanedTokens.get(5).isBlank() ? "" : cleanedTokens.get(5),
                cleanedTokens.get(6).isBlank() ? "" : cleanedTokens.get(6),
                cleanedTokens.get(7).isBlank() ? "" : cleanedTokens.get(7),
                cleanedTokens.get(13).isBlank() ? "" : cleanedTokens.get(13));

        PlantInfo info = new PlantInfo(cleanedTokens.get(8).isBlank() ? -1 : Integer.parseInt(cleanedTokens.get(8)),
                cleanedTokens.get(9).isBlank() ? -1 : Integer.parseInt(cleanedTokens.get(9)),
                cleanedTokens.get(10).isBlank() ? "" : cleanedTokens.get(10));


    }
    public String getId() {
        return id;
    }
}
