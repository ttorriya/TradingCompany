package controllers.controller_service;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public abstract class ControllerImpl implements Controller {

    @Override
    public void OpenWindow(Button button, String FXMLPath){
        Stage stage = (Stage)button.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(FXMLPath));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Parent root = loader.getRoot();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Override
    public void OpenWindowWithoutClosing(String FXMLPath){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(FXMLPath));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Override
    public void showAlert(String string){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Alert");

        alert.setHeaderText(null);
        alert.setContentText(string);

        alert.showAndWait();
    }
}
