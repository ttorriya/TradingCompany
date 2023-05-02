package controllers.supplier;

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

public class AddMenuSupplierController extends ControllerImpl {
    private String loginUser = ResponseHandler.massage;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Text surname;

    @FXML
    private Text name123;

    @FXML
    private MenuButton settingsMenu;

    @FXML
    private Button addGoodButton;

    @FXML
    private Button exitButton;

    @FXML
    private Button addButton;

    @FXML
    private TextField nameGood;

    @FXML
    private TextField unit;

    @FXML
    private TextField number;

    @FXML
    private TextField price;

    @FXML
    private TextField space;

    @FXML
    void initialize() {
        name123.setText(ResponseHandler.name);
        surname.setText(ResponseHandler.surname);

        exitButton.setOnAction(event -> {
            OpenWindow(exitButton, "/fxml/supplier/mainMenuSupplier.fxml");
        });

        addButton.setOnAction(event -> {
            if(!nameGood.getText().equals("") && !unit.getText().equals("") && !number.getText().equals("")
                    && !price.getText().equals("") && !space.getText().equals("")){
                try{
                    Integer.parseInt(number.getText().trim());
                    Float.parseFloat(price.getText().trim());
                    Float.parseFloat(space.getText().trim());
                    if(Integer.valueOf(number.getText().trim()) > 0
                            && Float.valueOf(price.getText().trim()) > 0
                            && Float.valueOf(space.getText().trim()) > 0){
                        sendRequestAddGood(nameGood.getText(), unit.getText(), number.getText(), price.getText(), space.getText(), loginUser);
                        showAlert("Выполнено");
                    }
                    else{
                        showAlert("Некккоректные данные");
                    }
                }
                catch (NumberFormatException e){
                    showAlert("Неверный формат ввода!");
                }
            }
            else {
                showAlert("Заполните все поля");
            }
        });

    }

    private void sendRequestAddGood(String name, String unit, String number, String price, String space, String loginUser){
        String request = RequestHandler.requestAddSupplierGood(name, unit, number, price, space, loginUser);
        MainApp.sendData(request);
    }
}
