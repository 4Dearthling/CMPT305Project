package com.macewan.cmpt305.edibletreesmap;

import java.math.BigDecimal;

public class PlantLocation {
    private String neighborhoodName;
    private BigDecimal latitude;
    private BigDecimal longitude;

    public PlantLocation(String neighborhoodName, BigDecimal latitude, BigDecimal longitude) {
        this.neighborhoodName = neighborhoodName;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public String getNeighborhoodName() {
        return neighborhoodName;
    }
    public BigDecimal getLatitude() {
        return latitude;
    }
    public BigDecimal getLongitude() {
        return longitude;
    }

}
