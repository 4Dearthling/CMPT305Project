package com.macewan.cmpt305.edibletreesmap.UILayer;

import javafx.scene.paint.Color;

import java.util.Map;

public class FruitColorMapper {
    private static final Map<String, Color> FRUIT_COLORS = Map.ofEntries(
            Map.entry("apple", Color.RED),
            Map.entry("cherry", Color.HOTPINK),
            Map.entry("crabapple", Color.ORANGE),
            Map.entry("plum", Color.PLUM),
            Map.entry("pear", Color.LIMEGREEN),
            Map.entry("chokecherry", Color.PURPLE),
            Map.entry("acorn", Color.BROWN),
            Map.entry("hawthorn", Color.LIGHTSKYBLUE),
            Map.entry("juniper", Color.GREENYELLOW),
            Map.entry("butternut", Color.GOLD),
            Map.entry("saskatoon", Color.SALMON),
            Map.entry("russian olive", Color.LAVENDER),
            Map.entry("coffeetree pod", Color.GRAY),
            Map.entry("walnut", Color.BLACK),
            Map.entry("hackberry", Color.NAVY),
            Map.entry("caragana flower/pod", Color.DARKGREEN)
    );

    private static final Color DEFAULT_COLOR = Color.GREEN;


    public static Color getColor(String fruitType) {
        if (fruitType == null) {
            return DEFAULT_COLOR;
        }

        String key = fruitType.trim().toLowerCase();
        return FRUIT_COLORS.getOrDefault(key, DEFAULT_COLOR);
    }
}