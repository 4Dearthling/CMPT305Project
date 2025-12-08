package com.macewan.cmpt305.edibletreesmap.UILayer;

import com.esri.arcgisruntime.geometry.Point;
import com.macewan.cmpt305.edibletreesmap.DataObjectsLayer.EdibleTree;
import com.macewan.cmpt305.edibletreesmap.DataObjectsLayer.EdibleTrees;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TreePieChart extends VBox {
    private final PieChart pieChart;
    private final Label titleLabel;
    private final Label infoLabel;
    private EdibleTrees edibleTrees;
    private Set<String> enabledFruitTypes = new HashSet<>();
    private Point lastCenter;
    private VBox legend;
    private double lastRadius;

    public TreePieChart() {
        super(10);

        titleLabel = new Label("Tree Distribution");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        titleLabel.setTextFill(Color.BLACK);

        infoLabel = new Label("Click on the map to see tree distribution");
        infoLabel.setWrapText(true);
        infoLabel.setTextFill(Color.BLACK);

        pieChart = new PieChart();
        pieChart.setLegendSide(Side.BOTTOM);
        pieChart.setLabelsVisible(false);

        pieChart.setMaxWidth(300);

        pieChart.setLegendVisible(true);





        getChildren().addAll(titleLabel, infoLabel, pieChart);
        setPadding(new Insets(10, 0, 0, 0));

        // Initially hide the pie chart until data is available
        pieChart.setVisible(false);


    }

    public void setEdibleTrees(EdibleTrees edibleTrees) {
        this.edibleTrees = edibleTrees;
    }

    public void setEnabledFruitTypes(Set<String> enabledFruitTypes) {
        this.enabledFruitTypes = enabledFruitTypes;
        // Refresh the chart if we already have a center point
        if (lastCenter != null) {
            updateChart(lastCenter, lastRadius);
        }
    }

    public void updateChart(Point center, double radiusInMetres) {
        if (edibleTrees == null || center == null) {
            return;
        }

        // Store for later refresh when filters change
        lastCenter = center;
        lastRadius = radiusInMetres;

        // Get trees within the radius
        List<EdibleTree> treesInRadius = edibleTrees.getTreeByRadius(center, radiusInMetres);

        // Filter by enabled fruit types
        List<EdibleTree> filteredTrees = treesInRadius.stream()
                .filter(tree -> {
                    String fruitType = tree.getPlantBiology().getTypeFruit();
                    if (fruitType == null || fruitType.isBlank()) {
                        return false;
                    }
                    // Check if the fruit type is enabled (case-insensitive)
                    return enabledFruitTypes.stream()
                            .anyMatch(enabled -> enabled.equalsIgnoreCase(fruitType));
                })
                .toList();

        if (filteredTrees.isEmpty()) {
            infoLabel.setText("No trees found in this area");
            pieChart.setVisible(false);
            return;
        }

        // Count trees by fruit type
        Map<String, Integer> fruitCounts = new HashMap<>();
        for (EdibleTree tree : filteredTrees) {
            String fruitType = tree.getPlantBiology().getTypeFruit();
            if (fruitType == null || fruitType.isBlank()) {
                fruitType = "Unknown";
            }
            fruitCounts.merge(fruitType, 1, Integer::sum);
        }

        // Create pie chart data
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        for (Map.Entry<String, Integer> entry : fruitCounts.entrySet()) {
            pieData.add(new PieChart.Data(entry.getKey() + " (" + entry.getValue() + ")", entry.getValue()));
        }

        pieChart.setData(pieData);
        pieChart.setVisible(true);

        double radiusKm = radiusInMetres / 1000.0;
        infoLabel.setText(String.format("%d trees within %.1f km", filteredTrees.size(), radiusKm));

        //buildLegend();

        // Apply colors to match fruit colors
        applyFruitColors();
    }

    private void applyFruitColors() {
        int index = 0;
        for (PieChart.Data data : pieChart.getData()) {
            String name = data.getName();
            // Extract fruit type from "FruitType (count)" format
            String fruitType = name.substring(0, name.lastIndexOf(" ("));
            Color color = FruitColorMapper.getColor(fruitType);

            // Convert JavaFX color to CSS
            String colorStyle = String.format("-fx-pie-color: rgb(%d, %d, %d);",
                    (int) (color.getRed() * 255),
                    (int) (color.getGreen() * 255),
                    (int) (color.getBlue() * 255));

            // Style the pie slice
            data.getNode().setStyle(colorStyle);

            // Style the corresponding legend item
            final int legendIndex = index;
            final String legendColorStyle = colorStyle;
            pieChart.lookupAll(".chart-legend-item-symbol").stream()
                    .skip(legendIndex)
                    .findFirst()
                    .ifPresent(node -> node.setStyle(legendColorStyle));

            index++;
        }
    }

    private void buildLegend() {

        for (PieChart.Data data : pieChart.getData()) {
            String fruit = data.getName().substring(0, data.getName().lastIndexOf(" ("));

            // get the color the slice is using
            Color color = FruitColorMapper.getColor(data.getName());

            // make color swatch
            Rectangle swatch = new Rectangle(10, 10);
            swatch.setFill(color);

            // label the swatch
            Label label = new Label(data.getName());

            // put them together
            HBox legendItem = new HBox(5, swatch, label);

            legendItem.setAlignment(Pos.CENTER_LEFT);
            legend.getChildren().add(legendItem);
        }
    }

    public void clearChart() {
        pieChart.getData().clear();
        pieChart.setVisible(false);
        infoLabel.setText("Click on the map to see tree distribution");
    }
}
