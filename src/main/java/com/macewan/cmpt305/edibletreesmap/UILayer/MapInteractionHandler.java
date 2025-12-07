package com.macewan.cmpt305.edibletreesmap.UILayer;

import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.view.Callout;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.macewan.cmpt305.edibletreesmap.DataObjectsLayer.EdibleTree;
import com.macewan.cmpt305.edibletreesmap.DataObjectsLayer.EdibleTrees;
import com.macewan.cmpt305.edibletreesmap.DataObjectsLayer.TreeCluster;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseButton;

import java.util.List;

import static javafx.scene.input.MouseEvent.MOUSE_CLICKED;

public class MapInteractionHandler {
    /*
    * Anything that the user does is dealt with here
    * */
    private final MapView mapView;
    private final Callout callout;
    private final EdibleTrees edibleTrees;
    private List<TreeCluster> clusters;

        public MapInteractionHandler(MapView mapView, EdibleTrees edibleTrees) {
            this.mapView = mapView;
            this.callout = mapView.getCallout();
            this.edibleTrees = edibleTrees;
            //calloutListener();

        }

        private void calloutListener() {

            mapView.setOnMouseClicked(event -> {
                System.out.println("=== CLICK DETECTED ===");
                System.out.println("Clusters: " + (clusters == null ? "null" : clusters.size()));
                System.out.println("Trees: " + edibleTrees.getSize());
                System.out.println("Match: " + (clusters != null && clusters.size() == edibleTrees.getSize()));

                // checks if there is no clustering, if the user click was from the primary mouse button and user is not panning
                if (clusters != null && clusters.size() == edibleTrees.getSize() && event.getButton() == MouseButton.PRIMARY && event.isStillSincePress()) {
                    handleTreeClick(event.getX(), event.getY());
                    // Convert mouse click to usable point on mapView

                }
            });
        }

        public void setCluster(List<TreeCluster> clusters) {
            this.clusters = clusters;
        }

        private void handleTreeClick(double x, double y) {
            // Map click to point on Map
            Point2D mouseClick = new Point2D(x, y);
            Point mapPoint = mapView.screenToLocation(mouseClick);
            Point projectedPoint = (Point) GeometryEngine.project(mapPoint, SpatialReferences.getWgs84());

            // Gets tree closest to point clicked under a certain tolerance
            EdibleTree clickedTree = edibleTrees.getTreeByPoint(projectedPoint);

            if (clickedTree != null) {
                showTreeCallout(clickedTree);
            }

        }
        public void clearCallout() {
            callout.dismiss();
        }

        private void showTreeCallout(EdibleTree clickedTree) {
            // Formats the callout card
            callout.setCornerRadius(15);
            callout.setTitle("Fruit Tree");
            callout.setDetail(String.format("Fruit: %s%nSpecies: %s%nDiameter: %d%nCondition: %d%%",
                    clickedTree.getPlantBiology().getTypeFruit(), clickedTree.getPlantBiology().getSpeciesCommon(),
                    clickedTree.getPlantInfo().getDiameterBreastHeight(), clickedTree.getPlantInfo().getConditionPercent()));
            callout.showCalloutAt(clickedTree.getPlantLocation().getPoint());
        }

}
