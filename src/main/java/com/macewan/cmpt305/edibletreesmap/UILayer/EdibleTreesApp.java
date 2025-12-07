package com.macewan.cmpt305.edibletreesmap.UILayer;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.BasemapStyle;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.macewan.cmpt305.edibletreesmap.DataObjectsLayer.EdibleTrees;
import com.macewan.cmpt305.edibletreesmap.ServiceLayer.TreeMapService;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main application class for the Edmonton Edible Trees interactive map.
 */
public class EdibleTreesApp extends Application {

    private final Map<String, List<Graphic>> fruitGraphics = new HashMap<>();

    // Map components
    private MapView mapView;
    private GraphicsOverlay graphicsOverlay;
    private TreeMapService treeMapService;
    private MapInteractionHandler mapInteractionHandler;
    private SidePanel sidePanel;

    // Data
    private static EdibleTrees edibleTrees;

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


    @Override
    public void start(Stage stage) {


        // Set up the window
        stage.setTitle("Edmonton Edible Trees");
        stage.setWidth(1200);
        stage.setHeight(800);

        // Initialize the map
        initializeMap();

        // Create Graphics Manager
        TreeGraphicsManager graphicsManager = new TreeGraphicsManager(graphicsOverlay);

        // Initialize renderer (needs graphics overlay)
        MapRenderer mapRenderer = new MapRenderer(graphicsManager, graphicsOverlay);
        treeMapService = new TreeMapService(mapView, mapRenderer);

        // Build UI
        sidePanel = new SidePanel(treeMapService::refreshClusters, graphicsManager);
        MainLayout layout = new MainLayout(mapView, sidePanel);


        // Set up and show the scene
        Scene scene = new Scene(layout);
        stage.setScene(scene);
        stage.show();
      
        // Load tree data in background
        loadTreeDataAsync();
    
    }

//    private VBox createSidePanel() {
//        VBox sidePane = getVBox();
//
//        // Title
//        Label title = new Label("Edmonton Edible Trees");
//        title.setFont(Font.font("System", FontWeight.BOLD, 18));
//
//        // filter section (moved into TreeFilterPanel)
//        TreeFilterPanel filterBox = new TreeFilterPanel(fruitGraphics);
//
//        // Add refresh button
//        Button refreshButton = new Button("Refresh Clusters");
//        refreshButton.setStyle("-fx-font-size: 14px; -fx-padding: 10;");
//        refreshButton.setMaxWidth(Double.MAX_VALUE);
//        refreshButton.setOnAction(e -> refreshClusters());
//
//        sidePane.getChildren().addAll(title, filterBox, refreshButton);
//
//        return sidePane;
//    }
//
//    /**
//     * Setting a sidebar for the filtering
//     */
//    private static VBox getVBox() {
//
//        VBox container = new VBox(20); //creating side panel box
//        container.setPadding(new Insets(20));
//        container.setPrefWidth(350);
//
//        // setting colour to white
//        container.setBackground(
//                new Background(new BackgroundFill(Color.WHITE, new CornerRadii(15), Insets.EMPTY))
//        );
//
//        container.setBorder(
//                new Border(new BorderStroke(Color.LIGHTGRAY,
//                        BorderStrokeStyle.SOLID,
//                        new CornerRadii(15),
//                        new BorderWidths(1)))
//        );
//
//        container.setEffect(new DropShadow(10, Color.gray(0, 0.2)));
//
//        return container;
//    }


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

        graphicsOverlay = new GraphicsOverlay();
        mapView.getGraphicsOverlays().add(graphicsOverlay);
    }

    /**
     * Loads tree data asynchronously in a background thread
     */
    private void loadTreeDataAsync() {
        new Thread(() -> {
            try {
                treeMapService.loadData();
                System.out.println("Loading tree data...");
                System.out.println("Data loaded successfully: " + treeMapService.getEdibleTrees().getTrees().size() + " trees");

                // Update map on JavaFX thread
                javafx.application.Platform.runLater(() -> {
                    if (edibleTrees != null) {
                        mapInteractionHandler = new MapInteractionHandler(mapView, treeMapService.getEdibleTrees());
                        treeMapService.setMapInteractionHandler(mapInteractionHandler);
                        treeMapService.refreshClusters();

                        // Refreshes filters after trees are rendered
                        sidePanel.refreshFilters();
                    }
                });
            } catch (IOException e) {
                System.err.println("Error loading data: " + e.getMessage());
            }
        }).start();
    }


     

    /**
     * Clean up resources when application closes
     */
    @Override
    public void stop() {
        if (mapView != null) {
            mapView.dispose();
            graphicsOverlay.clearSelection();
            fruitGraphics.clear();
        }
    }
}
