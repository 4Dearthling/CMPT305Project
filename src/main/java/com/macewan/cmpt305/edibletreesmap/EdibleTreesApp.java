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
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Displays an interactive map of Edmonton
 */
public class EdibleTreesApp extends Application {

    private final Map<String, List<Graphic>> fruitGraphics = new HashMap<>();

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
        loadData();
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
        // Title
        Label title = new Label("Edmonton Edible Trees");
        title.setFont(Font.font("System", FontWeight.BOLD, 18));

        // filter section (moved into TreeFilterPanel)
        TreeFilterPanel filterBox = new TreeFilterPanel(fruitGraphics);
        sidePane.getChildren().addAll(title, filterBox);

        //side by side
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

    /**
     * Setting a sidebar for the filtering
     */
    private static VBox getVBox() {

        VBox container = new VBox(20); //creating side panel box
        container.setPadding(new Insets(20));
        container.setPrefWidth(350);

        // setting colour to white
        container.setBackground(
                new Background(new BackgroundFill(Color.WHITE, new CornerRadii(15), Insets.EMPTY))
        );

        container.setBorder(
                new Border(new BorderStroke(Color.LIGHTGRAY,
                        BorderStrokeStyle.SOLID,
                        new CornerRadii(15),
                        new BorderWidths(1)))
        );

        container.setEffect(new DropShadow(10, Color.gray(0, 0.2)));

        return container;
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

        graphicsOverlay = new GraphicsOverlay();
        mapView.getGraphicsOverlays().add(graphicsOverlay);
        drawTree(edibleTrees);

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
     * Sets colours for each tree for distinction
     */
    private Color getColorForFruit(String key) {
        // key is lowercase
        return switch (key) {
            case "apple" -> Color.RED;
            case "cherry" -> Color.HOTPINK;
            case "crabapple" -> Color.ORANGE;
            case "plum" -> Color.PLUM;
            case "pear" -> Color.LIMEGREEN;
            case "chokecherry" -> Color.PURPLE;
            case "acorn" -> Color.BROWN;
            case "hawthorn" -> Color.LIGHTSKYBLUE;
            case "juniper" -> Color.GREENYELLOW;
            case "butternut" -> Color.GOLD;
            case "saskatoon" -> Color.SALMON;
            case "russian olive" -> Color.LAVENDER;
            case "coffeetree pod" -> Color.GRAY;
            case "walnut" -> Color.BLACK;
            case "hackberry" -> Color.NAVY;
            case "caragana flower/pod" -> Color.DARKGREEN;
            default -> Color.GREEN;
        };
    }

    private void drawTree(EdibleTrees edibleTrees) {
        for (EdibleTree tree : edibleTrees.getTrees()) {
            String fruitType = tree.getPlantBiology().getTypeFruit();
            if (fruitType == null) {
                fruitType = "";
            }
            String key = fruitType.trim().toLowerCase();

            // picking a colour based on fruit type
            Color color = getColorForFruit(key);
            SimpleMarkerSymbol symbol =
                    new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, color, 10);

            Graphic graphic = new Graphic(tree.getPlantLocation().getPoint(), symbol);

            // add to overlay
            graphicsOverlay.getGraphics().add(graphic);

            //remember which graphics belong to which fruit
            fruitGraphics
                    .computeIfAbsent(key, k -> new java.util.ArrayList<>())
                    .add(graphic);
        }
    }

}
