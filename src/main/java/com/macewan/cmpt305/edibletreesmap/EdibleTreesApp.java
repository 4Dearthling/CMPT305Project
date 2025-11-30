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
import javafx.scene.control.CheckBox;


import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Box;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.control.ScrollPane;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;


import java.io.IOException;
import java.util.List;




/**
 * Displays an interactive map of Edmonton
 */
public class EdibleTreesApp extends Application {
    private CheckBox appleCheckBox;
    private CheckBox cherryCheckBox;
    private CheckBox crabappleCheckBox;
    private CheckBox plumCheckBox;
    private CheckBox pearCheckBox;
    private CheckBox chokeCherryCheckBox;
    private CheckBox acornCheckBox;
    private CheckBox hawthornCheckBox;
    private CheckBox juniperCheckBox;

    private final java.util.Map<String, java.util.List<Graphic>> fruitGraphics = new java.util.HashMap<>();


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

        // filter section
        VBox filterBox = createFilterSection();
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
    private VBox createFilterSection() {

        //label for the filtering on side panel
        Label label = new Label("Filter by Tree Type");
        label.setFont(Font.font("System", FontWeight.BOLD, 16));

        // use fields so other methods can see them
        appleCheckBox = new CheckBox("Apple");
        cherryCheckBox = new CheckBox("Cherry");
        crabappleCheckBox = new CheckBox("Crabapple");
        plumCheckBox = new CheckBox("Plum");
        pearCheckBox = new CheckBox("Pear");
        chokeCherryCheckBox = new CheckBox("Chokecherry");
        acornCheckBox = new CheckBox("Acorn");
        hawthornCheckBox = new CheckBox("Hawthorn");
        juniperCheckBox = new CheckBox("Juniper");


        appleCheckBox.setSelected(true);
        cherryCheckBox.setSelected(true);
        crabappleCheckBox.setSelected(true);
        plumCheckBox.setSelected(true);
        pearCheckBox.setSelected(true);
        chokeCherryCheckBox.setSelected(true);
        acornCheckBox.setSelected(true);
        hawthornCheckBox.setSelected(true);
        juniperCheckBox.setSelected(true);


        // whenever any checkbox turns on  or off, update if it shows or not
        appleCheckBox.selectedProperty().addListener((obs, o, n) -> updateFruitVisibility());
        cherryCheckBox.selectedProperty().addListener((obs, o, n) -> updateFruitVisibility());
        crabappleCheckBox.selectedProperty().addListener((obs, o, n) -> updateFruitVisibility());
        plumCheckBox.selectedProperty().addListener((obs, o, n) -> updateFruitVisibility());
        pearCheckBox.selectedProperty().addListener((obs, o, n) -> updateFruitVisibility());
        chokeCherryCheckBox.selectedProperty().addListener((obs, o, n) -> updateFruitVisibility());
        acornCheckBox.selectedProperty().addListener((obs, o, n) -> updateFruitVisibility());
        hawthornCheckBox.selectedProperty().addListener((obs, o, n) -> updateFruitVisibility());
        juniperCheckBox.selectedProperty().addListener((obs, o, n) -> updateFruitVisibility());

        return new VBox(
                10,
                label,
                appleCheckBox,
                cherryCheckBox,
                crabappleCheckBox,
                plumCheckBox,
                pearCheckBox,
                chokeCherryCheckBox,
                acornCheckBox,
                hawthornCheckBox,
                juniperCheckBox
        );
    }
    private void updateFruitVisibility() {
        if (fruitGraphics.isEmpty()) {
            return;
        }

        for (var entry : fruitGraphics.entrySet()) {
            String key = entry.getKey();
            boolean visible = isFruitEnabled(key);

            for (Graphic g : entry.getValue()) {
                g.setVisible(visible);
            }
        }
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


    private Color getColorForFruit(String key) {
        // key is lowercase
        switch (key) {
            case "apple":
                return Color.RED;
            case "cherry":
                return Color.HOTPINK;
            case "crabapple":
                return Color.ORANGE;
            case "plum":
                return Color.PLUM;
            case "pear":
                return Color.LIMEGREEN;
            case "chokecherry":
                return Color.PURPLE;
            case "acorn":
                return Color.BROWN;
            case "hawthorn":
                return Color.LIGHTSKYBLUE;
            case "juniper":
                return Color.LIGHTYELLOW;
            default:
                return Color.GREEN;
        }
    }


    private boolean isFruitEnabled(String key) {
        key = key.toLowerCase();
        return switch (key) {
            case "apple"      -> appleCheckBox == null || appleCheckBox.isSelected();
            case "cherry"     -> cherryCheckBox == null || cherryCheckBox.isSelected();
            case "crabapple"  -> crabappleCheckBox == null || crabappleCheckBox.isSelected();
            case "plum"       -> plumCheckBox == null || plumCheckBox.isSelected();
            case "pear"       -> pearCheckBox == null || pearCheckBox.isSelected();
            case "chokecherry"-> chokeCherryCheckBox == null || chokeCherryCheckBox.isSelected();
            case "acorn"      -> acornCheckBox == null || acornCheckBox.isSelected();
            case "hawthorn"   -> hawthornCheckBox == null || hawthornCheckBox.isSelected();
            case "juniper"    -> juniperCheckBox == null || juniperCheckBox.isSelected();
            default           -> true; // any other fruit type stays visible
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
        updateFruitVisibility();
    }

}