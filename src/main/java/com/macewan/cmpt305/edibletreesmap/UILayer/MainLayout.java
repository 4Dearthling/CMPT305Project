package com.macewan.cmpt305.edibletreesmap.UILayer;

import com.esri.arcgisruntime.mapping.view.MapView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

public class MainLayout extends HBox {
    /*
    * Formats the entire application so map is on the left and side panel is on the right
    * */
    private final MapView mapView;
    private final SidePanel sidePanel;

    public MainLayout(MapView mapView, SidePanel sidePanel) {
        this.mapView = mapView;
        this.sidePanel = sidePanel;
        buildLayout();
    }


    private void buildLayout() {
        // Wrap side panel in a ScrollPane to make it scrollable
        ScrollPane scrollPane = new ScrollPane(sidePanel);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        // Remove default ScrollPane background styling
        scrollPane.setStyle("-fx-background: white; -fx-background-color: white;");
        scrollPane.setPrefWidth(370);

        getChildren().addAll(mapView, scrollPane);
        HBox.setHgrow(mapView, Priority.ALWAYS);
    }



}
