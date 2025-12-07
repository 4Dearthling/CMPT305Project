package com.macewan.cmpt305.edibletreesmap.DataObjectsLayer;

import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;

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
    public Point getPoint() {
        return new Point(
                this.getLongitude().doubleValue(),
                this.getLatitude().doubleValue(),
                SpatialReferences.getWgs84());
    }

}
