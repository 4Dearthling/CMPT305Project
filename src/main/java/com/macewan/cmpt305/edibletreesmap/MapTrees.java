package com.macewan.cmpt305.edibletreesmap;

import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapTrees {
    // reference to fruitGraphics from the app
    //private final Map<String, List<Graphic>> fruitGraphics = new HashMap<>();

    public static void drawTrees(EdibleTree tree, GraphicsOverlay graphicsOverlay, Map<String, List<Graphic>> fruitGraphics ) {

        String fruitType = tree.getPlantBiology().getTypeFruit();
        if (fruitType == null) {
            fruitType = "";
        }
        String key = fruitType.trim().toLowerCase();

        // picking a colour based on fruit type
        Color color = getColorForFruit(key);
        SimpleMarkerSymbol symbol =
                new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, color, 10);

        Graphic graphic = new Graphic(tree.getPlantLocation().getPoint(), symbol);

        // add to overlay
        graphicsOverlay.getGraphics().add(graphic);

        //remember which graphics belong to which fruit
        fruitGraphics
                .computeIfAbsent(key, k -> new ArrayList<>())
                .add(graphic);
    }

    private static Color getColorForFruit(String key) {
        // key is lowercase
        return switch (key) {
            case "apple" -> Color.RED;
            case "cherry" -> Color.HOTPINK;
            case "crabapple" -> Color.ORANGE;
            case "plum" -> Color.PLUM;
            case "pear" -> Color.LIMEGREEN;
            case "chokecherry" -> Color.PURPLE;
            case "acorn" -> Color.BROWN;
            case "hawthorn" -> Color.LIGHTSKYBLUE;
            case "juniper" -> Color.GREENYELLOW;
            case "butternut" -> Color.GOLD;
            case "saskatoon" -> Color.SALMON;
            case "russian olive" -> Color.LAVENDER;
            case "coffeetree pod" -> Color.GRAY;
            case "walnut" -> Color.BLACK;
            case "hackberry" -> Color.NAVY;
            case "caragana flower/pod" -> Color.DARKGREEN;
            default -> Color.GREEN;
        };
    }
}