package controllers.admin;

import java.net.URL;
import java.util.ResourceBundle;

import classes.RequestHandler;
import classes.ResponseHandler;
import controllers.controller_service.ControllerImpl;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.client.MainApp;

public class EditMenuWarehouseController extends ControllerImpl {
    private String loginUser = ResponseHandler.login;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Text Surname;

    @FXML
    private Text Name;

    @FXML
    private MenuButton settingsMenu;

    @FXML
    private Button addGoodButton;

    @FXML
    private Button exitButton;

    @FXML
    private Button editButton;

    @FXML
    private TextField nameWarehouse;

    @FXML
    private TextField volume;

    @FXML
    void initialize() {
        editButton.setText("Изменить");
        Name.setText(ResponseHandler.name);
        Surname.setText(ResponseHandler.surname);

        String array[] = ResponseHandler.massage.split("%");
        nameWarehouse.setText(array[1]);
        volume.setText(array[2]);

        exitButton.setOnAction(event -> {
            ResponseHandler.massage = loginUser;
            OpenWindow(exitButton, "/fxml/admin/mainMenuAdmin.fxml");
        });

        editButton.setOnAction(event -> {
            if(!nameWarehouse.getText().equals("") && !volume.getText().equals("")
                    && Integer.parseInt(volume.getText()) >= Integer.parseInt(array[3])){
                sendRequestEditWarehouse(array[0], nameWarehouse.getText(), volume.getText());
                showAlert("Выполнено");
            }
            else {
                if(Integer.parseInt(volume.getText()) >= Integer.parseInt(array[3])){
                    showAlert("Объём склада меньше, чем на нём находится товара!");
                }
                else showAlert("Заполните все поля");
            }
        });

    }

    private void sendRequestEditWarehouse(String id, String name, String volume){
        String request = RequestHandler.requestEditWarehouse(id, name, volume);
        MainApp.sendData(request);
    }
}
