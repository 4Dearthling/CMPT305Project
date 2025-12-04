package com.macewan.cmpt305.edibletreesmap;

import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a cluster of edible trees at a similar location.
 * Used for grouping nearby trees together for map visualization.
 */
public class TreeCluster {
    private List<EdibleTree> trees;

    /**
     * Creates a new empty tree cluster
     */
    public TreeCluster() {
        this.trees = new ArrayList<>();
    }

    /**
     * Adds a tree to this cluster
     *
     * @param tree The tree to add
     */
    public void addTree(EdibleTree tree) {
        trees.add(tree);
    }

    /**
     * Gets the number of trees in this cluster
     *
     * @return The count of trees
     */
    public int getTreeCount() {
        return trees.size();
    }

    /**
     * Calculates the center point of this cluster.
     * Uses the average latitude and longitude of all trees in the cluster.
     *
     * @return The center point, or null if cluster is empty
     */
    public Point getCenterPoint() {
        if (trees.isEmpty()) {
            return null;
        }

        // Calculate average latitude
        double avgLat = trees.stream()
                .mapToDouble(t -> t.getPlantLocation().getLatitude().doubleValue())
                .average()
                .orElse(0);

        // Calculate average longitude
        double avgLon = trees.stream()
                .mapToDouble(t -> t.getPlantLocation().getLongitude().doubleValue())
                .average()
                .orElse(0);

        return new Point(avgLon, avgLat, SpatialReferences.getWgs84());
    }

    /**
     * Gets all trees in this cluster
     *
     * @return List of trees (defensive copy)
     */
    public List<EdibleTree> getTrees() {
        return new ArrayList<>(trees);
    }
}