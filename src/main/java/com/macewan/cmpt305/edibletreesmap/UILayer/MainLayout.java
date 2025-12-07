package com.macewan.cmpt305.edibletreesmap.UILayer;

import com.esri.arcgisruntime.mapping.view.MapView;
import javafx.scene.layout.*;

public class MainLayout extends StackPane {
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
        HBox mainWindow = new HBox();

        mainWindow.getChildren().addAll(mapView, sidePanel);
        HBox.setHgrow(mapView, Priority.ALWAYS);
        HBox.setHgrow(sidePanel, Priority.ALWAYS);
        this.getChildren().add(mainWindow);

    }



}
