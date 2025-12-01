package com.macewan.cmpt305.edibletreesmap;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.BasemapStyle;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol;
import com.esri.arcgisruntime.symbology.CompositeSymbol;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.text.Font;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Displays an interactive map of Edmonton with manually clustered edible trees
 */
public class EdibleTreesApp extends Application {

    private MapView mapView;
    private GraphicsOverlay graphicsOverlay;
    private static EdibleTrees edibleTrees;
    private double lastScale = -1;

    // Edmonton coordinates (City Centre)
    private static final double EDMONTON_LATITUDE = 53.5461;
    private static final double EDMONTON_LONGITUDE = -113.4938;
    private static final double INITIAL_SCALE = 100000; // Scale for viewing Edmonton

    /**
     * Main method to launch the application
     */
    public static void main(String[] args) {
        Application.launch(args);
    }

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
        // Set the title and size of the stage (window)
        stage.setTitle("Edmonton Edible Trees");
        stage.setWidth(1200);
        stage.setHeight(800);

        // Initialize the map view
        initializeMap();

        // Add control panel
        VBox sidePane = getVBox();
        sidePane.getChildren().add(title());

        // Create refresh button for clustering
        Button refreshButton = new Button("Refresh Clusters");
        refreshButton.setStyle("-fx-font-size: 14px; -fx-padding: 10;");
        refreshButton.setMaxWidth(Double.MAX_VALUE); // Makes the button full width
        refreshButton.setOnAction(e -> {
            if (edibleTrees != null) {
                updateClusters();
                lastScale = mapView.getMapScale();
            }
        });

        sidePane.getChildren().add(refreshButton);

        // Create UI with Map and Side Panel side by side
        HBox mainPane = new HBox();
        mainPane.getChildren().addAll(mapView, sidePane);

        // Allow mapView to expand as needed
        HBox.setHgrow(mapView, Priority.ALWAYS);
        HBox.setHgrow(sidePane, Priority.ALWAYS);

        // Set the scene to the stage
        Scene scene = new Scene(mainPane);
        stage.setScene(scene);

        // Show the stage
        stage.show();

