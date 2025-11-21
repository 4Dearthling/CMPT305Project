module com.macewan.cmpt305.edibletreesmap {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires com.esri.arcgisruntime;
    requires java.net.http;
    requires com.opencsv;
    requires org.apache.commons.csv;

    opens com.macewan.cmpt305.edibletreesmap to javafx.fxml;
    exports com.macewan.cmpt305.edibletreesmap;
}