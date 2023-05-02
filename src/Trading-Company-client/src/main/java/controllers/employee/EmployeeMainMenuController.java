package controllers.employee;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import classes.Good;
import classes.RequestHandler;
import classes.ResponseHandler;
import classes.Warehouse;
import controllers.controller_service.ControllerImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.text.Text;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.client.MainApp;

import javax.imageio.ImageIO;

public class EmployeeMainMenuController extends ControllerImpl {
    private String loginUser = ResponseHandler.login;
    private ObservableList<Good> goods = FXCollections.observableArrayList();
    private ObservableList<Warehouse> warehouses = FXCollections.observableArrayList();

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
    private Button transferButton;

    @FXML
    private Button addDeliveryButton;

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
    private TableColumn<Good, Float> ratingNum;

    @FXML
    private TableColumn<Good, Float> ratingPrice;

    @FXML
    private TableColumn<Good, Float> priceRealise;

    @FXML
    private Button deleteButton;

    @FXML
    private Button metodButton;

    @FXML
    private Button metodButton1;

    @FXML
    private Button todoButton;

    @FXML
    private StackedBarChart<String, Number> chart;

    @FXML
    private Button fullnessButton;

    @FXML
    private TextField inputRatingNum;

    @FXML
    private Text ratingTextNum;

    @FXML
    private Text ratingTextPrice;

    @FXML
    private TextField inputRatingPrice;

    @FXML
    private Button priceRealiseButton;

