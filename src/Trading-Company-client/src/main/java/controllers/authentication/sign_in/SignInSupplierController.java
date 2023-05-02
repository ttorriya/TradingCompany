package controllers.authentication.sign_in;

import java.net.URL;
import java.util.ResourceBundle;

import classes.RequestHandler;
import classes.ResponseHandler;
import controllers.controller_service.ControllerImpl;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.client.MainApp;

public class SignInSupplierController extends ControllerImpl {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button exitButton;

    @FXML
    private Text accessText;

    @FXML
    private Button sigh_inButton;

    @FXML
    private PasswordField password;

    @FXML
    private TextField login;

    @FXML
    void initialize() {
        exitButton.setOnAction(event -> {
            OpenWindow(exitButton, "/fxml/authentication/authMenu.fxml");
        });

        sigh_inButton.setOnAction(event -> {
            boolean checkAdd = false;
            if(!login.getText().trim().equals("") && !password.getText().trim().equals("")){
                signInUser();
                boolean check = false;
                while (!check) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (ResponseHandler.response != null && ResponseHandler.response.split(" ")[0].equals("SignInUser")) {
                        if (ResponseHandler.request.equals("Error")) {
                            showAlert(ResponseHandler.massage);
                        } else {
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
                OpenWindow(sigh_inButton, "/fxml/supplier/mainMenuSupplier.fxml");
            }
            else OpenWindow(sigh_inButton, "/fxml/authentication/sign_in/sign_in_Supplier.fxml");
        });
    }

    private void signInUser() {
        String request = RequestHandler.requestCheckSignInUser(login.getText().trim(), password.getText().trim(),
                accessText.getText().trim());
        MainApp.sendData(request);
    }
}
