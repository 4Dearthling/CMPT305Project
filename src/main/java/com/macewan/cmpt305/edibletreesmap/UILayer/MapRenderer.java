package com.macewan.cmpt305.edibletreesmap.UILayer;

import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.symbology.CompositeSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.Symbol;
import com.esri.arcgisruntime.symbology.TextSymbol;
import com.macewan.cmpt305.edibletreesmap.DataObjectsLayer.EdibleTree;
import com.macewan.cmpt305.edibletreesmap.DataObjectsLayer.TreeCluster;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles rendering of tree clusters on the ArcGIS map.
 */
public class MapRenderer {
    /*
    * Creates all the UI elements on the map
    */

    private final GraphicsOverlay graphicsOverlay;
    private final TreeGraphicsManager treeGraphicsManager;

    /**
     * Creates a new MapRenderer
     *
     * @param graphicsOverlay The graphics overlay to draw on
     * @param treeGraphicsManager A graphics manager ot manage all the individual tree graphics
     */
    public MapRenderer(TreeGraphicsManager treeGraphicsManager, GraphicsOverlay graphicsOverlay) {
        this.graphicsOverlay = graphicsOverlay;
        this.treeGraphicsManager = treeGraphicsManager;
    }

    public void drawIndividualTrees(EdibleTree tree ) {

        String fruitType = tree.getPlantBiology().getTypeFruit();
        if (fruitType == null) {
            fruitType = "";
        }
        Color color = FruitColorMapper.getColor(fruitType);

        SimpleMarkerSymbol symbol =
                new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, color, 10);

        Graphic graphic = new Graphic(tree.getPlantLocation().getPoint(), symbol);

        // add to tree graphics
        treeGraphicsManager.addTreeGraphics(fruitType, graphic);
    }




    /**
     * Draws tree clusters on the map.
     * Single-tree clusters are drawn as individual markers only when fully zoomed in.
     * Otherwise, all clusters (including single-tree) are drawn as bubbles with counts.
     *
     * @param clusters List of tree clusters to draw
     */
    public void drawClusters(List<TreeCluster> clusters) {
        drawClusters(clusters, false);
    }

    /**
     * Draws tree clusters on the map.
     *
     * @param clusters List of tree clusters to draw
     * @param showIndividualTrees If true, single-tree clusters show as colored dots; if false, show as bubbles with "1"
     */
    public void drawClusters(List<TreeCluster> clusters, boolean showIndividualTrees) {
        // Clear existing graphics
        treeGraphicsManager.clear();

        // Draw each cluster
        for (TreeCluster cluster : clusters) {
            Point centerPoint = cluster.getCenterPoint();

            if (centerPoint == null) {
                continue; // Skip empty clusters
            }

            if (cluster.getTreeCount() == 1 && showIndividualTrees) {
                // Single tree at full zoom - draw as individual marker
                EdibleTree singleTree = cluster.getTrees().getFirst();
                drawIndividualTrees(singleTree);
            } else {
                // Draw as cluster bubble (including single-tree clusters when not fully zoomed)
                drawCluster(centerPoint, cluster.getTreeCount());
            }
        }
    }



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
            color = Color.rgb(42, 157, 143);
            size = 20;
        } else if (count < 50) {
            color = Color.rgb(233, 196, 106);
            size = 30;
        } else if (count < 100) {
            color = Color.rgb(244, 162, 97);
            size = 40;
        } else {
            color = Color.rgb(231, 111, 81);
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
                13,                                 // font size
                String.valueOf(count),                   // text
                Color.WHITE,                             // color
                TextSymbol.HorizontalAlignment.CENTER,
                TextSymbol.VerticalAlignment.MIDDLE
        );
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