    @FXML
    void initialize() {
        String style = addDeliveryButton.getStyle();
        chart.setVisible(false);

        getGoods();
        if(goods.size() != 0) getRating();

        todoButton.setVisible(false);
        sendRequestGetNameSurname(loginUser);
        name.setText(ResponseHandler.name);
        surname.setText(ResponseHandler.surname);
        goodsTable.setVisible(false);
        setVisibleMetod(false);


        barcode.setCellValueFactory(new PropertyValueFactory<Good, Integer>("barcode"));
        product_name.setCellValueFactory(new PropertyValueFactory<Good, String>("product_name"));
        quantity.setCellValueFactory(new PropertyValueFactory<Good, Integer>("quantity"));
        price.setCellValueFactory(new PropertyValueFactory<Good, Float>("price"));
        occupied_space.setCellValueFactory(new PropertyValueFactory<Good, Float>("occupied_space"));
        ratingNum.setCellValueFactory(new PropertyValueFactory<Good, Float>("ratingNum"));
        ratingPrice.setCellValueFactory(new PropertyValueFactory<Good, Float>("ratingPrice"));
        priceRealise.setCellValueFactory(new PropertyValueFactory<Good, Float>("priceRealise"));



        exitButton.setOnAction(event -> {
            OpenWindow(exitButton, "/fxml/authentication/authMenu.fxml");
        });

        addDeliveryButton.setOnAction(event -> {
            OpenWindow(addDeliveryButton, "/fxml/employee/deliveryMenuEmployee.fxml");
        });

        transferButton.setOnAction(event -> {
            OpenWindow(addDeliveryButton, "/fxml/employee/transferMenuEmployee.fxml");
        });

        deleteButton.setOnAction(event -> {
            fullnessButton.setStyle(style);
            addDeliveryButton.setStyle(style);
            transferButton.setStyle(style);
            deleteButton.setStyle("-fx-background-color: #3c4c5b; opacity: 0.3");
            metodButton1.setStyle(style);
            metodButton.setStyle(style);

            scrollpane.setVisible(true);
            chart.setVisible(false);
            todoButton.setVisible(true);
            todoButton.setText("Удалить");
            goodsTable.setVisible(true);
            goodsTable.setItems(goods);
            goodsTable.setRowFactory(tv -> {
                TableRow<Good> row = new TableRow<>();
                row.setOnMouseClicked(event1 -> {

                    if (!row.isEmpty() && event1.getButton() == MouseButton.PRIMARY
                            && event1.getClickCount() == 1) {
                        Good clickedRow = row.getItem();

                        todoButton.setOnAction(event2 -> {

                            sendRequestDeleteGood(clickedRow.getBarcode());
                            showAlert("Выполнено");
                        });
                    }
                });
                return row;
            });
        });

        metodButton.setOnAction(event -> {
            getRating();
            fullnessButton.setStyle(style);
            addDeliveryButton.setStyle(style);
            transferButton.setStyle(style);
            deleteButton.setStyle(style);
            metodButton1.setStyle(style);
            metodButton.setStyle("-fx-background-color: #3c4c5b; opacity: 0.3");

            scrollpane.setVisible(true);
            setVisibleMetod(true);
            chart.setVisible(false);
            todoButton.setVisible(true);
            todoButton.setText("Оценить");
            goodsTable.setVisible(true);
            goodsTable.setItems(goods);
            goodsTable.setRowFactory(tv -> {
                TableRow<Good> row = new TableRow<>();
                row.setOnMouseClicked(event1 -> {

                    if (!row.isEmpty() && event1.getButton() == MouseButton.PRIMARY
                            && event1.getClickCount() == 1) {
                        Good clickedRow = row.getItem();

                        todoButton.setOnAction(event2 -> {
                            if(!inputRatingNum.getText().trim().equals("")
                                    && !inputRatingPrice.getText().trim().equals("")) {
                                try{
                                    Float.parseFloat(inputRatingNum.getText().trim());
                                    Float.parseFloat(inputRatingPrice.getText().trim());
                                    if(Float.parseFloat(inputRatingNum.getText().trim()) < 0
                                            && Float.parseFloat(inputRatingNum.getText().trim()) > 10
                                            && Float.parseFloat(inputRatingPrice.getText().trim()) < 0
                                            && Float.parseFloat(inputRatingPrice.getText().trim()) > 10){
                                        showAlert("Выставите оценку от 0 до 10!");
                                    }
                                    else {
                                        sendRequestAddRating(loginUser, clickedRow.getBarcode(),
                                                inputRatingNum.getText().trim(), inputRatingPrice.getText().trim());
                                        showAlert("Выполнено");
                                        for(int i = 0; i < goods.size(); i++){
                                            if(goods.get(i).getBarcode() == clickedRow.getBarcode()){
                                                clickedRow.setRatingNum(Float.parseFloat(inputRatingNum.getText()));
                                                clickedRow.setRatingPrice(Float.parseFloat(inputRatingPrice.getText()));
                                                goods.get(i).setRatingNum(clickedRow.getRatingNum());
                                                goods.get(i).setRatingPrice(clickedRow.getRatingPrice());
                                                row.setItem(goods.get(i));
                                                goodsTable.refresh();
                                                i = goods.size();
                                            }
                                        }
                                        inputRatingPrice.setText("");
                                        inputRatingNum.setText("");
                                        goodsTable.refresh();
                                    }
                                }
                                catch (NumberFormatException e){
                                    showAlert("Неверный формат ввода!");
                                }
                            }
                            else{
                                showAlert("Неккорректно введены данные!");
                            }
                        });
                    }
                });
                return row;
            });
        });

        metodButton1.setOnAction(event -> {
            fullnessButton.setStyle(style);
            addDeliveryButton.setStyle(style);
            transferButton.setStyle(style);
            deleteButton.setStyle(style);
            metodButton.setStyle(style);
            metodButton1.setStyle("-fx-background-color: #3c4c5b; opacity: 0.3");

            goodsTable.refresh();
            setVisibleMetod(false);
            chart.setVisible(false);
            scrollpane.setVisible(true);
            todoButton.setVisible(true);
            todoButton.setText("Выбрать");
            goodsTable.setVisible(true);
            goodsTable.setItems(goods);

            goodsTable.setRowFactory(tv -> {
                TableRow<Good> row = new TableRow<>();
                row.setOnMouseClicked(event1 -> {

                    if (!row.isEmpty() && event1.getButton() == MouseButton.PRIMARY
                            && event1.getClickCount() == 1) {
                        Good clickedRow = row.getItem();

                        todoButton.setOnAction(event2 -> {
                            ResponseHandler.massage = String.valueOf(clickedRow.getBarcode());
                            OpenWindow(metodButton, "/fxml/admin/metodMenuAdmin.fxml");
                        });
                    }
                });
                return row;
            });
        });

        fullnessButton.setOnAction(event -> {
            setVisibleMetod(false);
            if(warehouses.isEmpty()) getWarehouses();
            todoButton.setVisible(true);
            todoButton.setText("Сохранить отчёт");
            fullnessButton.setStyle("-fx-background-color: #3c4c5b; opacity: 0.3");
            addDeliveryButton.setStyle(style);
            transferButton.setStyle(style);
            deleteButton.setStyle(style);
            metodButton1.setStyle(style);
            metodButton.setStyle(style);

            goodsTable.setVisible(false);
            chart.setVisible(true);
            scrollpane.setVisible(false);

            String[] listWarehouses = new String[warehouses.size()];
            for(int i = 0; i < warehouses.size(); i++){
                listWarehouses[i] = warehouses.get(i).getName();
            }
            chart.setTitle("Заполненность складов");
            chart.getXAxis().setLabel("Наименование склада");
            chart.getYAxis().setLabel("Заполненность (%)");
            chart.getData().clear();

            for(int i = 0; i < warehouses.size(); i++){
                XYChart.Series<String, Number> series1 = new XYChart.Series<>();
                series1.setName(listWarehouses[i]);
                series1.getData().add(new XYChart.Data<>(listWarehouses[i],
                        Float.valueOf(warehouses.get(i).getFullness())/warehouses.get(i).getVolume()*100));
                chart.getData().add(series1);
            }


            Axis xAxis = new CategoryAxis();
            xAxis.setLabel("Наименование склада");

            Axis yAxis = new NumberAxis();
            yAxis.setLabel("Заполненность (%)");

            LineChart<String, Number> secondchart = new LineChart<>(xAxis, yAxis);
            secondchart.setAnimated(false);
            secondchart.setPrefWidth(662);
            secondchart.setPrefHeight(439);
            Group root = new Group(secondchart);
            Scene scene1 = new Scene(root, 662, 439);

            secondchart.getData().clear();

            for(int i = 0; i < warehouses.size(); i++){
                XYChart.Series<String, Number> series2 = new XYChart.Series<>();
                series2.setName(listWarehouses[i]);
                series2.getData().add(new XYChart.Data<>(listWarehouses[i],
                        Float.valueOf(warehouses.get(i).getFullness())/warehouses.get(i).getVolume()*100));
                secondchart.getData().add(series2);
            }


            todoButton.setOnAction(event1 ->{


                File file1 = new File("chart.png");
                WritableImage img = new WritableImage(662,
                        439);
                scene1.snapshot(img);
                RenderedImage renderedImage = SwingFXUtils.fromFXImage(img, null);
                try {
                    ImageIO.write(renderedImage,"png", file1);

                    File file = new File("warehousesChart.pdf");
                    PDDocument doc = PDDocument.load(file);

                    PDPage page = doc.getPage(0);

                    PDImageXObject pdImage =
                            PDImageXObject.createFromFile("chart.png",doc);

                    PDPageContentStream contents = new PDPageContentStream(doc, page);
                    contents.drawImage(pdImage, 100, 100);
                    contents.close();

                    doc.save("warehousesChart.pdf");
                    doc.close();
                    showAlert("Отчёт сохранён");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        });


        priceRealiseButton.setOnAction(event -> {
            OpenWindow(priceRealiseButton, "/fxml/employee/editPriseRealiseEmployee.fxml");
        });
    }

    private void setVisibleMetod(boolean visible){
        ratingTextNum.setVisible(visible);
        inputRatingNum.setVisible(visible);

        ratingTextPrice.setVisible(visible);
        inputRatingPrice.setVisible(visible);
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

    private void sendRequestAddRating(String login, int idGood, String ratingNum, String ratingPrice){
        String request = RequestHandler.requestAddRating(login, idGood, ratingNum, ratingPrice);
        MainApp.sendData(request);
    }

    private void sendRequestDeleteGood(int id){
        String request = RequestHandler.requestDeleteWarehouseGood(id);
        MainApp.sendData(request);
        goodsTable.getItems().clear();
        getGoods();
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
                good.setBarcode(Integer.parseInt(arrayUsersData[i++]));
                good.setProduct_name(arrayUsersData[i++]);
                good.setUnit_measurement(arrayUsersData[i++]);
                i++;
                good.setPrice(Float.parseFloat(arrayUsersData[i++]/*.replaceAll("руб", "")*/.trim()));
                good.setOccupied_space(Float.parseFloat(arrayUsersData[i++]));
                good.setQuantity(Integer.parseInt(arrayUsersData[i++]));
                good.setPriceRealise(Float.parseFloat(arrayUsersData[i++]/*.replaceAll("руб", "")*/.trim()));
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

    private void sendRequestGetListWarehouses(){
        String request = RequestHandler.requestGetListWarehouses();
        MainApp.sendData(request);
    }

    private void getWarehouses() {
        warehouses.clear();
        sendRequestGetListWarehouses();
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
                Warehouse warehouse = new Warehouse();
                warehouse.setId(Integer.valueOf(arrayUsersData[i++]));
                warehouse.setName(arrayUsersData[i++]);
                warehouse.setVolume(Integer.valueOf(arrayUsersData[i++]));
                warehouse.setIdBasket(Integer.valueOf(arrayUsersData[i++]));
                warehouse.setFullness(Integer.valueOf(arrayUsersData[i++]));
                warehouses.add(warehouse);
            }
        }
    }
}


