package com.macewan.cmpt305.edibletreesmap.UILayer;

import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;

import java.util.*;

public class TreeGraphicsManager {
    /*
    * All graphics to do with displaying the trees is dealt with here
    * */
    private Map<String, List<Graphic>> fruitGraphics = new HashMap<>();
    private GraphicsOverlay graphicsOverlay;

    public TreeGraphicsManager(GraphicsOverlay fruitGraphicOverlay) {
        this.graphicsOverlay = fruitGraphicOverlay;
    }

    public void addTreeGraphics(String fruit, Graphic fruitGraphic) {
        fruitGraphic.setVisible(true);
        //remember which graphics belong to which fruit
        fruitGraphics
                .computeIfAbsent(fruit.toLowerCase(), k -> new ArrayList<>())
                .add(fruitGraphic);
        graphicsOverlay.getGraphics().add(fruitGraphic);
    }

    public void setFruitVisibility(String fruitType, boolean visible) {
        fruitGraphics.getOrDefault(fruitType.toLowerCase(), List.of())
                .forEach(graphic -> graphic.setVisible(visible));
    }

    public Set<String> getFruitTypes() {
        return fruitGraphics.keySet();
    }

    public void clear(){
        graphicsOverlay.getGraphics().clear();
        fruitGraphics.clear();
    }
    public Map<String, List<Graphic>> getFruitGraphics() {
        return fruitGraphics;
    }


}
