package controllers.admin;

import java.net.URL;
import java.util.ResourceBundle;

import classes.MetodEntity;
import classes.RequestHandler;
import classes.ResponseHandler;
import controllers.controller_service.ControllerImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import org.client.MainApp;

public class AdminMetodController extends ControllerImpl {
    private String barcode = ResponseHandler.massage;
    private ObservableList<MetodEntity> metodEntitys = FXCollections.observableArrayList();

    @FXML    private ResourceBundle resources;
    @FXML    private URL location;
    @FXML    private Text surname;
    @FXML    private Text Name;
    @FXML    private MenuButton settingsMenu;
    @FXML    private Button exitButton;
    @FXML    private Button metodButton;
    @FXML    private Button todoButton;
    @FXML    private TableView<MetodEntity> firstTable;
    @FXML    private TableColumn<MetodEntity, Integer> fisrtTableId;
    @FXML    private TableColumn<MetodEntity, Float> firstTableNum;
    @FXML    private TableColumn<MetodEntity, Float> firstTablePrice;
    @FXML    private TableView<MetodEntity> secondTable;
    @FXML    private TableColumn<MetodEntity, Integer> secondTableId;
    @FXML    private TableColumn<MetodEntity, String> secondTableNum;
    @FXML    private TableColumn<MetodEntity, String> secondTablePrice;
    @FXML    private Text finalNum;
    @FXML    private Text finalPrice;

    @FXML
    void initialize() {
        Name.setText(ResponseHandler.name);
        surname.setText(ResponseHandler.surname);

        getRating(barcode);
        metod();

        firstTable.setItems(metodEntitys);
        secondTable.setItems(metodEntitys);

        fisrtTableId.setCellValueFactory(new PropertyValueFactory<MetodEntity, Integer>("id"));
        firstTableNum.setCellValueFactory(new PropertyValueFactory<MetodEntity, Float>("num"));
        firstTablePrice.setCellValueFactory(new PropertyValueFactory<MetodEntity, Float>("price"));

        secondTableId.setCellValueFactory(new PropertyValueFactory<MetodEntity, Integer>("id"));
        secondTableNum.setCellValueFactory(new PropertyValueFactory<MetodEntity, String>("goalNum"));
        secondTablePrice.setCellValueFactory(new PropertyValueFactory<MetodEntity, String>("goalPrice"));


        exitButton.setOnAction(event -> {
            OpenWindow(exitButton, "/fxml/employee/mainMenuEmployee.fxml");
        });

        todoButton.setOnAction(event -> {
            if(Float.valueOf(finalNum.getText()) > Float.valueOf(finalPrice.getText())){
                showAlert("Необходимо пополнить склад товаров с ID = " + barcode);
                OpenWindow(todoButton, "/fxml/employee/deliveryMenuEmployee.fxml");
            }
            else if(Float.valueOf(finalNum.getText()) < Float.valueOf(finalPrice.getText())){
                showAlert("Необходимо изменить цену товара с ID = " + barcode);
                OpenWindow(todoButton, "/fxml/employee/editPriseRealiseEmployee.fxml");
            }
            else {
                showAlert("У товара с ID = " +barcode+" одинаковые веса целей");
            }
        });

    }

    private void sendRequestGetRatingMetod(String barcode){
        String request = RequestHandler.requestGetRatingMetod(barcode);
        MainApp.sendData(request);
    }

    private void getRating(String barcode){
        sendRequestGetRatingMetod(barcode);

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(ResponseHandler.massage.trim().equals("")){
            showAlert("Эксперты не выставили оценки!");
        }
        else {
            String[] arrayUsersData = ResponseHandler.massage.split("%");

            for (int j = 0; j < arrayUsersData.length; j++) {
                MetodEntity metodEntity = new MetodEntity();
                metodEntity.setId(Integer.valueOf(arrayUsersData[j++]));
                metodEntity.setNum(Float.valueOf(arrayUsersData[j++]));
                metodEntity.setPrice(Float.valueOf(arrayUsersData[j]));
                metodEntitys.add(metodEntity);
            }

        }
    }

    private void metod(){
        float[] sum = new float[metodEntitys.size()];
        for(int i = 0; i < metodEntitys.size(); i++){
            sum[i] = metodEntitys.get(i).getNum() + metodEntitys.get(i).getPrice();
            metodEntitys.get(i).setGoalNum(metodEntitys.get(i).getNum() + "/" + sum[i]);
            metodEntitys.get(i).setGoalPrice(metodEntitys.get(i).getPrice() + "/" + sum[i]);
        }
        float tempSum = 0;
        for(int i = 0; i < metodEntitys.size(); i++){
            tempSum += metodEntitys.get(i).getNum()/sum[i];
        }
        finalNum.setText(String.valueOf(tempSum/metodEntitys.size()));
        tempSum = 0;
        for(int i = 0; i < metodEntitys.size(); i++){
            tempSum += metodEntitys.get(i).getPrice()/sum[i];
        }
        finalPrice.setText(String.valueOf(tempSum/metodEntitys.size()));
    }
}
