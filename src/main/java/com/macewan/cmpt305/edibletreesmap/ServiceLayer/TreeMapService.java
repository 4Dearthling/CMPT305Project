package com.macewan.cmpt305.edibletreesmap.ServiceLayer;

import com.esri.arcgisruntime.mapping.view.MapView;
import com.macewan.cmpt305.edibletreesmap.DataObjectsLayer.EdibleTreeDAO;
import com.macewan.cmpt305.edibletreesmap.DataObjectsLayer.EdibleTrees;
import com.macewan.cmpt305.edibletreesmap.DataAccessLayer.ApiCsvEtDAO;
import com.macewan.cmpt305.edibletreesmap.DataObjectsLayer.TreeCluster;
import com.macewan.cmpt305.edibletreesmap.UILayer.MapInteractionHandler;
import com.macewan.cmpt305.edibletreesmap.UILayer.MapRenderer;

import java.io.IOException;
import java.util.List;

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
     * Refreshes the tree clusters on the map based on current zoom level
     */
    public void refreshClusters() {
        if (edibleTrees == null) {
            return;
        }

        // Get current map scale to determine clustering level
        double currentScale = mapView.getMapScale();
        double clusterDistance = clusteringScript.getClusterDistance(currentScale);

        // Cluster the trees
        clusters = clusteringScript.clusterTrees(
                edibleTrees.getTrees(),
                clusterDistance
        );

        // Draw the clusters on the map
        mapRenderer.drawClusters(clusters);


        if (mapInteractionHandler != null) {
            mapInteractionHandler.clearCallout();
            mapInteractionHandler.setCluster(clusters);
        }

        System.out.println("Refreshed clusters at scale " + currentScale +
                " - showing " + clusters.size() + " clusters");
    }

    public EdibleTrees getEdibleTrees() {
        return edibleTrees;
    }
    public List<TreeCluster> getClusters() {
        return clusters;
    }


}
