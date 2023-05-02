package controllers.employee;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import classes.Good;
import classes.RequestHandler;
import classes.ResponseHandler;
import controllers.controller_service.ControllerImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import org.client.MainApp;

public class MetodViewController extends ControllerImpl {
    private String loginUser = ResponseHandler.login;
    private ObservableList<Good> goods = FXCollections.observableArrayList();
    private ArrayList<String> finish;
    private ArrayList<String> idgoods = new ArrayList<>(Arrays.asList(ResponseHandler.massage.split("%")));

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Text surname;

    @FXML
    private Text Name;

    @FXML
    private MenuButton settingsMenu;

    @FXML
    private Button exitButton;

    @FXML
    private Button metodButton;

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
    private TableColumn<Good, Integer> number;

    @FXML
    void initialize() {
        Name.setText(ResponseHandler.name);
        surname.setText(ResponseHandler.surname);
        finish = new ArrayList<>(Arrays.asList(ResponseHandler.massage.split("&")[1].split("%")));
        idgoods = new ArrayList<>(Arrays.asList(ResponseHandler.massage.split("&")[0].split("%")));
        getGoods();

        barcode.setCellValueFactory(new PropertyValueFactory<Good, Integer>("barcode"));
        product_name.setCellValueFactory(new PropertyValueFactory<Good, String>("product_name"));
        number.setCellValueFactory(new PropertyValueFactory<Good, Integer>("quantity"));
        unit_measurement.setCellValueFactory(new PropertyValueFactory<Good, String>("unit_measurement"));
        price.setCellValueFactory(new PropertyValueFactory<Good, Float>("price"));
        occupied_space.setCellValueFactory(new PropertyValueFactory<Good, Float>("occupied_space"));
        number.setCellValueFactory(new PropertyValueFactory<Good, Integer>("number"));

        goodsTable.setItems(getGoodsWithRating());

    }

    private ObservableList<Good> getGoodsWithRating(){
        ObservableList<Good> temp = FXCollections.observableArrayList();
        int size = finish.size();;
        for(int j = 0; j < size;j++){
            float max = 0;
            int id = 0;
            for(int i = 0; i < finish.size(); i++){
                if(max < Float.valueOf(finish.get(i))){
                    max = Float.valueOf(finish.get(i));
                    id = i;
                }
            }
            //----------------------------------
            temp.add(goods.get(Integer.valueOf(idgoods.get(id))));
            finish.remove(id);
        }
        return temp;
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
}
