package com.macewan.cmpt305.edibletreesmap.UILayer;

import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class TreeFilterPanel extends VBox {
    /*
    * The checkout boxes in the side panel is dealt with here
    * */

    private CheckBox appleCheckBox;
    private CheckBox cherryCheckBox;
    private CheckBox crabappleCheckBox;
    private CheckBox plumCheckBox;
    private CheckBox pearCheckBox;
    private CheckBox chokeCherryCheckBox;
    private CheckBox acornCheckBox;
    private CheckBox hawthornCheckBox;
    private CheckBox juniperCheckBox;
    private CheckBox butternutCheckBox;
    private CheckBox saskatoonCheckbox;
    private CheckBox russianOliveCheckbox;
    private CheckBox coffeeTreeCheckBox;
    private CheckBox walnutCheckBox;
    private CheckBox hackberryCheckBox;
    private CheckBox caraganaCheckBox;

    private Label selectAllLabel;
    private Label selectNoneLabel;

    // reference to fruitGraphics from the app
    //private final Map<String, List<Graphic>> fruitGraphics;
    private final TreeGraphicsManager treeGraphicsManager;

    public TreeFilterPanel(TreeGraphicsManager treeGraphicsManager) {
        super(10);
        this.treeGraphicsManager = treeGraphicsManager;
        setPadding(new Insets(0));
        buildUI();
        updateBulkSelectState();
    }

    private void updateFruitVisibility(){
        for (String fruit: treeGraphicsManager.getFruitTypes()){
            treeGraphicsManager.setFruitVisibility(fruit, isFruitEnabled(fruit));
        }
    }


    private void buildUI() {
        // label for the filtering on side panel
        Label label = new Label("Filter by Tree Type");
        label.setFont(Font.font("System", FontWeight.BOLD, 16));

        // clickable bulk actions
        selectAllLabel = new Label("Select All");
        selectNoneLabel = new Label("Select None");

        // if select all is clicked
        selectAllLabel.setOnMouseClicked(e -> {
            if (!selectAllLabel.isDisabled()) {
                setAllCheckBoxes(true); // check all boxes
                updateFruitVisibility();
                updateBulkSelectState();
            }
        });

        //if select none label is clicked
        selectNoneLabel.setOnMouseClicked(e -> {
            if (!selectNoneLabel.isDisabled()) {
                setAllCheckBoxes(false); // uncheck all boxes
                updateFruitVisibility();
                updateBulkSelectState();
            }
        });

        // r
        HBox headerRow = new HBox(10, label, selectAllLabel, selectNoneLabel);

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
        butternutCheckBox = new CheckBox("Butternut");
        saskatoonCheckbox = new  CheckBox("Saskatoon");
        russianOliveCheckbox = new CheckBox("Russian Olive");
        coffeeTreeCheckBox = new CheckBox("Coffee Tree");
        walnutCheckBox = new CheckBox("Walnut");
        hackberryCheckBox = new CheckBox("Hackberry");
        caraganaCheckBox =  new CheckBox("Caragana Flower/Pod");

        appleCheckBox.setSelected(true);
        cherryCheckBox.setSelected(true);
        crabappleCheckBox.setSelected(true);
        plumCheckBox.setSelected(true);
        pearCheckBox.setSelected(true);
        chokeCherryCheckBox.setSelected(true);
        acornCheckBox.setSelected(true);
        hawthornCheckBox.setSelected(true);
        juniperCheckBox.setSelected(true);
        butternutCheckBox.setSelected(true);
        saskatoonCheckbox.setSelected(true);
        russianOliveCheckbox.setSelected(true);
        coffeeTreeCheckBox.setSelected(true);
        walnutCheckBox.setSelected(true);
        hackberryCheckBox.setSelected(true);
        caraganaCheckBox.setSelected(true);

        // whenever any checkbox turns on or off, update visibility and header state
        addListener(appleCheckBox);
        addListener(cherryCheckBox);
        addListener(crabappleCheckBox);
        addListener(plumCheckBox);
        addListener(pearCheckBox);
        addListener(chokeCherryCheckBox);
        addListener(acornCheckBox);
        addListener(hawthornCheckBox);
        addListener(juniperCheckBox);
        addListener(butternutCheckBox);
        addListener(saskatoonCheckbox);
        addListener(russianOliveCheckbox);
        addListener(coffeeTreeCheckBox);
        addListener(walnutCheckBox);
        addListener(hackberryCheckBox);
        addListener(caraganaCheckBox);

        VBox box = new VBox(
                10,
                headerRow,
                appleCheckBox,
                cherryCheckBox,
                crabappleCheckBox,
                plumCheckBox,
                pearCheckBox,
                chokeCherryCheckBox,
                acornCheckBox,
                hawthornCheckBox,
                juniperCheckBox,
                butternutCheckBox,
                saskatoonCheckbox,
                russianOliveCheckbox,
                coffeeTreeCheckBox,
                walnutCheckBox,
                hackberryCheckBox,
                caraganaCheckBox
        );

        // initialize the Select all or Select none method
        updateBulkSelectState();

        getChildren().add(box);
    }

    private void addListener(CheckBox cb) {
        cb.selectedProperty().addListener((obs, o, n) -> {
            updateFruitVisibility();
            updateBulkSelectState();
        });
    }

    private void setAllCheckBoxes(boolean value) {
        selected(value, appleCheckBox, cherryCheckBox, crabappleCheckBox, plumCheckBox, pearCheckBox, chokeCherryCheckBox, acornCheckBox, hawthornCheckBox);
        selected(value, juniperCheckBox, butternutCheckBox, saskatoonCheckbox, russianOliveCheckbox, coffeeTreeCheckBox, walnutCheckBox, hackberryCheckBox, caraganaCheckBox);
    }

    private void selected(boolean value, CheckBox appleCheckBox, CheckBox cherryCheckBox, CheckBox crabappleCheckBox, CheckBox plumCheckBox, CheckBox pearCheckBox, CheckBox chokeCherryCheckBox, CheckBox acornCheckBox, CheckBox hawthornCheckBox) {
        if (appleCheckBox != null)       appleCheckBox.setSelected(value);
        if (cherryCheckBox != null)      cherryCheckBox.setSelected(value);
        if (crabappleCheckBox != null)   crabappleCheckBox.setSelected(value);
        if (plumCheckBox != null)        plumCheckBox.setSelected(value);
        if (pearCheckBox != null)        pearCheckBox.setSelected(value);
        if (chokeCherryCheckBox != null) chokeCherryCheckBox.setSelected(value);
        if (acornCheckBox != null)       acornCheckBox.setSelected(value);
        if (hawthornCheckBox != null)    hawthornCheckBox.setSelected(value);
    }

    private void updateBulkSelectState() {
        if (appleCheckBox == null) return; // not initialized yet

        boolean allSelected =
                appleCheckBox.isSelected() &&
                        cherryCheckBox.isSelected() &&
                        crabappleCheckBox.isSelected() &&
                        plumCheckBox.isSelected() &&
                        pearCheckBox.isSelected() &&
                        chokeCherryCheckBox.isSelected() &&
                        acornCheckBox.isSelected() &&
                        hawthornCheckBox.isSelected() &&
                        juniperCheckBox.isSelected() &&
                        butternutCheckBox.isSelected() &&
                        saskatoonCheckbox.isSelected() &&
                        russianOliveCheckbox.isSelected() &&
                        coffeeTreeCheckBox.isSelected() &&
                        walnutCheckBox.isSelected() &&
                        hackberryCheckBox.isSelected() &&
                        caraganaCheckBox.isSelected();

        boolean noneSelected =
                !appleCheckBox.isSelected() &&
                        !cherryCheckBox.isSelected() &&
                        !crabappleCheckBox.isSelected() &&
                        !plumCheckBox.isSelected() &&
                        !pearCheckBox.isSelected() &&
                        !chokeCherryCheckBox.isSelected() &&
                        !acornCheckBox.isSelected() &&
                        !hawthornCheckBox.isSelected() &&
                        !juniperCheckBox.isSelected() &&
                        !butternutCheckBox.isSelected() &&
                        !saskatoonCheckbox.isSelected() &&
                        !russianOliveCheckbox.isSelected() &&
                        !coffeeTreeCheckBox.isSelected() &&
                        !walnutCheckBox.isSelected() &&
                        !hackberryCheckBox.isSelected() &&
                        !caraganaCheckBox.isSelected();


        setBulkSelectEnabled(selectAllLabel, !allSelected);
        setBulkSelectEnabled(selectNoneLabel, !noneSelected);
    }

    private void setBulkSelectEnabled(Label label, boolean enabled) {
        if (label == null) return;

        label.setDisable(!enabled);
        if (enabled) {
            label.setTextFill(Color.DODGERBLUE); // blue when not clicked
            label.setOpacity(1.0);
        } else {
            label.setTextFill(Color.GREY);       // greyed out when clicked
            label.setOpacity(0.6);
        }
    }

//    private void updateFruitVisibility() {
//        if (fruitGraphics.isEmpty()) {
//            return;
//        }
//
//        for (var entry : fruitGraphics.entrySet()) {
//            String key = entry.getKey();
//            boolean visible = isFruitEnabled(key);
//
//            for (Graphic g : entry.getValue()) {
//                g.setVisible(visible);
//            }
//        }
//    }

    /**
     * For selected and unselected checkboxes
     */
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
            case "butternut"  -> butternutCheckBox == null || butternutCheckBox.isSelected();
            case "saskatoon"  -> saskatoonCheckbox == null || saskatoonCheckbox.isSelected();
            case "russian olive" -> russianOliveCheckbox == null || russianOliveCheckbox.isSelected();
            case "coffeetree pod" -> coffeeTreeCheckBox == null || coffeeTreeCheckBox.isSelected();
            case "walnut" -> walnutCheckBox == null || walnutCheckBox.isSelected();
            case "hackberry" -> hackberryCheckBox == null || hackberryCheckBox.isSelected();
            case "caragana flower/pod" -> caraganaCheckBox == null || caraganaCheckBox.isSelected();
            default           -> true; // any other fruit type stays visible
        };
    }
}
