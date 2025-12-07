module com.macewan.cmpt305.edibletreesmap {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires com.esri.arcgisruntime;
    requires java.net.http;
    requires com.opencsv;
    requires org.apache.commons.csv;
    requires jdk.compiler;
    requires java.desktop;

    opens com.macewan.cmpt305.edibletreesmap to javafx.fxml;
    exports com.macewan.cmpt305.edibletreesmap.UILayer;
    opens com.macewan.cmpt305.edibletreesmap.UILayer to javafx.fxml;
    exports com.macewan.cmpt305.edibletreesmap.DataObjectsLayer;
    opens com.macewan.cmpt305.edibletreesmap.DataObjectsLayer to javafx.fxml;
    exports com.macewan.cmpt305.edibletreesmap.ServiceLayer;
    opens com.macewan.cmpt305.edibletreesmap.ServiceLayer to javafx.fxml;
    exports com.macewan.cmpt305.edibletreesmap.DataAccessLayer;
    opens com.macewan.cmpt305.edibletreesmap.DataAccessLayer to javafx.fxml;
}