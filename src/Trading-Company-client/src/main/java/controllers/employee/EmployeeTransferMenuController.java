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

public class EmployeeTransferMenuController extends ControllerImpl {
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
    private Button exitButton;

    @FXML
    private Button transferButton;

    @FXML
    private ScrollPane scrollpane;

    @FXML
    private TableView<WarehouseGood> goodsTable;

    @FXML
    private TableColumn<WarehouseGood, Integer> barcode;

    @FXML
    private TableColumn<WarehouseGood, String> product_name;

    @FXML
    private TableColumn<WarehouseGood, String> unit_measurement;

    @FXML
    private TableColumn<WarehouseGood, Float> price;

    @FXML
    private TableColumn<WarehouseGood, Float> occupied_space;

    @FXML
    private TableColumn<WarehouseGood, Integer> number;

    @FXML
    private Button todoButton;

    @FXML
    private TextField numberGoods;

    @FXML
    private ComboBox<String> fromListWarehouse;

    @FXML
    private ComboBox<String> toListWarehouse;

    @FXML
    void initialize() {
        name.setText(ResponseHandler.name);
        surname.setText(ResponseHandler.surname);
        getGoods();
        getWarehouses();
        initComboBoxValue(fromListWarehouse);
        initComboBoxValue(toListWarehouse);
        todoButton.setVisible(false);
        toListWarehouse.setVisible(false);
        numberGoods.setVisible(false);

        barcode.setCellValueFactory(new PropertyValueFactory<WarehouseGood, Integer>("barcode"));
        product_name.setCellValueFactory(new PropertyValueFactory<WarehouseGood, String>("product_name"));
        unit_measurement.setCellValueFactory(new PropertyValueFactory<WarehouseGood, String>("unit_measurement"));
        price.setCellValueFactory(new PropertyValueFactory<WarehouseGood, Float>("price"));
        occupied_space.setCellValueFactory(new PropertyValueFactory<WarehouseGood, Float>("occupied_space"));
        number.setCellValueFactory(new PropertyValueFactory<WarehouseGood, Integer>("number"));


        exitButton.setOnAction(event -> {
            OpenWindow(exitButton, "/fxml/employee/mainMenuEmployee.fxml");
        });

        fromListWarehouse.setOnAction(event -> {
            goodsTable.setItems(getListGoodsOnWarehouse(
                    Integer.parseInt(fromListWarehouse.getSelectionModel().getSelectedItem().split(" ")[0])));

            goodsTable.setRowFactory(tv -> {
                todoButton.setVisible(true);
                toListWarehouse.setVisible(true);
                numberGoods.setVisible(true);
                TableRow<WarehouseGood> row = new TableRow<>();
                row.setOnMouseClicked(event1 -> {
                    if (!row.isEmpty() && event1.getButton() == MouseButton.PRIMARY
                            && event1.getClickCount() == 1) {
                        WarehouseGood clickedRow = row.getItem();
                        todoButton.setOnAction(event2 -> {
                            if (!toListWarehouse.getSelectionModel().getSelectedItem().isEmpty()
                                    && !numberGoods.getText().equals("")) {
                                try {
                                    Integer.valueOf(numberGoods.getText());
                                    if (Integer.valueOf(numberGoods.getText().trim()) > clickedRow.getNumber()
                                            || Integer.valueOf(numberGoods.getText().trim()) <= 0) {
                                        showAlert("Неверное значение количества");
                                    } else {
                                        sendRequestTransferGoods(
                                                fromListWarehouse.getSelectionModel().getSelectedItem().split(" ")[0],
                                                toListWarehouse.getSelectionModel().getSelectedItem().split(" ")[0],
                                                clickedRow.getBarcode(), numberGoods.getText().trim());
                                        showAlert("Выполнено");
                                        OpenWindow(exitButton, "/fxml/employee/mainMenuEmployee.fxml");
                                    }
                                } catch (NumberFormatException e) {
                                    showAlert("Неверный формат!");
                                }
                            }
                        });
                    }
                });
                return row;
            });
        });
    }

    private void sendRequestTransferGoods(String idFromWarehouse, String idToWarehouse, int idGood, String numberGoods) {
        String request = RequestHandler.requestTransferGoods(idFromWarehouse, idToWarehouse, idGood, numberGoods);
        MainApp.sendData(request);
    }

    private ObservableList<WarehouseGood> getListGoodsOnWarehouse(int idWarehouse) {
        ObservableList<WarehouseGood> warehouseGoods = FXCollections.observableArrayList();
        for (int i = 0; i < warehouses.size(); i++) {
            if (warehouses.get(i).getId() == idWarehouse) {
                sendRequestGetListGoodsWarehouse(warehouses.get(i).getIdBasket());
                i = warehouses.size();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (ResponseHandler.massage.trim().equals("")) {
                } else {
                    String[] arrayUsersData = ResponseHandler.massage.split("%");

                    for (int y = 0; y < arrayUsersData.length; y++) {
                        for (int k = 0; k < goods.size(); k++) {
                                if (Integer.parseInt(arrayUsersData[y]) == goods.get(k).getBarcode()) {
                                    WarehouseGood warehouseGood = new WarehouseGood();

                                    warehouseGood.setBarcode(goods.get(k).getBarcode());
                                    warehouseGood.setQuantity(goods.get(k).getQuantity());
                                    warehouseGood.setOccupied_space(goods.get(k).getOccupied_space());
                                    warehouseGood.setPrice(goods.get(k).getPrice());
                                    warehouseGood.setProduct_name(goods.get(k).getProduct_name());
                                    warehouseGood.setUnit_measurement(goods.get(k).getUnit_measurement());

                                    warehouseGood.setNumber(Integer.parseInt(arrayUsersData[y + 1]));
                                    warehouseGoods.add(warehouseGood);
                                    y++;
                                }
                        }
                    }
                }
            }
        }
        return warehouseGoods;
    }

    private void sendRequestGetListGoodsWarehouse(int idBasket) {
        String request = RequestHandler.requestGetListGoodsWarehouse(idBasket);
        MainApp.sendData(request);
    }

    private void initComboBoxValue(ComboBox<String> comboBox) {
        ObservableList<String> accessRights =
                FXCollections.observableArrayList();
        for (Warehouse warehouse : warehouses) {
            accessRights.add(warehouse.getId() + " " + warehouse.getName() + " ("
                    + warehouse.getFullness() + "/" + warehouse.getVolume() + ")");
        }
        comboBox.setItems(accessRights);
    }

    private void sendRequestGetListWarehouses() {
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
        if (ResponseHandler.massage.trim().equals("")) {
        } else {
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

    /*private void sendRequestAddDelivery(int barcode, String idWarehouse, String number, float space){
        String request = RequestHandler.requestAddDelivery(barcode + "%" + idWarehouse + "%" + number + "%" + space);
        MainApp.sendData(request);
    }*/

    private void sendRequestGetListGoodsAll() {
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
        if (ResponseHandler.massage.trim().equals("")) {
        } else {
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

}
