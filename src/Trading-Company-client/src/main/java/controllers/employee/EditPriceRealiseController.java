package controllers.employee;

import java.net.URL;
import java.util.ResourceBundle;

import classes.Good;
import classes.GoodIterator;
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

public class EditPriceRealiseController extends ControllerImpl {
    private String loginUser = ResponseHandler.login;
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
    private Button exitButton;

    @FXML
    private Button priceRealiseButton;

    @FXML
    private ScrollPane scrollpane;

    @FXML
    private TableView<Good> goodsTable;

    @FXML
    private TableColumn<Good, Integer> barcode;

    @FXML
    private TableColumn<Good, String> product_name;

    @FXML
    private TableColumn<Good, Float> price;

    @FXML
    private TableColumn<Good, Float> occupied_space;

    @FXML
    private TableColumn<Good, Integer> quantity;

    @FXML
    private TableColumn<Good, Float> ratingNum;

    @FXML
    private TableColumn<Good, Float> ratingPrice;

    @FXML
    private TableColumn<Good, Float> priceRealise;

    @FXML
    private Button todoButton;

    @FXML
    private TextField inputRatingNum;

    @FXML
    private Text ratingTextNum;

    @FXML
    void initialize() {
        name.setText(ResponseHandler.name);
        surname.setText(ResponseHandler.surname);

        getGoods();
        getRating();

        barcode.setCellValueFactory(new PropertyValueFactory<Good, Integer>("barcode"));
        product_name.setCellValueFactory(new PropertyValueFactory<Good, String>("product_name"));
        quantity.setCellValueFactory(new PropertyValueFactory<Good, Integer>("quantity"));
        price.setCellValueFactory(new PropertyValueFactory<Good, Float>("price"));
        occupied_space.setCellValueFactory(new PropertyValueFactory<Good, Float>("occupied_space"));
        ratingNum.setCellValueFactory(new PropertyValueFactory<Good, Float>("ratingNum"));
        ratingPrice.setCellValueFactory(new PropertyValueFactory<Good, Float>("ratingPrice"));
        priceRealise.setCellValueFactory(new PropertyValueFactory<Good, Float>("priceRealise"));

        goodsTable.setItems(goods);

        goodsTable.setRowFactory(tv -> {
            TableRow<Good> row = new TableRow<>();
            row.setOnMouseClicked(event1 -> {

                if (!row.isEmpty() && event1.getButton() == MouseButton.PRIMARY
                        && event1.getClickCount() == 1) {
                    Good clickedRow = row.getItem();

                    todoButton.setOnAction(event -> {
                        if (!inputRatingNum.getText().equals("")) {
                            try {
                                Float.valueOf(inputRatingNum.getText());
                                if (Float.valueOf(inputRatingNum.getText()) < 0) {
                                    showAlert("Неверный формат ввода!");
                                } else {
                                    sendRequestUpdatePriceRealise(clickedRow.getBarcode(), inputRatingNum.getText());
                                    showAlert("Выполнено");


                                    GoodIterator goodIterator = new GoodIterator(goods);
                                    while(goodIterator.hasNext()){
                                        Good tempGood = (Good) goodIterator.getNext();
                                        int position = goodIterator.getCurrentPosition();
                                        if(tempGood.getBarcode() == clickedRow.getBarcode()){
                                            tempGood.setPriceRealise(Float.valueOf(inputRatingNum.getText()));
                                            row.setItem(tempGood);
                                            goodsTable.refresh();
                                        }
                                        goods.get(position).setPriceRealise(Float.valueOf(inputRatingNum.getText()));
                                    }




                                    inputRatingNum.setText("");
                                }
                            } catch (NumberFormatException e) {
                                showAlert("Неверный формат ввода!");
                            }
                        }
                    });

                }

            });
            return row;
        });

        exitButton.setOnAction(event -> {
            OpenWindow(exitButton, "/fxml/employee/mainMenuEmployee.fxml");
        });

    }

    private void sendRequestUpdatePriceRealise(int barcode, String price){
        String request = RequestHandler.requestUpdatePriceRealise(barcode, price);
        MainApp.sendData(request);
    }

    private void sendRequestGetRating(String loginUser){
        String request = RequestHandler.requestGetRating(loginUser);
        MainApp.sendData(request);
    }

    private void getRating(){
        sendRequestGetRating(loginUser);

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(ResponseHandler.massage.trim().equals("")){}
        else {
            String[] arrayUsersData = ResponseHandler.massage.split("%");
            int j = 0;
            while(j < arrayUsersData.length) {
                for (int i = 0; i < goods.size() && j < arrayUsersData.length; i++) {
                    if (goods.get(i).getBarcode() == Integer.valueOf(arrayUsersData[j])) {
                        goods.get(i).setRatingNum(Float.valueOf(arrayUsersData[++j]));
                        goods.get(i).setRatingPrice(Float.valueOf(arrayUsersData[++j]));
                        j++;
                    }
                }
            }
        }
    }

    private void sendRequestGetListGoodsRating(String loginUser){
        String request = RequestHandler.requestGetListGoodsRating(loginUser);
        MainApp.sendData(request);
    }

    private void getGoods() {
        sendRequestGetListGoodsRating(loginUser);

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
                i++;
                good.setPrice(Float.valueOf(arrayUsersData[i++]/*.replaceAll("руб", "")*/.trim()));
                good.setOccupied_space(Float.valueOf(arrayUsersData[i++]));
                good.setQuantity(Integer.valueOf(arrayUsersData[i++]));
                good.setPriceRealise(Float.valueOf(arrayUsersData[i++]/*.replaceAll("руб", "")*/.trim()));
                goods.add(good);
            }
        }
    }

    /*private void getGoods() {
        sendRequestGetListGoodsRating(loginUser);

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(ResponseHandler.massage.trim().equals("")){}
        else {
            String[] arrayUsersData = ResponseHandler.massage.split("#")[0].split("%");

            int i = 0;
            while (i < arrayUsersData.length) {
                Good good = new Good();
                good.setBarcode(Integer.valueOf(arrayUsersData[i++]));
                good.setProduct_name(arrayUsersData[i++]);
                good.setUnit_measurement(arrayUsersData[i++]);
                good.setQuantity(Integer.valueOf(arrayUsersData[i++]));
                good.setPrice(Float.valueOf(arrayUsersData[i++].replaceAll("руб", "").trim()));
                good.setOccupied_space(Float.valueOf(arrayUsersData[i++]));
                goods.add(good);
            }
            i = 0;
            arrayUsersData = ResponseHandler.massage.split("#")[1].split("%");
            int j = 0;
            while(j < arrayUsersData.length - 1) {
                i = 0;
                while (i < goods.size()) {
                    if (goods.get(i).getBarcode() == Integer.valueOf(arrayUsersData[j])) {
                        ++j;
                        goods.get(i).setPriceRealise(Float.valueOf(arrayUsersData[j++].replaceAll("руб", "").trim()));
                        i = goods.size();
                    } else {
                        ++i;
                    }
                }
            }
        }
    }*/

    /* for(int i = 0; i < goods.size(); i++){
                                        if(goods.get(i).getBarcode() == clickedRow.getBarcode()){
                                            goods.get(i).setPriceRealise(Float.valueOf(inputRatingNum.getText()));
                                            row.setItem(goods.get(i));
                                            goodsTable.refresh();
                                            i = goods.size();
                                        }
                                    }*/
}
