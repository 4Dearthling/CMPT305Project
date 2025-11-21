package com.macewan.cmpt305.edibletreesmap;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import com.opencsv.CSVReader;


public class EdibleTree {
    private Integer id;
    private PlantLocation plantLocation;
    private PlantBiology plantBiology;
    private PlantInfo plantInfo;




    public EdibleTree(CSVRecord tree) {
        this.addTree(tree);

    }

    public void addTree(CSVRecord csvLine) {
        this.id = csvLine.get(0).isBlank() ? -1 : Integer.parseInt(csvLine.get(0));

        PlantLocation location = new PlantLocation(csvLine.get(1).isBlank() ? "" : csvLine.get(1),
                csvLine.get(15).isBlank() ? BigDecimal.ZERO : new BigDecimal(csvLine.get(15)),
                csvLine.get(16).isBlank() ? BigDecimal.ZERO : new BigDecimal(csvLine.get(16)));

        PlantBiology biology = new PlantBiology(csvLine.get(3).isBlank() ? "" : csvLine.get(3),
                csvLine.get(4).isBlank() ? "" : csvLine.get(4),
                csvLine.get(5).isBlank() ? "" : csvLine.get("genus"),
                csvLine.get(6).isBlank() ? "" : csvLine.get(6),
                csvLine.get(7).isBlank() ? "" : csvLine.get(7),
                csvLine.get(13).isBlank() ? "" : csvLine.get(13));

        PlantInfo info = new PlantInfo(csvLine.get(8).isBlank() ? -1 : Integer.parseInt(csvLine.get(8)),
                csvLine.get(9).isBlank() ? -1 : Integer.parseInt(csvLine.get(9)),
                csvLine.get(10).isBlank() ? "" : csvLine.get(10));

        this.plantLocation = location;
        this.plantBiology = biology;
        this.plantInfo = info;


    }
    @Override
    public String toString() {
        return "[ID: " + getId() + ", Neighborhood: " + getPlantLocation().getNeighborhoodName() + ", Fruit: " + getPlantBiology().getTypeFruit() + "]";
    }
    public Integer getId() {
        return id;
    }
    public PlantLocation getPlantLocation() {
        return plantLocation;
    }
    public PlantBiology getPlantBiology() {
        return plantBiology;
    }
    public PlantInfo getPlantInfo() {
        return plantInfo;
    }

}
