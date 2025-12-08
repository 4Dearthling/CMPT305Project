package com.macewan.cmpt305.edibletreesmap.UILayer;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


public class RadiusSlider extends VBox {
    private final Slider slider;
    public RadiusSlider() {
        super(20);
        slider = new Slider(0, 10, 1); // 0-10 km, start at 1 km
        buildUI();
        styleSlider();
    }

    private void buildUI(){
        Label label = new Label("Radius (km)");
        label.setFont(Font.font("System", FontWeight.BOLD, 16));
        label.setTextFill(Color.BLACK);

        VBox labeledSlider = new VBox(label, slider);
        getChildren().add(labeledSlider);
    }

    private void styleSlider(){
        slider.setMaxWidth(240.0);
        slider.setMajorTickUnit(2);
        slider.setMinorTickCount(1);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setDisable(true); // Disabled until user clicks on map
        slider.setStyle("-fx-font-weight: bold; -fx-font-size: 15;");
        slider.setPadding(new Insets(10, 10, 0, 10));
    }

    public void setEnabled(boolean enabled) {
        slider.setDisable(!enabled);
        System.out.println("Slider enabled: " + enabled + ", isDisabled: " + slider.isDisabled());
    }

    /**
     * Returns the radius in metres (converts from km)
     */
    public double getRadius(){
        return slider.getValue() * 1000; // Convert km to metres
    }


    public Slider getSlider(){
        return slider;
    }
}
