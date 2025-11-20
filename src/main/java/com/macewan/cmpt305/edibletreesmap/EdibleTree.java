package com.macewan.cmpt305.edibletreesmap;

import java.math.BigDecimal;

public class EdibleTree {
    private String id;
    private String neighborhoodName;
    private String speciesBotanical;
    private String speciesCommon;
    private String Genus;
    private String Species;
    private String Cultivar;
    private Integer diameterBreatHeight;
    private Integer conditionPercent;
    private Integer yearPlanted;
    private String typeFruit;
    private BigDecimal latitude;
    private BigDecimal longitude;


    public EdibleTree(String csv) {




    }
    public String getId() {
        return id;
    }
    public String getNeighborhoodName() {
        return neighborhoodName;
    }
    public String getSpeciesBotanical() {
        return speciesBotanical;
    }
    public String getSpeciesCommon() {
        return speciesCommon;
    }
    public String getGenus() {
        return Genus;
    }
    public String getSpecies() {
        return Species;
    }
    public String getCultivar() {
        return Cultivar;
    }
    public Integer getDiameterBreatHeight() {
        return diameterBreatHeight;
    }
    public Integer getConditionPercent() {
        return conditionPercent;
    }
    public Integer getYearPlanted() {
        return yearPlanted;
    }
    public String getTypeFruit() {
        return typeFruit;
    }
    public BigDecimal getLatitude() {
        return latitude;
    }
    public BigDecimal getLongitude() {
        return longitude;
    }


}
