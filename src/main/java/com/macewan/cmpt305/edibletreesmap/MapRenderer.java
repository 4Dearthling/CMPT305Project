package com.macewan.cmpt305.edibletreesmap;

import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.symbology.CompositeSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.Symbol;
import com.esri.arcgisruntime.symbology.TextSymbol;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Handles rendering of tree clusters on the ArcGIS map.
 */
public class MapRenderer {

    private final GraphicsOverlay graphicsOverlay;

    /**
     * Creates a new MapRenderer
     *
     * @param graphicsOverlay The graphics overlay to draw on
     */
    public MapRenderer(GraphicsOverlay graphicsOverlay) {
        this.graphicsOverlay = graphicsOverlay;
    }

    /**
     * Draws tree clusters on the map.
     * Single-tree clusters are drawn as individual markers.
     * Multi-tree clusters are drawn as colored circles with counts.
     *
     * @param clusters List of tree clusters to draw
     */
    public void drawClusters(List<TreeCluster> clusters, Map<String, List<Graphic>> fruitGraphics) {
        // Clear existing graphics
        graphicsOverlay.getGraphics().clear();

        // Draw each cluster
        for (TreeCluster cluster : clusters) {
            Point centerPoint = cluster.getCenterPoint();

            if (centerPoint == null) {
                continue; // Skip empty clusters
            }

            if (cluster.getTreeCount() == 1) {
                // Single tree - draw as individual marker
                EdibleTree singleTree = cluster.getTrees().getFirst();
                MapTrees.drawTrees(singleTree, graphicsOverlay, fruitGraphics);
                //drawIndividualTree(singleTree);
            } else {
                // Multiple trees - draw as cluster
                drawCluster(centerPoint, cluster.getTreeCount());
            }
        }
    }

    /**
     * Draws an individual tree marker.
     * Displayed as a small green circle.
     *
     * @param tree the tree of a single cluster
     */
//    private void drawIndividualTree(EdibleTree tree) {
//        String fruitType = tree.getPlantBiology().getTypeFruit();
//        if (fruitType == null) {
//            fruitType = "";
//        }
//        String key = fruitType.trim().toLowerCase();
//
//        // picking a colour based on fruit type
//        Color color = getColorForFruit(key);
//        SimpleMarkerSymbol symbol =
//                new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, color, 10);
//
//        Graphic graphic = new Graphic(tree.getPlantLocation().getPoint(), symbol);
//
//        // add to overlay
//        graphicsOverlay.getGraphics().add(graphic);
//
//        //remember which graphics belong to which fruit
//        fruitGraphics
//                .computeIfAbsent(key, k -> new ArrayList<>())
//                .add(graphic);
////        SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(
////                SimpleMarkerSymbol.Style.CIRCLE,
////                Color.rgb(34, 139, 34), // Forest Green
////                8
////        );
////
////        Graphic graphic = new Graphic(point, symbol);
//        graphicsOverlay.getGraphics().add(graphic);
//    }
//


    /**
     * Draws a cluster marker with count label.
     * Color and size vary based on cluster size:
     * - Green (small): < 10 trees
     * - Yellow (medium): 10-49 trees
     * - Orange (large): 50-99 trees
     * - Red (very large): 100+ trees
     *
     * @param point The location to draw the cluster marker
     * @param count The number of trees in the cluster
     */
    private void drawCluster(Point point, int count) {
        // Determine color and size based on cluster size
        Color color;
        double size;

        if (count < 10) {
            color = Color.rgb(50, 205, 50); // Green
            size = 20;
        } else if (count < 50) {
            color = Color.rgb(255, 215, 0); // Yellow
            size = 30;
        } else if (count < 100) {
            color = Color.rgb(255, 140, 0); // Orange
            size = 40;
        } else {
            color = Color.rgb(220, 20, 60); // Red
            size = 50;
        }

        // Create circle marker
        SimpleMarkerSymbol circleSymbol = new SimpleMarkerSymbol(
                SimpleMarkerSymbol.Style.CIRCLE,
                color,
                (float) size
        );

        // Create text label showing tree count
        TextSymbol textSymbol = new TextSymbol(
                14,                                      // font size
                String.valueOf(count),                   // text
                Color.WHITE,                             // color
                TextSymbol.HorizontalAlignment.CENTER,
                TextSymbol.VerticalAlignment.MIDDLE
        );
        textSymbol.setHaloColor(Color.BLACK);            // Black outline for readability
        textSymbol.setHaloWidth(1);

        // Combine circle and text into composite symbol
        List<Symbol> symbols = new ArrayList<>();
        symbols.add(circleSymbol);
        symbols.add(textSymbol);
        CompositeSymbol compositeSymbol = new CompositeSymbol(symbols);

        // Add to map
        Graphic graphic = new Graphic(point, compositeSymbol);
        graphicsOverlay.getGraphics().add(graphic);
    }

    /**
     * Clears all graphics from the map
     */
    public void clear() {
        graphicsOverlay.getGraphics().clear();
    }
}