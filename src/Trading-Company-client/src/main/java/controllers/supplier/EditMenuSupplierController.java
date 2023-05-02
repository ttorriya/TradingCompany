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

public class EditMenuSupplierController extends ControllerImpl {
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
        addButton.setText("Изменить");
        Name.setText(ResponseHandler.name);
        Surname.setText(ResponseHandler.surname);

        String array[] = ResponseHandler.massage.split("%");
        int id = Integer.valueOf(array[0]);
        nameGood.setText(array[1]);
        unit.setText(array[2]);
        number.setText(array[3]);
        price.setText(array[4]);
        space.setText(array[5]);
        loginUser = array[6];

        exitButton.setOnAction(event -> {
            ResponseHandler.massage = loginUser;
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
                        sendRequestEditGood(id, nameGood.getText(), unit.getText(), number.getText(), price.getText(), space.getText());
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

    private void sendRequestEditGood(int id, String name, String unit, String number, String price, String space){
        String request = RequestHandler.requestEditSupplierGood(id, name, unit, number, price, space);
        MainApp.sendData(request);
    }
}
