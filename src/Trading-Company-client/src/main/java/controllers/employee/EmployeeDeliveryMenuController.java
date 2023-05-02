package controllers.employee;

import java.net.URL;
import java.util.ResourceBundle;

import classes.*;
import controllers.controller_service.ControllerImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.text.Text;
import org.client.MainApp;

public class EmployeeDeliveryMenuController extends ControllerImpl {
    private String loginUser = ResponseHandler.login;
    private ObservableList<Good> goods = FXCollections.observableArrayList();
    private ObservableList<Supplier> suppliers = FXCollections.observableArrayList();
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
    private Button todoButton;

    @FXML
    private ScrollPane scrollpane1;

    @FXML
    private TableView<Supplier> supplierTable;

    @FXML
    private TableColumn<Supplier, Integer> id;

    @FXML
    private TableColumn<Supplier, String> org_name;

    @FXML
    private TableColumn<Supplier, String> city;

    @FXML
    private TableColumn<Supplier, String> country;

    @FXML
    private TableColumn<Supplier, String> address;

    @FXML
    private TextField numberGoods;

    @FXML
    private ComboBox<String> listWarehouse;

    @FXML
    void initialize() {
        name.setText(ResponseHandler.name);
        surname.setText(ResponseHandler.surname);
        supplierTable.setVisible(false);
        scrollpane1.setVisible(false);
        todoButton.setText("Добавить");
        getGoods();
        getWarehouses();
        initComboBoxValue(listWarehouse);

        barcode.setCellValueFactory(new PropertyValueFactory<Good, Integer>("barcode"));
        product_name.setCellValueFactory(new PropertyValueFactory<Good, String>("product_name"));
        unit_measurement.setCellValueFactory(new PropertyValueFactory<Good, String>("unit_measurement"));
        price.setCellValueFactory(new PropertyValueFactory<Good, Float>("price"));
        occupied_space.setCellValueFactory(new PropertyValueFactory<Good, Float>("occupied_space"));

        id.setCellValueFactory(new PropertyValueFactory<Supplier, Integer>("id"));
        org_name.setCellValueFactory(new PropertyValueFactory<Supplier, String>("org_name"));
        city.setCellValueFactory(new PropertyValueFactory<Supplier, String>("city"));
        country.setCellValueFactory(new PropertyValueFactory<Supplier, String>("country"));
        address.setCellValueFactory(new PropertyValueFactory<Supplier, String>("address"));

        goodsTable.setItems(goods);

        exitButton.setOnAction(event -> {
            OpenWindow(exitButton, "/fxml/employee/mainMenuEmployee.fxml");
            ResponseHandler.name = name.getText();
            ResponseHandler.surname = surname.getText();
        });




        goodsTable.setRowFactory(tv -> {
            TableRow<Good> row = new TableRow<>();
            row.setOnMouseClicked(event1 -> {
                if (!row.isEmpty() && event1.getButton() == MouseButton.PRIMARY
                        && event1.getClickCount() == 1) {
                    Good clickedRow = row.getItem();

                    getSuppliers(clickedRow.getBarcode());
                    supplierTable.setItems(suppliers);
                    scrollpane1.setVisible(true);
                    supplierTable.setVisible(true);

                    todoButton.setOnAction(event2 -> {
                        if (!numberGoods.getText().equals("") && !listWarehouse.getSelectionModel().getSelectedItem().equals(""))
                            sendRequestAddDelivery(clickedRow.getBarcode(),
                                    listWarehouse.getSelectionModel().getSelectedItem().split(" ")[0], numberGoods.getText(), clickedRow.getOccupied_space());
                        OpenWindow(todoButton, "/fxml/employee/deliveryMenuEmployee.fxml");
                        showAlert("Выполнено");
                    });
                }
            });
            return row;
        });

    }

    private void initComboBoxValue(ComboBox<String> comboBox){
        ObservableList<String> accessRights =
                FXCollections.observableArrayList();
        for(int i = 0; i < warehouses.size(); i++){
            accessRights.add(warehouses.get(i).getId() + " " + warehouses.get(i).getName() + " ("
                    + warehouses.get(i).getFullness() + "/" + warehouses.get(i).getVolume() + ")");
        }
        comboBox.setItems(accessRights);
    }

    private void sendRequestGetListWarehouses(){
        String request = RequestHandler.requestGetListWarehouses();
        MainApp.sendData(request);
    }

    private void getWarehouses() {
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

    private void sendRequestAddDelivery(int barcode, String idWarehouse, String number, float space){
        String request = RequestHandler.requestAddDelivery(barcode + "%" + idWarehouse + "%" + number + "%" + space);
        MainApp.sendData(request);
    }

    private void sendRequestGetListGoodsAll(){
        String request = RequestHandler.requestGetListGoodsAll();
        MainApp.sendData(request);
    }

    private void getGoods() {
        sendRequestGetListGoodsAll();

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

    private void sendRequestGetListSuppliers(int barcode){
        String request = RequestHandler.requestGetListSuppliers(barcode);
        MainApp.sendData(request);
    }

    private void getSuppliers(int barcode){
        sendRequestGetListSuppliers(barcode);

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
                Supplier good = new Supplier();
                good.setId(Integer.valueOf(arrayUsersData[i++]));
                good.setOrg_name(arrayUsersData[i++]);
                good.setCity(arrayUsersData[i++]);
                good.setCountry(arrayUsersData[i++]);
                good.setAddress(arrayUsersData[i++]);
                good.setId_basket(Integer.valueOf(arrayUsersData[i++]));
                suppliers.add(good);
            }
        }
    }
}