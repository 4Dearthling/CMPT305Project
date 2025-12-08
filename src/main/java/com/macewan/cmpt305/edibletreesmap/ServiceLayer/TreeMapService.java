package com.macewan.cmpt305.edibletreesmap.ServiceLayer;

import com.esri.arcgisruntime.mapping.view.MapView;
import com.macewan.cmpt305.edibletreesmap.DataObjectsLayer.EdibleTree;
import com.macewan.cmpt305.edibletreesmap.DataObjectsLayer.EdibleTreeDAO;
import com.macewan.cmpt305.edibletreesmap.DataObjectsLayer.EdibleTrees;
import com.macewan.cmpt305.edibletreesmap.DataAccessLayer.ApiCsvEtDAO;
import com.macewan.cmpt305.edibletreesmap.DataObjectsLayer.TreeCluster;
import com.macewan.cmpt305.edibletreesmap.UILayer.MapInteractionHandler;
import com.macewan.cmpt305.edibletreesmap.UILayer.MapRenderer;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TreeMapService {
    /*
    * Deals with all the logic between the UI Elements and the Data
    * */
    private TreeClusteringScript clusteringScript;
    private MapRenderer mapRenderer;
    private final MapView mapView;
    private MapInteractionHandler mapInteractionHandler;

    private EdibleTrees edibleTrees;
    private List<TreeCluster> clusters;
    private Set<String> enabledFruitTypes;
    public TreeMapService(MapView mapView, MapRenderer mapRenderer) {
        this.mapView = mapView;
        this.mapRenderer = mapRenderer;
        this.clusteringScript = new TreeClusteringScript();
    }
    public void loadData() throws IOException {
        try {
            EdibleTreeDAO dao = new ApiCsvEtDAO("https://data.edmonton.ca/api/v3/views/eecg-fc54/query.csv");
            this.edibleTrees = new EdibleTrees(dao.getAll());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public void setMapInteractionHandler(MapInteractionHandler mapInteractionHandler) {
        this.mapInteractionHandler = mapInteractionHandler;
    }

    /**
     * Refreshes the tree clusters on the map based on current zoom level.
     * Uses previously set enabled fruit types, or all trees if none set.
     */
    public void refreshClusters() {
        refreshClusters(this.enabledFruitTypes);
    }

    /**
     * Refreshes the tree clusters on the map based on current zoom level,
     * only including trees with fruit types in the enabled set.
     *
     * @param enabledFruitTypes Set of fruit type names to include, or null for all
     */
    public void refreshClusters(Set<String> enabledFruitTypes) {
        if (edibleTrees == null) {
            return;
        }

        this.enabledFruitTypes = enabledFruitTypes;

        // Filter trees by enabled fruit types
        List<EdibleTree> treesToCluster;
        if (enabledFruitTypes == null) {
            // null means no filter applied - show all trees
            treesToCluster = edibleTrees.getTrees();
        } else if (enabledFruitTypes.isEmpty()) {
            // empty set means nothing selected - show no trees
            treesToCluster = List.of();
        } else {
            treesToCluster = edibleTrees.getTrees().stream()
                    .filter(tree -> {
                        String fruitType = tree.getPlantBiology().getTypeFruit().toLowerCase();
                        return enabledFruitTypes.contains(fruitType);
                    })
                    .collect(Collectors.toList());
        }

        // Get current map scale to determine clustering level
        double currentScale = mapView.getMapScale();
        double clusterDistance = clusteringScript.getClusterDistance(currentScale);

        // Cluster the filtered trees
        clusters = clusteringScript.clusterTrees(
                treesToCluster,
                clusterDistance
        );

        // Draw the clusters on the map
        // Only show individual tree markers when fully zoomed in (clusterDistance == 0)
        boolean showIndividualTrees = (clusterDistance == 0);
        mapRenderer.drawClusters(clusters, showIndividualTrees);

        // Clear the radius circle when refreshing clusters
        mapRenderer.clearRadius();

        if (mapInteractionHandler != null) {
            mapInteractionHandler.clearCallout();
            mapInteractionHandler.setCluster(clusters, showIndividualTrees);
            mapInteractionHandler.clearRadius();
        }

        System.out.println("Refreshed clusters at scale " + currentScale +
                " - showing " + clusters.size() + " clusters from " + treesToCluster.size() + " filtered trees");
    }

    public EdibleTrees getEdibleTrees() {
        return edibleTrees;
    }
    public List<TreeCluster> getClusters() {
        return clusters;
    }


}
