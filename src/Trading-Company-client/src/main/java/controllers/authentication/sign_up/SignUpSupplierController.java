package controllers.authentication.sign_up;

import classes.RequestHandler;
import classes.ResponseHandler;
import controllers.controller_service.ControllerImpl;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.client.MainApp;

import java.net.URL;
import java.util.ResourceBundle;

public class SignUpSupplierController extends ControllerImpl {
    private boolean checkAdd = false;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Text accessText;

    @FXML
    private TextField firstname;

    @FXML
    private TextField lastname;

    @FXML
    private TextField patronymic;

    @FXML
    private TextField email;

    @FXML
    private TextField phone_number;

    @FXML
    private TextField login;

    @FXML
    private TextField password;

    @FXML
    private Button sigh_inButton;

    @FXML
    private Button exitButton;

    @FXML
    private TextField city;

    @FXML
    private TextField country;

    @FXML
    private TextField address;

    @FXML
    private TextField org_name;

    @FXML
    void initialize() {
        String[] array = ResponseHandler.massage.split("%");
        login.setText(array[0]);
        password.setText(array[1]);
        email.setText(array[2]);
        phone_number.setText(array[3]);
        firstname.setText(array[4]);
        lastname.setText(array[5]);
        patronymic.setText(array[6]);

        exitButton.setOnAction(event -> {
            OpenWindow(exitButton, "/fxml/authentication/authMenu.fxml");
        });

        sigh_inButton.setOnAction(event -> {
            if(!login.getText().trim().equals("") && !password.getText().trim().equals("")
                    && !firstname.getText().trim().equals("") && !lastname.getText().trim().equals("")
                    && !patronymic.getText().trim().equals("") && !phone_number.getText().trim().equals("")
                    && !email.getText().trim().equals("") && !org_name.getText().trim().equals("")
                    && !city.getText().trim().equals("") && !country.getText().trim().equals("")
                    && !address.getText().trim().equals("")) {
                signUpUser();
                boolean check = false;
                while (!check) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (ResponseHandler.response != null && ResponseHandler.response.split(" ")[0].equals("AddUser")) {
                        if (ResponseHandler.request.equals("Error")) {
                            showAlert(ResponseHandler.massage);
                        } else {
                            showAlert(ResponseHandler.massage);
                            checkAdd = true;
                        }
                        check = true;
                    }
                }
            }
            else{
                showAlert("Заполнены не все поля!");
            }
            if (checkAdd == true) {
                OpenWindow(sigh_inButton, "/fxml/authentication/authMenu.fxml");
            }
            else OpenWindow(sigh_inButton, "/fxml/authentication/sign_up/sign_up.fxml");
        });

    }


    private void signUpUser() {
        String request = RequestHandler.requestAddUser(login.getText().trim(), password.getText().trim(), email.getText().trim(),
                phone_number.getText().trim(), firstname.getText().trim(), lastname.getText().trim(), patronymic.getText().trim(),
                "Поставщик", org_name.getText().trim(), city.getText().trim(), country.getText().trim(),
                address.getText().trim());
        MainApp.sendData(request);
    }
}
