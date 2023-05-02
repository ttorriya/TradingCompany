module SAPIS_client {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires javafx.graphics;
    requires jfreechart;
    requires java.desktop;
    requires javafx.swing;
    requires org.apache.pdfbox;
    opens org.client to javafx.fxml;
    opens classes to javafx.fxml;
    exports org.client;
    exports classes;
    exports controllers.authentication.sign_in;
    opens controllers.authentication.sign_in to javafx.fxml;
    exports controllers.authentication.sign_up;
    opens controllers.authentication.sign_up to javafx.fxml;
    exports controllers.supplier;
    opens controllers.supplier to javafx.fxml;
    exports controllers.admin;
    opens controllers.admin to javafx.fxml;
    exports controllers.employee;
    opens controllers.employee to javafx.fxml;
    exports controllers.controller_service;
    opens controllers.controller_service to javafx.fxml;
    exports controllers.authentication;
    opens controllers.authentication to javafx.fxml;
}