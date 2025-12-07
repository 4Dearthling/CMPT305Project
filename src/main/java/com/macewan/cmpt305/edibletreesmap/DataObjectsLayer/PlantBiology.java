package com.macewan.cmpt305.edibletreesmap.DataObjectsLayer;

public class PlantBiology {

    private String speciesBotanical;
    private String speciesCommon;
    private String Genus;
    private String Species;
    private String Cultivar;
    private String typeFruit;
    public PlantBiology(String speciesBotanical, String speciesCommon, String Genus, String Species, String Cultivar, String typeFruit) {
        this.speciesBotanical = speciesBotanical;
        this.speciesCommon = speciesCommon;
        this.Genus = Genus;
        this.Species = Species;
        this.Cultivar = Cultivar;
        this.typeFruit = typeFruit;
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
    public String getTypeFruit() {
        return typeFruit;
    }
}