        // Load data asynchronously after the map is initialized
        new Thread(() -> {
            try {
                loadData();

                // Update the map on JavaFX thread
                javafx.application.Platform.runLater(() -> {
                    if (edibleTrees != null) {
                        drawTreesClustered(edibleTrees);
                        lastScale = mapView.getMapScale();
                    }
                });
            } catch (IOException e) {
                System.err.println("Error loading data: " + e.getMessage());
            }
        }).start();
    }

    private static Label title() {
        // Create Label
        final Label label = new Label("Edmonton Edible Trees");
        label.setStyle("-fx-font-size: 18px;");
        label.setFont(Font.font("System", 18));
        label.setStyle("-fx-font-weight: 800;");
        return label;
    }

    private static VBox getVBox() {
        // Create a DropShadow effect
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.BLACK); // Set the shadow color
        dropShadow.setRadius(10.0);       // Set the blur radius
        dropShadow.setOffsetX(5.0);       // Positive value shifts the shadow to the right
        dropShadow.setOffsetY(0.0);

        VBox sidePane = new VBox(10);
        sidePane.setPadding(new Insets(10));
        sidePane.setBackground(new Background(new BackgroundFill(Color.LIGHTSKYBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        sidePane.setPrefWidth(350);
        sidePane.setEffect(dropShadow);
        return sidePane;
    }

    /**
     * Initialize the map view with Edmonton centered
     */
    private void initializeMap() {
        // Set ArcGIS API key
        String installPath = System.getProperty("user.home") + "/.arcgis/arcgis-runtime-sdk-java-200.6.0";
        System.out.println("Setting ArcGIS install directory to: " + installPath);
        ArcGISRuntimeEnvironment.setInstallDirectory(installPath);

        String apiKey = "AAPTxy8BH1VEsoebNVZXo8HurBJUIX-g4NPhmB2onUVG1IsWiBuAWl4xRgjLspLOR_N_WrKZN7mTlUQfxRIWRCZ6dX6Pn2pN17bQ6y_QSLOACjM2yRy-uqPm-OIM8uqogSsPSTznK7Q2GIuh5M_KmQCEx4SVi-RnbPylh4K0LMgSW3gwzFICn8YkFZpzEG2WYV_1No-UPeJfh0TPBeHKg81cPJ6GOfO5CsO23ZdHmCOFqUY.AT1_5klIgjI6";
        ArcGISRuntimeEnvironment.setApiKey(apiKey);

        // Create a map with a streets basemap style
        ArcGISMap map = new ArcGISMap(BasemapStyle.ARCGIS_STREETS);

        // Create a map view and set the map to it
        mapView = new MapView();
        mapView.setMap(map);

        // Create a point for Edmonton city center
        Point edmontonPoint = new Point(
                EDMONTON_LONGITUDE,
                EDMONTON_LATITUDE,
                SpatialReferences.getWgs84()
        );

        // Set the viewpoint to center on Edmonton
        mapView.setViewpoint(new Viewpoint(edmontonPoint, INITIAL_SCALE));

        // Create graphics overlay to draw on map
        graphicsOverlay = new GraphicsOverlay();
        mapView.getGraphicsOverlays().add(graphicsOverlay);
    }

    /**
     * Draw trees with clustering based on zoom level
     */
    private void drawTreesClustered(EdibleTrees edibleTrees) {
        graphicsOverlay.getGraphics().clear();

        double scale = mapView.getMapScale();
        List<EdibleTree> trees = edibleTrees.getTrees();

        // Determine clustering distance based on scale
        double clusterDistance = getClusterDistance(scale);

        if (clusterDistance > 0) {
            // Cluster the trees
            List<TreeCluster> clusters = clusterTrees(trees, clusterDistance);

            // Draw clusters
            for (TreeCluster cluster : clusters) {
                if (cluster.getTreeCount() == 1) {
                    // Single tree - draw as individual marker
                    drawIndividualTree(cluster.getCenterPoint());
                } else {
                    // Multiple trees - draw as cluster
                    drawCluster(cluster.getCenterPoint(), cluster.getTreeCount());
                }
            }
        } else {
            // Very zoomed in - show all individual trees
            for (EdibleTree tree : trees) {
                drawIndividualTree(tree.getPlantLocation().getPoint());
            }
        }

        System.out.println("Drew trees at scale: " + scale + " with cluster distance: " + clusterDistance);
    }

    /**
     * Get clustering distance based on map scale
     */
    private double getClusterDistance(double scale) {
        if (scale > 500000) {
            return 0.15; // Very zoomed out - ~15km
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
     * Grid-based clustering algorithm
     */
    private List<TreeCluster> clusterTrees(List<EdibleTree> trees, double clusterDistance) {
        if (clusterDistance == 0) {
            // No clustering - return individual trees
            List<TreeCluster> clusters = new ArrayList<>();
            for (EdibleTree tree : trees) {
                TreeCluster cluster = new TreeCluster();
                cluster.addTree(tree);
                clusters.add(cluster);
            }
            return clusters;
        }

        // Use a grid to group trees
        Map<String, TreeCluster> gridClusters = new HashMap<>();

        for (EdibleTree tree : trees) {
            double lat = tree.getPlantLocation().getLatitude().doubleValue();
            double lon = tree.getPlantLocation().getLongitude().doubleValue();

            // Calculate grid cell
            int gridLat = (int) Math.floor(lat / clusterDistance);
            int gridLon = (int) Math.floor(lon / clusterDistance);
            String gridKey = gridLat + "," + gridLon;

            // Add to grid cluster
            TreeCluster cluster = gridClusters.get(gridKey);
            if (cluster == null) {
                cluster = new TreeCluster();
                gridClusters.put(gridKey, cluster);
            }
            cluster.addTree(tree);
        }

        return new ArrayList<>(gridClusters.values());
    }

    /**
     * Draw an individual tree marker
     */
    private void drawIndividualTree(Point point) {
        SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(
                SimpleMarkerSymbol.Style.CIRCLE,
                Color.rgb(34, 139, 34), // Forest Green
                8
        );
        Graphic graphic = new Graphic(point, symbol);
        graphicsOverlay.getGraphics().add(graphic);
    }

    /**
     * Draw a cluster marker with count
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

        // Create text label for count
        TextSymbol textSymbol = new TextSymbol(
                14,                                 // size
                String.valueOf(count),                   // text
                Color.WHITE,                             // color
                TextSymbol.HorizontalAlignment.CENTER,
                TextSymbol.VerticalAlignment.MIDDLE
        );
        textSymbol.setHaloColor(Color.BLACK); // Black halo
        textSymbol.setHaloWidth(1);

        // Combine circle and text
        List<com.esri.arcgisruntime.symbology.Symbol> symbols = new ArrayList<>();
        symbols.add(circleSymbol);
        symbols.add(textSymbol);
        CompositeSymbol compositeSymbol = new CompositeSymbol(symbols);

        Graphic graphic = new Graphic(point, compositeSymbol);
        graphicsOverlay.getGraphics().add(graphic);
    }

    /**
     * Update clusters when viewport changes
     */
    private void updateClusters() {
        drawTreesClustered(edibleTrees);
    }

    /**
     * Clean up resources when application is closed
     */
    @Override
    public void stop() {
        if (mapView != null) {
            mapView.dispose();
        }
    }

    /**
     * Helper class to represent a cluster of trees
     */
    private static class TreeCluster {
        private List<EdibleTree> trees = new ArrayList<>();

        public void addTree(EdibleTree tree) {
            trees.add(tree);
        }

        public int getTreeCount() {
            return trees.size();
        }

        public Point getCenterPoint() {
            if (trees.isEmpty()) return null;

            // Calculate average position
            double avgLat = trees.stream()
                    .mapToDouble(t -> t.getPlantLocation().getLatitude().doubleValue())
                    .average()
                    .orElse(0);
            double avgLon = trees.stream()
                    .mapToDouble(t -> t.getPlantLocation().getLongitude().doubleValue())
                    .average()
                    .orElse(0);

            return new Point(avgLon, avgLat, SpatialReferences.getWgs84());
        }
    }
}