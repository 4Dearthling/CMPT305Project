package com.macewan.cmpt305.edibletreesmap;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.BasemapStyle;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.Callout;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapView;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

/**
 * Main application class for the Edmonton Edible Trees interactive map.
 */
public class EdibleTreesApp extends Application {

    // Map components
    private MapView mapView;
    private GraphicsOverlay graphicsOverlay;
    private Callout callout;
    private TreeCluster cluster;
    private List<TreeCluster> clusters;

    // Services
    private TreeClusteringScript clusteringScript;
    private MapRenderer mapRenderer;

    // Data
    private static EdibleTrees edibleTrees;
    private double lastScale = -1;

    // Edmonton coordinates (City Centre)
    private static final double EDMONTON_LATITUDE = 53.5461;
    private static final double EDMONTON_LONGITUDE = -113.4938;
    private static final double INITIAL_SCALE = 100000;

    /**
     * Main method to launch the application
     */
    public static void main(String[] args) {
        Application.launch(args);
    }

    /**
     * Loads tree data from the Edmonton Open Data API
     */
    public static void loadData() throws IOException {
        try {
            EdibleTreeDAO dao = new ApiCsvEtDAO("https://data.edmonton.ca/api/v3/views/eecg-fc54/query.csv");
            edibleTrees = new EdibleTrees(dao.getAll());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void start(Stage stage) {
        // Initialize services
        clusteringScript = new TreeClusteringScript();

        // Set up the window
        stage.setTitle("Edmonton Edible Trees");
        stage.setWidth(1200);
        stage.setHeight(800);

        // Initialize the map
        initializeMap();

        // Initialize renderer (needs graphics overlay)
        mapRenderer = new MapRenderer(graphicsOverlay);

        // Create UI components
        VBox sidePane = createSidePanel();

        // Layout: map on left, side panel on right
        HBox mainPane = new HBox();
        mainPane.getChildren().addAll(mapView, sidePane);
        HBox.setHgrow(mapView, Priority.ALWAYS);
        HBox.setHgrow(sidePane, Priority.ALWAYS);

        // Set up and show the scene
        Scene scene = new Scene(mainPane);
        stage.setScene(scene);
        stage.show();

        // Load tree data in background
        loadTreeDataAsync();

        // Callout Card
        callout = mapView.getCallout();
        calloutListener(callout);
    }

    // Create a listener to create a callout when a tree point is pressed
    private void calloutListener(Callout callout) {
        mapView.setOnMouseClicked(event -> {
            // checks if there is no clustering, if the user click was from the primary mouse button and user is not panning
            if (clusters.size() == edibleTrees.getSize() && event.getButton() == MouseButton.PRIMARY && event.isStillSincePress()) {

                // Convert mouse click to usable point on mapView
                Point2D mouseClick = new Point2D(event.getX(), event.getY());
                Point mapPoint = mapView.screenToLocation(mouseClick);
                Point projectedPoint = (Point) GeometryEngine.project(mapPoint, SpatialReferences.getWgs84());

                // Gets tree closest to point clicked under a certain tolerance
                EdibleTree clickedTree = edibleTrees.getTreeByPoint(projectedPoint);

                // Create callout card
                if (clickedTree != null) {
                    callout.setCornerRadius(15);
                    callout.setTitle("Fruit Tree");
                    callout.setDetail(String.format("Fruit: %s%nSpecies: %s%nDiameter: %d%nCondition: %d%%",
                            clickedTree.getPlantBiology().getTypeFruit(), clickedTree.getPlantBiology().getSpeciesCommon(),
                            clickedTree.getPlantInfo().getDiameterBreastHeight(), clickedTree.getPlantInfo().getConditionPercent()));
                    callout.showCalloutAt(clickedTree.getPlantLocation().getPoint());
                }
            }
        });
    }

    /**
     * Initializes the ArcGIS map view centered on Edmonton
     */
    private void initializeMap() {
        // Set ArcGIS runtime environment
        String installPath = System.getProperty("user.home") + "/.arcgis/arcgis-runtime-sdk-java-200.6.0";
        ArcGISRuntimeEnvironment.setInstallDirectory(installPath);

        String apiKey = "AAPTxy8BH1VEsoebNVZXo8HurBJUIX-g4NPhmB2onUVG1IsWiBuAWl4xRgjLspLOR_N_WrKZN7mTlUQfxRIWRCZ6dX6Pn2pN17bQ6y_QSLOACjM2yRy-uqPm-OIM8uqogSsPSTznK7Q2GIuh5M_KmQCEx4SVi-RnbPylh4K0LMgSW3gwzFICn8YkFZpzEG2WYV_1No-UPeJfh0TPBeHKg81cPJ6GOfO5CsO23ZdHmCOFqUY.AT1_5klIgjI6";
        ArcGISRuntimeEnvironment.setApiKey(apiKey);

        // Create map with streets basemap
        ArcGISMap map = new ArcGISMap(BasemapStyle.ARCGIS_STREETS);
        mapView = new MapView();
        mapView.setMap(map);

        // Center on Edmonton
        Point edmontonPoint = new Point(EDMONTON_LONGITUDE, EDMONTON_LATITUDE, SpatialReferences.getWgs84());
        mapView.setViewpoint(new Viewpoint(edmontonPoint, INITIAL_SCALE));

        // Create graphics overlay for drawing
        graphicsOverlay = new GraphicsOverlay();
        mapView.getGraphicsOverlays().add(graphicsOverlay);
    }



    /**
     * Creates the side panel with title and controls
     */
    private VBox createSidePanel() {
        VBox sidePane = new VBox(10);
        sidePane.setPadding(new Insets(10));
        sidePane.setBackground(new Background(
                new BackgroundFill(Color.LIGHTSKYBLUE, CornerRadii.EMPTY, Insets.EMPTY)
        ));
        sidePane.setPrefWidth(350);

        // Add drop shadow effect
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.BLACK);
        dropShadow.setRadius(10.0);
        dropShadow.setOffsetX(5.0);
        dropShadow.setOffsetY(0.0);
        sidePane.setEffect(dropShadow);

        // Add title
        Label titleLabel = new Label("Edmonton Edible Trees");
        titleLabel.setFont(Font.font("System", 18));
        titleLabel.setStyle("-fx-font-weight: 800;");
        sidePane.getChildren().add(titleLabel);

        // Add refresh button
        Button refreshButton = new Button("Refresh Clusters");
        refreshButton.setStyle("-fx-font-size: 14px; -fx-padding: 10;");
        refreshButton.setMaxWidth(Double.MAX_VALUE);
        refreshButton.setOnAction(e -> refreshClusters());
        sidePane.getChildren().add(refreshButton);

        return sidePane;
    }

    /**
     * Loads tree data asynchronously in a background thread
     */
    private void loadTreeDataAsync() {
        new Thread(() -> {
            try {
                System.out.println("Loading tree data...");
                loadData();
                System.out.println("Data loaded successfully: " + edibleTrees.getTrees().size() + " trees");

                // Update map on JavaFX thread
                javafx.application.Platform.runLater(() -> {
                    if (edibleTrees != null) {
                        refreshClusters();
                    }
                });
            } catch (IOException e) {
                System.err.println("Error loading data: " + e.getMessage());
            }
        }).start();
    }

    /**
     * Refreshes the tree clusters on the map based on current zoom level
     */
    private void refreshClusters() {
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

        // Update last scale
        lastScale = currentScale;

        System.out.println("Refreshed clusters at scale " + currentScale +
                " - showing " + clusters.size() + " clusters");
    }

    /**
     * Clean up resources when application closes
     */
    @Override
    public void stop() {
        if (mapView != null) {
            mapView.dispose();
        }
    }
}