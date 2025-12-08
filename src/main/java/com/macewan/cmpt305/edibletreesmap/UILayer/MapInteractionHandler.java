package com.macewan.cmpt305.edibletreesmap.UILayer;

import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.view.Callout;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.macewan.cmpt305.edibletreesmap.DataObjectsLayer.EdibleTree;
import com.macewan.cmpt305.edibletreesmap.DataObjectsLayer.EdibleTrees;
import com.macewan.cmpt305.edibletreesmap.DataObjectsLayer.TreeCluster;
import javafx.animation.PauseTransition;
import javafx.geometry.Point2D;
import javafx.scene.control.Slider;
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
    private RadiusSlider radiusSlider;
    private MapRenderer mapRenderer;
    private Point radiusCenter;
    private TreePieChart pieChart;

    public MapInteractionHandler(MapView mapView, EdibleTrees edibleTrees, RadiusSlider radiusSlider, MapRenderer mapRenderer) {
        this.mapView = mapView;
        this.callout = mapView.getCallout();
        this.edibleTrees = edibleTrees;
        this.radiusSlider = radiusSlider;
        this.mapRenderer = mapRenderer;
        calloutClickListener();
        calloutHoverListener();
        initRadiusListener();
    }

    public void setPieChart(TreePieChart pieChart) {
        this.pieChart = pieChart;
        pieChart.setEdibleTrees(edibleTrees);
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
            clearCallout();
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
            System.out.println("CLICKED");
            // checks if there is no clustering, if the user click was from the primary mouse button and user is not panning
            if (showingIndividualTrees && event.getButton() == MouseButton.PRIMARY && event.isStillSincePress()) {
                System.out.println("clicked");
                radiusCenter = convertToMapPoint(event.getX(), event.getY());
                // Draw radius immediately on click
                double radius = radiusSlider.getRadius();
                mapRenderer.drawRadius(radiusCenter, radius);
                // Enable the slider now that a center point is set
                radiusSlider.setEnabled(true);
                // Update pie chart
                if (pieChart != null) {
                    pieChart.updateChart(radiusCenter, radius);
                }
                System.out.println("radius set");
            }
        });
    }

    public void setCluster(List<TreeCluster> clusters, boolean showingIndividualTrees) {
        this.clusters = clusters;
        this.showingIndividualTrees = showingIndividualTrees;
    }

//    private void handleTreeClick(double x, double y) {
//        // Map click to point on Map
//        Point projectedPoint = convertToMapPoint(x, y);
//
//        // Gets tree closest to point clicked under a certain tolerance
//        EdibleTree clickedTree = edibleTrees.getTreeByPoint(projectedPoint);
//
//        if (clickedTree != null) {
//            showTreeCallout(clickedTree);
//        }
//
//    }
    public void clearCallout() {
        callout.dismiss();
    }

    /**
     * Clears the radius selection, disables the slider, and clears the pie chart
     */
    public void clearRadius() {
        radiusCenter = null;
        radiusSlider.setEnabled(false);
        if (pieChart != null) {
            pieChart.clearChart();
        }
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

    /**
     * Initialize the radius slider listener once during construction.
     * This avoids adding multiple listeners on each click.
     */
    private void initRadiusListener() {
        radiusSlider.getSlider().valueProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Slider changed: " + oldValue + " km -> " + newValue + " km");
            if (radiusCenter == null || mapRenderer.getCircleGraphic() == null) {
                System.out.println("radiusCenter or circleGraphic is null");
                return;
            }
            // Convert km to metres
            double radiusInMetres = newValue.doubleValue() * 1000;
            System.out.println("Updating radius to: " + radiusInMetres + " metres");
            mapRenderer.updateRadius(radiusCenter, radiusInMetres);
            // Update pie chart dynamically as slider moves
            if (pieChart != null) {
                pieChart.updateChart(radiusCenter, radiusInMetres);
            }
        });
    }




}
