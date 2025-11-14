module com.macewan.cmpt305.edibletreesmap {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires com.esri.arcgisruntime;

    opens com.macewan.cmpt305.edibletreesmap to javafx.fxml;
    exports com.macewan.cmpt305.edibletreesmap;
}