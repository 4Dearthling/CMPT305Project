package com.macewan.cmpt305.edibletreesmap;

import com.esri.arcgisruntime.mapping.view.Graphic;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TreeFilterPanel extends VBox {

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

    // dropdown parts
    private VBox dropdownContent;
    private Label dropdownDisplay;
    private Label dropdownArrow;

    // reference to fruitGraphics from the app
    private final Map<String, List<Graphic>> fruitGraphics;

    /**
     * Creates the tree filter panel that controls which fruit types
     * are visible on the map.
     *
     * @param fruitGraphics - a map where each key represents a fruit type
     *                      and each val is a list of graphics associated with that type.
     */
    public TreeFilterPanel(Map<String, List<Graphic>> fruitGraphics) {
        super(10);
        this.fruitGraphics = fruitGraphics; // reference to the map of fruit-type graphic
        setPadding(new Insets(10)); // add padding for cleaner look
        buildUI(); // build the dropdown ui and the checkboxes
        updateBulkSelectState(); // refresh the state of the "Select All" or "Select None" label
    }

    /**
     * Builds the UI for the dropdown, checkboxes and the select all or none labels
     *
     */
    private void buildUI() {

        // Title label like in the portal
        Label title = new Label("Type of Edible Fruit");
        title.setFont(Font.font("System", FontWeight.BOLD, 16));

        // header of the dropdown
        dropdownDisplay = new Label("Select…");
        dropdownArrow = new Label("▼"); //taken from coolsymbol.top

        //creating the dropdown for the filtered boxes.
        HBox dropdownHeader = new HBox(10, dropdownDisplay, new Region(), dropdownArrow);
        HBox.setHgrow(dropdownHeader.getChildren().get(1), Priority.ALWAYS);
        dropdownHeader.setPadding(new Insets(6, 10, 6, 10));
        dropdownHeader.setBorder(
                new Border(new BorderStroke(
                        Color.LIGHTGRAY,
                        BorderStrokeStyle.SOLID,
                        new CornerRadii(4),
                        new BorderWidths(1)))
        );
        dropdownHeader.setBackground(
                new Background(new BackgroundFill(Color.WHITE, new CornerRadii(4), Insets.EMPTY))
        );

        // when the header is clicked, toggle visibility of the dropdownContent
        dropdownHeader.setOnMouseClicked(e -> toggleDropdown());

        // checkboxes and to choose multiple or none
        selectAllLabel = new Label("Select All");
        selectNoneLabel = new Label("Select None");

        HBox bulkRow = new HBox(10, selectAllLabel, selectNoneLabel);
        bulkRow.setPadding(new Insets(5, 0, 5, 0));

        selectAllLabel.setTextFill(Color.DODGERBLUE);
        selectNoneLabel.setTextFill(Color.DODGERBLUE);

        // when select all is clicked it sets all the checkboxes to be checked
        selectAllLabel.setOnMouseClicked(e -> {
            if (!selectAllLabel.isDisabled()) {
                setAllCheckBoxes(true);
                updateFruitVisibility();
                updateBulkSelectState();
                updateDropdownDisplayText(); // the dropdown header will display the updated selection and will close
            }
        });

        // if select none is clicked it sets all checkboxes to false (deselects them)
        selectNoneLabel.setOnMouseClicked(e -> {
            if (!selectNoneLabel.isDisabled()) {
                setAllCheckBoxes(false);
                updateFruitVisibility();
                updateBulkSelectState(); //updates the selection status (No
                updateDropdownDisplayText(); // updates the dropdown display to say how many are selected
            }
        });

        // create all the checkboxes
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
        saskatoonCheckbox = new CheckBox("Saskatoon");
        russianOliveCheckbox = new CheckBox("Russian Olive");
        coffeeTreeCheckBox = new CheckBox("Coffee Tree");
        walnutCheckBox = new CheckBox("Walnut");
        hackberryCheckBox = new CheckBox("Hackberry");
        caraganaCheckBox = new CheckBox("Caragana Flower/Pod");

        // start with everything selected
        selectedBoxes(appleCheckBox, cherryCheckBox, crabappleCheckBox, plumCheckBox, pearCheckBox, chokeCherryCheckBox, acornCheckBox, hawthornCheckBox);
        selectedBoxes(juniperCheckBox, butternutCheckBox, saskatoonCheckbox, russianOliveCheckbox, coffeeTreeCheckBox, walnutCheckBox, hackberryCheckBox, caraganaCheckBox);

        // when any checkbox changes, update visibility & header state
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

        // content that shows when dropdown is open
        dropdownContent = new VBox(5, bulkRow,
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

        dropdownContent.setPadding(new Insets(8, 10, 8, 10));
        dropdownContent.setBorder(
                new Border(new BorderStroke(
                        Color.LIGHTGRAY,
                        BorderStrokeStyle.SOLID,
                        new CornerRadii(4),
                        new BorderWidths(1, 1, 1, 1)))
        );
        dropdownContent.setBackground(
                new Background(new BackgroundFill(Color.WHITE, new CornerRadii(4), Insets.EMPTY))
        );

        // start closed dropdown (header shows)
        dropdownContent.setVisible(false);
        dropdownContent.setManaged(false);
        updateDropdownDisplayText();

        // add everything to this VBox
        getChildren().addAll(title, dropdownHeader, dropdownContent);
    }

    private void selectedBoxes(CheckBox appleCheckBox, CheckBox cherryCheckBox, CheckBox crabappleCheckBox, CheckBox plumCheckBox, CheckBox pearCheckBox, CheckBox chokeCherryCheckBox, CheckBox acornCheckBox, CheckBox hawthornCheckBox) {
        appleCheckBox.setSelected(true);
        cherryCheckBox.setSelected(true);
        crabappleCheckBox.setSelected(true);
        plumCheckBox.setSelected(true);
        pearCheckBox.setSelected(true);
        chokeCherryCheckBox.setSelected(true);
        acornCheckBox.setSelected(true);
        hawthornCheckBox.setSelected(true);
    }

    private void toggleDropdown() {
        boolean nowVisible = !dropdownContent.isVisible();
        dropdownContent.setVisible(nowVisible);
        dropdownContent.setManaged(nowVisible);
        dropdownArrow.setText(nowVisible ? "▲" : "▼");
    }

    private void addListener(CheckBox cb) {
        cb.selectedProperty().addListener((obs, o, n) -> {
            updateFruitVisibility();
            updateBulkSelectState();
            updateDropdownDisplayText();
        });
    }

    private void setAllCheckBoxes(boolean value) {
        selected(value, appleCheckBox, cherryCheckBox, crabappleCheckBox, plumCheckBox, pearCheckBox, chokeCherryCheckBox, acornCheckBox, hawthornCheckBox);
        selected(value, juniperCheckBox, butternutCheckBox, saskatoonCheckbox, russianOliveCheckbox, coffeeTreeCheckBox, walnutCheckBox, hackberryCheckBox, caraganaCheckBox);
    }

    private void selected(boolean value, CheckBox appleCheckBox, CheckBox cherryCheckBox, CheckBox crabappleCheckBox, CheckBox plumCheckBox, CheckBox pearCheckBox, CheckBox chokeCherryCheckBox, CheckBox acornCheckBox, CheckBox hawthornCheckBox) {
        if (appleCheckBox != null) appleCheckBox.setSelected(value);
        if (cherryCheckBox != null) cherryCheckBox.setSelected(value);
        if (crabappleCheckBox != null) crabappleCheckBox.setSelected(value);
        if (plumCheckBox != null) plumCheckBox.setSelected(value);
        if (pearCheckBox != null) pearCheckBox.setSelected(value);
        if (chokeCherryCheckBox != null) chokeCherryCheckBox.setSelected(value);
        if (acornCheckBox != null) acornCheckBox.setSelected(value);
        if (hawthornCheckBox != null) hawthornCheckBox.setSelected(value);
    }


    private void updateBulkSelectState() {
        if (appleCheckBox == null) return;

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
            label.setTextFill(Color.DODGERBLUE);
            label.setOpacity(1.0);
        } else {
            label.setTextFill(Color.GREY);
            label.setOpacity(0.6);
        }
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

    private boolean isFruitEnabled(String key) {
        key = key.toLowerCase();
        return switch (key) {
            case "apple" -> appleCheckBox == null || appleCheckBox.isSelected();
            case "cherry" -> cherryCheckBox == null || cherryCheckBox.isSelected();
            case "crabapple" -> crabappleCheckBox == null || crabappleCheckBox.isSelected();
            case "plum" -> plumCheckBox == null || plumCheckBox.isSelected();
            case "pear" -> pearCheckBox == null || pearCheckBox.isSelected();
            case "chokecherry" -> chokeCherryCheckBox == null || chokeCherryCheckBox.isSelected();
            case "acorn" -> acornCheckBox == null || acornCheckBox.isSelected();
            case "hawthorn" -> hawthornCheckBox == null || hawthornCheckBox.isSelected();
            case "juniper" -> juniperCheckBox == null || juniperCheckBox.isSelected();
            case "butternut" -> butternutCheckBox == null || butternutCheckBox.isSelected();
            case "saskatoon" -> saskatoonCheckbox == null || saskatoonCheckbox.isSelected();
            case "russian olive" -> russianOliveCheckbox == null || russianOliveCheckbox.isSelected();
            case "coffeetree pod" -> coffeeTreeCheckBox == null || coffeeTreeCheckBox.isSelected();
            case "walnut" -> walnutCheckBox == null || walnutCheckBox.isSelected();
            case "hackberry" -> hackberryCheckBox == null || hackberryCheckBox.isSelected();
            case "caragana flower/pod" -> caraganaCheckBox == null || caraganaCheckBox.isSelected();
            default -> true;
        };
    }

    private void updateDropdownDisplayText() {

        // LinkedHashMap keeps insertion order supports no limit to size
        Map<String, Boolean> selections = new LinkedHashMap<>();

        selections.put("Apple", appleCheckBox.isSelected());
        selections.put("Cherry", cherryCheckBox.isSelected());
        selections.put("Crabapple", crabappleCheckBox.isSelected());
        selections.put("Plum", plumCheckBox.isSelected());
        selections.put("Pear", pearCheckBox.isSelected());
        selections.put("Chokecherry", chokeCherryCheckBox.isSelected());
        selections.put("Acorn", acornCheckBox.isSelected());
        selections.put("Hawthorn", hawthornCheckBox.isSelected());
        selections.put("Juniper", juniperCheckBox.isSelected());
        selections.put("Butternut", butternutCheckBox.isSelected());
        selections.put("Saskatoon", saskatoonCheckbox.isSelected());
        selections.put("Russian Olive", russianOliveCheckbox.isSelected());
        selections.put("Coffee Tree", coffeeTreeCheckBox.isSelected());
        selections.put("Walnut", walnutCheckBox.isSelected());
        selections.put("Hackberry", hackberryCheckBox.isSelected());
        selections.put("Caragana Flower/Pod", caraganaCheckBox.isSelected());

        // count selected + list names
        List<String> selectedNames = selections.entrySet().stream()
                .filter(Map.Entry::getValue)
                .map(Map.Entry::getKey)
                .toList();

        int count = selectedNames.size();
        int total = selections.size();

        if (count == 0) {
            dropdownDisplay.setText("None selected");
        } else if (count == total) {
            dropdownDisplay.setText("All types selected");
        } else if (count == 1) {
            dropdownDisplay.setText(selectedNames.getFirst());  // show the specific fruit
        } else {
            dropdownDisplay.setText(count + " selected types");
        }
    }
}