package controllers.authentication.sign_up;

import classes.RequestHandler;
import classes.ResponseHandler;
import controllers.controller_service.ControllerImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.client.MainApp;

import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController extends ControllerImpl {

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
    private ComboBox<String> accessBox;

    @FXML
    private Button sigh_upButton;

    @FXML
    private Button exitButton;

    @FXML
    void initialize() {
        initComboBoxValue(accessBox);

        exitButton.setOnAction(event -> {
            OpenWindow(exitButton, "/fxml/authentication/authMenu.fxml");
        });

        sigh_upButton.setOnAction(event -> {
            if(accessBox.getSelectionModel().getSelectedItem().equals("Поставщик")) {
                ResponseHandler.massage = login.getText().trim() + "%" + password.getText().trim() + "%" + email.getText().trim()
                        + "%" + phone_number.getText().trim() + "%" + firstname.getText().trim()
                        + "%" + lastname.getText().trim() + "%" + patronymic.getText().trim();
                OpenWindow(sigh_upButton, "/fxml/authentication/sign_up/sign_up_supplier.fxml");
            }
            else {
                if (!login.getText().trim().equals("") && !password.getText().trim().equals("")
                        && !firstname.getText().trim().equals("") && !lastname.getText().trim().equals("")
                        && !patronymic.getText().trim().equals("") && !phone_number.getText().trim().equals("")
                        && !email.getText().trim().equals("")) {

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
                } else {
                    showAlert("Заполнены не все поля!");
                }
                if (checkAdd == true) {
                    OpenWindow(sigh_upButton, "/fxml/authentication/authMenu.fxml");
                } else OpenWindow(sigh_upButton, "/fxml/authentication/sign_up/sign_up.fxml");
            }
        });

    }

    private void signUpUser() {
        /*if(accessBox.getSelectionModel().getSelectedItem().equals("Поставщик")){
            ResponseHandler.massage = login.getText().trim() + "%" + password.getText().trim() + "%" + email.getText().trim()
                    + "%" + phone_number.getText().trim() + "%" + firstname.getText().trim()
                    + "%" + lastname.getText().trim() + "%" + patronymic.getText().trim();*/
            /*Stage stage = (Stage)s.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/sign_up_supplier.fxml"));
            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Parent root = loader.getRoot();
            stage.setScene(new Scene(root));
            stage.show();*/
           /* FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/fxml/sign_up_supplier.fxml"));
            Stage stage = (Stage) exitButton.getScene().getWindow();
            stage.close();
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 600, 400);
            } catch (IOException e) {
                e.printStackTrace();
            }
            stage = new Stage();
            stage.setScene(scene);
            stage.show();*/
        /*    OpenWindow(sigh_upButton, "/fxml/sign_up_supplier.fxml");
        }
        else {*/
            String request = RequestHandler.requestAddUser(login.getText().trim(), password.getText().trim(), email.getText().trim(),
                    phone_number.getText().trim(), firstname.getText().trim(), lastname.getText().trim(), patronymic.getText().trim(),
                    accessBox.getSelectionModel().getSelectedItem());
            MainApp.sendData(request);

    }

    private void initComboBoxValue(ComboBox<String> comboBox){
        ObservableList<String> accessRights =
                FXCollections.observableArrayList(/*"Пользователь", */"Сотрудник", "Поставщик", "Администратор");
        comboBox.setItems(accessRights);
        comboBox.setValue("Сотрудник");
    }

}
