package com.macewan.cmpt305.edibletreesmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Responsible for clustering edible trees based on their location.
 */
public class TreeClusteringScript {

    /**
     * Groups nearby trees into clusters for efficient map visualization.
     *
     * @param trees The list of all edible trees to cluster
     * @param clusterDistance Grid cell size in degrees (0.1 ≈ 10km, 0.01 ≈ 1km, 0 = no clustering)
     * @return List of TreeClusters, each containing trees from one grid cell
     */
    public List<TreeCluster> clusterTrees(List<EdibleTree> trees, double clusterDistance) {

        // When zoomed in very close show every tree individually
        if (clusterDistance == 0) {
            return createIndividualClusters(trees);
        }

        // Store clusters by using a grid cell format: key = "gridLat,gridLon", value = cluster
        Map<String, TreeCluster> gridClusters = new HashMap<>();

        // Process each tree and assign it to a grid cell
        for (EdibleTree tree : trees) {
            // Get tree coordinates
            double lat = tree.getPlantLocation().getLatitude().doubleValue();
            double lon = tree.getPlantLocation().getLongitude().doubleValue();

            // Calculate which grid cell this tree belongs to
            String gridKey = calculateGridKey(lat, lon, clusterDistance);

            // Get existing cluster for this grid cell, or create new one
            TreeCluster cluster = gridClusters.get(gridKey);
            if (cluster == null) {
                cluster = new TreeCluster();
                gridClusters.put(gridKey, cluster);
            }

            // Add tree to its cluster
            cluster.addTree(tree);
        }

        // Return all clusters (converts HashMap values to List)
        return new ArrayList<>(gridClusters.values());
    }

    /**
     * Determines the appropriate cluster distance based on map scale.
     * Larger scales (zoomed out) use larger grid cells for fewer, bigger clusters.
     * Smaller scales (zoomed in) use smaller grid cells for more, detailed clusters.
     *
     * @param scale The current map scale
     * @return The cluster distance in degrees
     */
    public double getClusterDistance(double scale) {
        if (scale > 500000) {
            return 0.15; // Very zoomed out - ~15km grid cells
        } else if (scale > 300000) {
            return 0.08; // Zoomed out - ~8km
        } else if (scale > 200000) {
            return 0.04; // Far - ~4km
        } else if (scale > 100000) {
            return 0.02; // Medium zoom - ~2km
        } else if (scale > 50000) {
            return 0.01; // Getting closer - ~1km
        } else if (scale > 20000) {
            return 0.005; // Close up - ~500m
        } else if (scale > 5000) {
            return 0.002; // Very close - ~200m
        } else {
            return 0; // Extremely close - no clustering
        }
    }

    /**
     * Creates individual clusters for each tree.
     * Used when zoomed in very close and clustering is not needed.
     *
     * @param trees The list of trees
     * @return List of clusters, each containing exactly one tree
     */
    private List<TreeCluster> createIndividualClusters(List<EdibleTree> trees) {
        List<TreeCluster> clusters = new ArrayList<>();
        for (EdibleTree tree : trees) {
            TreeCluster cluster = new TreeCluster();
            cluster.addTree(tree);
            clusters.add(cluster);
        }
        return clusters;
    }

    /**
     * Calculates the grid cell key for a given coordinate.
     * Trees with the same grid key will be in the same cluster.
     *
     * @param lat Latitude coordinate
     * @param lon Longitude coordinate
     * @param clusterDistance Grid cell size
     * @return String key representing the grid cell (e.g., "535,-1135")
     */
    private String calculateGridKey(double lat, double lon, double clusterDistance) {
        // Divide by grid size and round down to get grid cell numbers
        // Example: lat 53.54 ÷ 0.1 = 535.4 → floor = 535
        // All trees from lat 53.50-53.59 get gridLat = 535
        int gridLat = (int) Math.floor(lat / clusterDistance);
        int gridLon = (int) Math.floor(lon / clusterDistance);

        // Combine string key
        return gridLat + "," + gridLon;
    }
}