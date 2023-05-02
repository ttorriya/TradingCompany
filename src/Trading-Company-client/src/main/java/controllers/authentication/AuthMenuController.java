package controllers.authentication;


import controllers.controller_service.ControllerImpl;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class AuthMenuController extends ControllerImpl {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button employeeButton;

    @FXML
    private Button supplierButton;

    @FXML
    private Button adminButton;

    @FXML
    private Button sign_upButton;

    @FXML
    void initialize() {
        adminButton.setOnAction(event -> {
            OpenWindow(adminButton, "/fxml/authentication/sign_in/sign_in_Admin.fxml");
        });

        supplierButton.setOnAction(event -> {
            OpenWindow(supplierButton, "/fxml/authentication/sign_in/sign_in_Supplier.fxml");
        });

        employeeButton.setOnAction(event -> {
            OpenWindow(employeeButton, "/fxml/authentication/sign_in/sign_in_Employee.fxml");
        });

        sign_upButton.setOnAction(event -> {
            OpenWindow(sign_upButton, "/fxml/authentication/sign_up/sign_up.fxml");
        });
    }
}


