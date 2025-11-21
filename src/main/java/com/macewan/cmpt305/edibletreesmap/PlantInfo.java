package com.macewan.cmpt305.edibletreesmap;

public class PlantInfo {
    private Integer diameterBreastHeight;
    private Integer conditionPercent;
    private Integer yearPlanted;
    public PlantInfo(Integer diameterBreastHeight, Integer conditionPercent, String datePlanted) {
        this.diameterBreastHeight = diameterBreastHeight;
        this.conditionPercent = conditionPercent;
        this.setYearPlanted(datePlanted);
    }
    private void setYearPlanted(String datePlanted) {
        this.yearPlanted = Integer.parseInt(datePlanted.substring(0, 4));
    }
    public Integer getDiameterBreastHeight(){
        return diameterBreastHeight;
    }
    public Integer getConditionPercent(){
        return conditionPercent;
    }
    public Integer getYearPlanted(){
        return yearPlanted;
    }
}
