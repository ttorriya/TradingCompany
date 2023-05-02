package controllers.admin;

import classes.RequestHandler;
import classes.ResponseHandler;
import classes.User;
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

public class ManageUsersMenuController extends ControllerImpl {
    private ObservableList<User> users = FXCollections.observableArrayList();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button exitButton;

    @FXML
    private Button deleteUserButton;

    @FXML
    private Button addAccessButton;

    @FXML
    private Text name;

    @FXML
    private Text Surname;

    @FXML
    private MenuButton settingsMenu;

    @FXML
    private ScrollPane scrollpane;

    @FXML
    private TableView<User> usersTable;

    @FXML
    private TableColumn<User, Integer> id;

    @FXML
    private TableColumn<User, String> login;

    @FXML
    private TableColumn<User, String> password;

    @FXML
    private TableColumn<User, String> email;

    @FXML
    private TableColumn<User, String> phoneNumber;

    @FXML
    private TableColumn<User, String> firstname;

    @FXML
    private TableColumn<User, String> lastname;

    @FXML
    private TableColumn<User, String> patronymic;

    @FXML
    private TableColumn<User, String> accessRights;

    @FXML
    private TableColumn<User, String> confirmAccess;

    @FXML
    private Button todoButton;

    @FXML
    void initialize() {
        String style = addAccessButton.getStyle();

        name.setText(ResponseHandler.name);
        Surname.setText(ResponseHandler.surname);

        getUsers();

        id.setCellValueFactory(new PropertyValueFactory<User, Integer>("id"));
        login.setCellValueFactory(new PropertyValueFactory<User, String>("login"));
        password.setCellValueFactory(new PropertyValueFactory<User, String>("password"));
        email.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
        phoneNumber.setCellValueFactory(new PropertyValueFactory<User, String>("phoneNumber"));
        firstname.setCellValueFactory(new PropertyValueFactory<User, String>("firstname"));
        lastname.setCellValueFactory(new PropertyValueFactory<User, String>("lastname"));
        patronymic.setCellValueFactory(new PropertyValueFactory<User, String>("patronymic"));
        accessRights.setCellValueFactory(new PropertyValueFactory<User, String>("accessRights"));
        confirmAccess.setCellValueFactory(new PropertyValueFactory<User, String>("confirmAccess"));


        exitButton.setOnAction(event -> {
            OpenWindow(exitButton, "/fxml/admin/mainMenuAdmin.fxml");
        });

        addAccessButton.setOnAction(event -> {
            deleteUserButton.setStyle(style);
            addAccessButton.setStyle("-fx-background-color: #3c4c5b; opacity: 0.3");
            todoButton.setText("Подтвердить");

            usersTable.setItems(users);

            usersTable.setRowFactory(tv -> {

                TableRow<User> row = new TableRow<>();
                row.setOnMouseClicked(event1 -> {
                    if (! row.isEmpty() && event1.getButton()== MouseButton.PRIMARY
                            && event1.getClickCount() == 1) {
                        User clickedRow = row.getItem();
                        todoButton.setOnAction(event2 -> {
                            sendRequestModifyAccess(clickedRow.getLogin());
                            OpenWindow(todoButton, "/fxml/admin/manageUsersMenu.fxml");
                            showAlert("Выполнено");

                        });
                    }
                });
                return row;
            });
        });

        deleteUserButton.setOnAction(event -> {
            addAccessButton.setStyle(style);
            deleteUserButton.setStyle("-fx-background-color: #3c4c5b; opacity: 0.3");
            todoButton.setText("Удалить");

            usersTable.setItems(users);

            usersTable.setRowFactory(tv2 -> {

                TableRow<User> row = new TableRow<>();
                row.setOnMouseClicked(event1 -> {
                    if (! row.isEmpty() && event1.getButton()== MouseButton.PRIMARY
                            && event1.getClickCount() == 1) {
                        User clickedRow = row.getItem();
                        todoButton.setOnAction(event2 -> {
                            sendRequestDeleteUser(clickedRow.getLogin());
                            OpenWindow(todoButton, "/fxml/admin/manageUsersMenu.fxml");
                            showAlert("Выполнено");
                        });
                    }
                });
                return row;
            });
        });


    }

    private void sendRequestGetListUsers() {
        String request = RequestHandler.requestGetListUsers();
        MainApp.sendData(request);
    }

    private void sendRequestDeleteUser(String login) {
        String request = RequestHandler.requestDeleteUser(login);
        MainApp.sendData(request);
        getUsers();
        usersTable.getItems().clear();
    }

    private void sendRequestModifyAccess(String login) {
        String request = RequestHandler.requestModifyAccess(login);
        MainApp.sendData(request);
        getUsers();
        usersTable.getItems().clear();
    }

    private void getUsers(){
        sendRequestGetListUsers();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(ResponseHandler.massage.equals("")){
            showAlert("Список пуст!");
        }
        else{
            String[] arrayUsersData = ResponseHandler.massage.split(" ");

            int i = 0;
            while(i < arrayUsersData.length){
                User user = new User();
                user.setId(Integer.valueOf(arrayUsersData[i++]));
                user.setLogin(arrayUsersData[i++]);
                user.setPassword(arrayUsersData[i++]);
                user.setEmail(arrayUsersData[i++]);
                user.setPhoneNumber(arrayUsersData[i++]);
                user.setFirstname(arrayUsersData[i++]);
                user.setLastname(arrayUsersData[i++]);
                user.setPatronymic(arrayUsersData[i++]);
                user.setAccessRights(arrayUsersData[i++]);
                if(arrayUsersData[i++].equals("ff")){
                    user.setConfirmAccess("-");
                }
                else{
                    user.setConfirmAccess("+");
                }
                users.add(user);
            }
        }
    }

}
