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
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.text.Font;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Box;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.List;

/**
 * Displays an interactive map of Edmonton
 */
public class EdibleTreesApp extends Application {

    private MapView mapView;
    private GraphicsOverlay graphicsOverlay;
    private static EdibleTrees edibleTrees;

    // Edmonton coordinates (City Centre)
    private static final double EDMONTON_LATITUDE = 53.5461;
    private static final double EDMONTON_LONGITUDE = -113.4938;
    private static final double INITIAL_SCALE = 100000; // Scale for viewing Edmonton

    /**
     * Main method to launch the application
     */
    public static void main(String[] args) throws IOException, InterruptedException {

        //loadData();
        Application.launch(args);
    }

    public static void loadData() throws IOException {
        try{
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

        // Create Label
        final Label label = new Label("Edmonton Edible Trees");
        label.setStyle("-fx-font-size: 18px;");
        label.setFont(Font.font("System",18));
        label.setStyle("-fx-font-weight: 800;");


        // Add control panel
        VBox sidePane = getVBox();
        sidePane.getChildren().add(title());

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


    }
    private static Label title(){
        // Create Label
        final Label label = new Label("Edmonton Edible Trees");
        label.setStyle("-fx-font-size: 18px;");
        label.setFont(Font.font("System",18));
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
        //drawTree(edibleTrees);



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

    private void drawTree(EdibleTrees edibleTrees) {
        SimpleMarkerSymbol simpleMarkerSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.GREEN, 10);
        List<EdibleTree> trees = edibleTrees.getTrees();
        for (EdibleTree tree : trees) {
            Graphic graphic = new Graphic(tree.getPlantLocation().getPoint(), simpleMarkerSymbol);
            graphicsOverlay.getGraphics().add(graphic);
        }


    }


}