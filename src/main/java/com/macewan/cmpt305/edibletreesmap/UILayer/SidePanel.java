package com.macewan.cmpt305.edibletreesmap.UILayer;

import javafx.geometry.Insets;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.control.Button;

import java.util.Set;
import java.util.function.Consumer;

public class SidePanel extends VBox{
    private Button refreshButton;
    private TreeFilterPanel treeFilterPanel;

    public SidePanel(Consumer<Set<String>> refreshCluster, TreeGraphicsManager treeGraphicsManager, RadiusSlider radiusSlider, TreePieChart pieChart) {
        /*
        * Formats the side panel elements
        * */
        super(20);
        applyStyles();
        buildUI(pieChart, radiusSlider, treeGraphicsManager,refreshCluster );
    }

    private void buildUI(TreePieChart pieChart, RadiusSlider radiusSlider, TreeGraphicsManager treeGraphicsManager, Consumer<Set<String>> refreshCluster) {
        // Title
        javafx.scene.control.Label title = new javafx.scene.control.Label("Edmonton Edible Trees");
        title.setFont(Font.font("System", FontWeight.BOLD, 18));
        title.setTextFill(Color.BLACK);

        // filter section (moved into TreeFilterPanel)
        treeFilterPanel = new TreeFilterPanel(treeGraphicsManager);

        // Update pie chart when filter checkboxes change
        treeFilterPanel.setOnFilterChange(pieChart::setEnabledFruitTypes);

        // Initialize pie chart with current filter state
        pieChart.setEnabledFruitTypes(treeFilterPanel.getEnabledFruitTypes());

        // Add refresh button
        refreshButton = new Button("Refresh Clusters");
        refreshButton.setStyle("-fx-font-size: 14px; -fx-padding: 10;");
        refreshButton.setMaxWidth(Double.MAX_VALUE);
        refreshButton.setOnAction(e -> refreshCluster.accept(treeFilterPanel.getEnabledFruitTypes()));

        getChildren().addAll(title, pieChart, radiusSlider, treeFilterPanel, refreshButton );
        VBox.setVgrow(pieChart, Priority.ALWAYS);

    }

    private void applyStyles() {

        setPadding(new Insets(20));
        setPrefWidth(350);

        // setting colour to white
        setBackground(
                new Background(new BackgroundFill(Color.WHITE, new CornerRadii(15), Insets.EMPTY))
        );

        setBorder(
                new Border(new BorderStroke(Color.LIGHTGRAY,
                        BorderStrokeStyle.SOLID,
                        new CornerRadii(15),
                        new BorderWidths(1)))
        );

        setEffect(new DropShadow(10, Color.gray(0, 0.2)));

    }
    public void refreshFilters(){
        if (treeFilterPanel != null) {
            treeFilterPanel.refreshVisibility();
        }
    }

}
