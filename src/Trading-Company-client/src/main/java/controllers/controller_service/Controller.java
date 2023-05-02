package controllers.controller_service;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public interface Controller {
    void OpenWindow(Button button, String FXMLPath);
    void OpenWindowWithoutClosing(String FXMLPath);
    void showAlert(String string);
}
