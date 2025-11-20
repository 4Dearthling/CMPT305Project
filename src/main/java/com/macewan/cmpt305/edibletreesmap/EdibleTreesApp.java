package com.macewan.cmpt305.edibletreesmap;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.BasemapStyle;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.MapView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Displays an interactive map of Edmonton
 */
public class EdibleTreesApp extends Application {

    private MapView mapView;

    // Edmonton coordinates (City Centre)
    private static final double EDMONTON_LATITUDE = 53.5461;
    private static final double EDMONTON_LONGITUDE = -113.4938;
    private static final double INITIAL_SCALE = 100000; // Scale for viewing Edmonton

    /**
     * Main method to launch the application
     */
    public static void main(String[] args) {

        EdibleTreeDAO dao = new ApiCsvEtDAO("https://data.edmonton.ca/api/v3/views/eecg-fc54/query.csv");

        dao.getById(385224);
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) {
        // Set the title and size of the stage (window)
        stage.setTitle("Edmonton Edible Trees");
        stage.setWidth(1200);
        stage.setHeight(800);

        // Create a StackPane as the root layout
        StackPane stackPane = new StackPane();

        // Create the scene
        Scene scene = new Scene(stackPane);

        // Set the scene to the stage
        stage.setScene(scene);

        // Show the stage
        stage.show();

        // Initialize the map view
        initializeMap(stackPane);
    }

    /**
     * Initialize the map view with Edmonton centered
     */
    private void initializeMap(StackPane stackPane) {
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

        // Add the map view to the stack pane
        stackPane.getChildren().add(mapView);
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


}