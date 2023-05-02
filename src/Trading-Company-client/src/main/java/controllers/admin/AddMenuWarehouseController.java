package controllers.admin;

import classes.RequestHandler;
import classes.ResponseHandler;
import controllers.controller_service.ControllerImpl;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.client.MainApp;

public class AddMenuWarehouseController extends ControllerImpl {
    private String loginUser = ResponseHandler.login;

    @FXML
    private Text surname;
    @FXML
    private Text name123;
    @FXML
    private Button exitButton;
    @FXML
    private Button addButton;
    @FXML
    private TextField nameWarehouse;
    @FXML
    private TextField volume;

    @FXML
    void initialize() {
        name123.setText(ResponseHandler.name);
        surname.setText(ResponseHandler.surname);

        exitButton.setOnAction(event -> {
            OpenWindow(exitButton, "/fxml/admin/mainMenuAdmin.fxml");
        });

        addButton.setOnAction(event -> {
            if (!nameWarehouse.getText().equals("") && !volume.getText().equals("")) {
                sendRequestAddWarehouse(nameWarehouse.getText(), volume.getText());
                showAlert("Выполнено");
                nameWarehouse.setText("");
                volume.setText("");
            } else {
                showAlert("Заполните все поля");
            }
        });

    }

    private void sendRequestAddWarehouse(String name, String volume) {
        String request = RequestHandler.requestAddWarehouse(name, volume);
        MainApp.sendData(request);
    }
}
