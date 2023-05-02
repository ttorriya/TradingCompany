package controllers.authentication.sign_up;

import classes.Good;
import classes.RequestHandler;
import classes.ResponseHandler;
import controllers.controller_service.ControllerImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.text.Text;
import org.client.MainApp;

import java.net.URL;
import java.util.ResourceBundle;

public class SupplierMainMenuController extends ControllerImpl {
    private String loginUser = ResponseHandler.massage;
    private ObservableList<Good> goods = FXCollections.observableArrayList();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Text surname;

    @FXML
    private Text name;

    @FXML
    private MenuButton settingsMenu;

    @FXML
    private Button deleteGoodButton;

    @FXML
    private Button editGoodButton;

    @FXML
    private Button addGoodButton;

    @FXML
    private Button exitButton;

    @FXML
    private ScrollPane scrollpane;

    @FXML
    private TableView<Good> goodsTable;

    @FXML
    private TableColumn<Good, Integer> barcode;

    @FXML
    private TableColumn<Good, String> product_name;

    @FXML
    private TableColumn<Good, String> unit_measurement;

    @FXML
    private TableColumn<Good, Float> price;

    @FXML
    private TableColumn<Good, Float> occupied_space;

    @FXML
    private TableColumn<Good, Integer> quantity;

    @FXML
    private Button todoButton;

    @FXML
    void initialize() {
        String style = addGoodButton.getStyle();

        getGoods();
        todoButton.setVisible(false);
        sendRequestGetNameSurname(loginUser);
        name.setText(ResponseHandler.name);
        surname.setText(ResponseHandler.surname);

        barcode.setCellValueFactory(new PropertyValueFactory<Good, Integer>("barcode"));
        product_name.setCellValueFactory(new PropertyValueFactory<Good, String>("product_name"));
        unit_measurement.setCellValueFactory(new PropertyValueFactory<Good, String>("unit_measurement"));
        quantity.setCellValueFactory(new PropertyValueFactory<Good, Integer>("quantity"));
        price.setCellValueFactory(new PropertyValueFactory<Good, Float>("price"));
        occupied_space.setCellValueFactory(new PropertyValueFactory<Good, Float>("occupied_space"));

        exitButton.setOnAction(event -> {
            OpenWindow(exitButton, "/fxml/authentication/authMenu.fxml");
        });

        deleteGoodButton.setOnAction(event -> {
            if(goods.isEmpty()){
                showAlert("Список товаров пуст");
            }
            else {
                deleteGoodButton.setStyle("-fx-background-color: #3c4c5b; opacity: 0.3");
                editGoodButton.setStyle(style);
                addGoodButton.setStyle(style);
                todoButton.setVisible(true);
                todoButton.setText("Удалить");
                goodsTable.setItems(goods);


                goodsTable.setRowFactory(tv -> {
                    TableRow<Good> row = new TableRow<>();
                    row.setOnMouseClicked(event1 -> {
                        if (!row.isEmpty() && event1.getButton() == MouseButton.PRIMARY
                                && event1.getClickCount() == 1) {
                            Good clickedRow = row.getItem();
                            todoButton.setOnAction(event2 -> {
                                sendRequestDeleteGood(clickedRow.getBarcode());
                                goodsTable.getItems().clear();
                                showAlert("Выполнено");
                                ResponseHandler.massage = loginUser;
                                OpenWindow(exitButton, "/fxml/supplier/mainMenuSupplier.fxml");
                            });
                        }
                    });
                    return row;
                });
            }
        });

        editGoodButton.setOnAction(event -> {
            if(goods.isEmpty()){
                showAlert("Список товаров пуст");
            }
            else {
                editGoodButton.setStyle("-fx-background-color: #3c4c5b; opacity: 0.3");
                deleteGoodButton.setStyle(style);
                addGoodButton.setStyle(style);
                goodsTable.setItems(goods);
                todoButton.setVisible(true);
                todoButton.setText("Изменить");

                goodsTable.setRowFactory(tv -> {
                    TableRow<Good> row = new TableRow<>();
                    row.setOnMouseClicked(event1 -> {
                        if (!row.isEmpty() && event1.getButton() == MouseButton.PRIMARY
                                && event1.getClickCount() == 1) {
                            Good clickedRow = row.getItem();
                            todoButton.setOnAction(event2 -> {
                                ResponseHandler.massage = clickedRow.getBarcode() + "%" + clickedRow.getProduct_name() + "%"
                                        + clickedRow.getUnit_measurement() + "%" + clickedRow.getQuantity() + "%"
                                        + clickedRow.getPrice() + "%" + clickedRow.getOccupied_space() + "%" + loginUser;
                                OpenWindow(exitButton, "/fxml/supplier/editMenuSupplier.fxml");
                            });
                        }
                    });
                    return row;
                });
            }
        });

        addGoodButton.setOnAction(event -> {
            ResponseHandler.massage = loginUser;
            OpenWindow(addGoodButton, "/fxml/supplier/addMenuSupplier.fxml");
        });

    }

    private void sendRequestDeleteGood(int id){
        String request = RequestHandler.requestDeleteSupplierGood(id, loginUser);
        MainApp.sendData(request);
        goodsTable.getItems().clear();
        getGoods();
    }

    private void sendRequestGetListGoods(){
        String request = RequestHandler.requestGetListGoods(loginUser);
        MainApp.sendData(request);
    }

    private void getGoods() {
        sendRequestGetListGoods();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(ResponseHandler.massage.trim().equals("")){}
        else {
            String[] arrayUsersData = ResponseHandler.massage.split("%");

            int i = 0;
            while (i < arrayUsersData.length) {
                Good good = new Good();
                good.setBarcode(Integer.valueOf(arrayUsersData[i++]));
                good.setProduct_name(arrayUsersData[i++]);
                good.setUnit_measurement(arrayUsersData[i++]);
                good.setQuantity(Integer.valueOf(arrayUsersData[i++]));
                good.setPrice(Float.valueOf(arrayUsersData[i++]/*.replaceAll("руб", "")*/.trim()));
                good.setOccupied_space(Float.valueOf(arrayUsersData[i++]));
                goods.add(good);
            }
        }
    }

    private void sendRequestGetNameSurname(String loginUser){
        String request = RequestHandler.requestGetNameSurname(loginUser);
        MainApp.sendData(request);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
