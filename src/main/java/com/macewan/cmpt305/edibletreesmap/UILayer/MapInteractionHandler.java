package com.macewan.cmpt305.edibletreesmap.UILayer;

import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.view.Callout;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.macewan.cmpt305.edibletreesmap.DataObjectsLayer.EdibleTree;
import com.macewan.cmpt305.edibletreesmap.DataObjectsLayer.EdibleTrees;
import com.macewan.cmpt305.edibletreesmap.DataObjectsLayer.TreeCluster;
import javafx.animation.PauseTransition;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.List;

public class MapInteractionHandler {
    /*
    * Anything that the user does is dealt with here
    * */
    private final MapView mapView;
    private final Callout callout;
    private final EdibleTrees edibleTrees;
    private List<TreeCluster> clusters;
    private boolean showingIndividualTrees = false;
    private final PauseTransition hoverPause = new PauseTransition(Duration.millis(200));
    private Point2D lastPoint;

    public MapInteractionHandler(MapView mapView, EdibleTrees edibleTrees) {
        this.mapView = mapView;
        this.callout = mapView.getCallout();
        this.edibleTrees = edibleTrees;
        //calloutClickListener();
        calloutHoverListener();

    }
    private void calloutHoverListener() {
        // When hover pause timer finishes, show callout for tree at last position
        hoverPause.setOnFinished(event -> {
            // Only show callout when displaying individual trees (not clustered)
            if (!showingIndividualTrees) {
                return;
            }
            if (lastPoint == null) {
                return;
            }
            Point mapPoint = convertToMapPoint(lastPoint.getX(), lastPoint.getY());
            if (mapPoint == null) {
                callout.dismiss();
                return;
            }

            EdibleTree hoveredTree = edibleTrees.getTreeByPoint(mapPoint);

            if (hoveredTree != null) {
                showTreeCallout(hoveredTree);
            } else {
                callout.dismiss();
            }
        });

        // Register mouse move listener to track hover position
        mapView.addEventFilter(MouseEvent.MOUSE_MOVED, e -> {
            // ignore while dragging
            if (e.isPrimaryButtonDown()) {
                return;
            }

            lastPoint = new Point2D(e.getX(), e.getY());
            // restart timer on each move
            hoverPause.playFromStart();
        });

        // when mouse leaves mapView
        mapView.setOnMouseExited(e -> {
            hoverPause.stop();
            callout.dismiss();
        });
    }

    private Point convertToMapPoint(double x, double y) {
        // Map click to point on Map
        Point2D mouseClick = new Point2D(x, y);
        Point mapPoint = mapView.screenToLocation(mouseClick);
        return (Point) GeometryEngine.project(mapPoint, SpatialReferences.getWgs84());
    }


    private void calloutClickListener() {
        mapView.setOnMouseClicked(event -> {
            // checks if there is no clustering, if the user click was from the primary mouse button and user is not panning
            if (clusters.size() == edibleTrees.getSize() && event.getButton() == MouseButton.PRIMARY && event.isStillSincePress()) {
                handleTreeClick(event.getX(), event.getY());
                // Convert mouse click to usable point on mapView

            }
        });
    }

    public void setCluster(List<TreeCluster> clusters, boolean showingIndividualTrees) {
        this.clusters = clusters;
        this.showingIndividualTrees = showingIndividualTrees;
    }

    private void handleTreeClick(double x, double y) {
        // Map click to point on Map
        Point projectedPoint = convertToMapPoint(x, y);

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

    public void radiusListener(double radius) {
        mapView.setOnMouseClicked(event -> {
            drawRadius(event.getX(), event.getY(), radius);
        });
    }
    private void drawRadius(double x, double y, double radius) {
        // Map click to point on Map
        Point2D mouseClick = new Point2D(x, y);
        Point mapPoint = mapView.screenToLocation(mouseClick);
        Point projectedPoint = (Point) GeometryEngine.project(mapPoint, SpatialReferences.getWgs84());

        SimpleMarkerSymbol centerOfCircle = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.ALICEBLUE, 2);

    }


}
