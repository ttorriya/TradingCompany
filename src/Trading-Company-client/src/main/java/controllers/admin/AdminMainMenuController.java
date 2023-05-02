package controllers.admin;

import java.awt.image.RenderedImage;
import java.io.*;
import java.net.URL;
import javax.imageio.ImageIO;

import controllers.controller_service.ControllerImpl;
import javafx.embed.swing.SwingFXUtils;

import java.util.Calendar;
import java.util.ResourceBundle;

import classes.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.image.WritableImage;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.text.Text;
import org.client.MainApp;
import org.apache.pdfbox.pdmodel.*;

public class AdminMainMenuController extends ControllerImpl {
    private String loginUser = ResponseHandler.login;
    private ObservableList<Warehouse> warehouses = FXCollections.observableArrayList();

    @FXML    private ResourceBundle resources;
    @FXML    private URL location;
    @FXML    private Text surname;
    @FXML    private Text Name;
    @FXML    private MenuButton settingsMenu;
    @FXML    private Button manageUsers;
    @FXML    private Button deleteWarehouseButton;
    @FXML    private Button editWarehouseButton;
    @FXML    private Button addWarehouseButton;
    @FXML    private Button exitButton;
    @FXML    private ScrollPane scrollpane;
    @FXML    private TableView<Warehouse> tableWarehouse;
    @FXML    private TableColumn<Warehouse, Integer> id;
    @FXML    private TableColumn<Warehouse, String> name;
    @FXML    private TableColumn<Warehouse, Integer> volume;
    @FXML    private TableColumn<Warehouse, Integer> fullness;
    @FXML    private Button todoButton;
    @FXML    private Button report2Button;
    @FXML    private Button report1Button;
    @FXML    private Button lineChartButton;
    @FXML    private LineChart<String, Integer> lineChart;
    @FXML    private TextArea textArea;

    @FXML
    void initialize() {
        textArea.setVisible(true);
        scrollpane.setVisible(false);
        lineChart.setVisible(false);
        String style = addWarehouseButton.getStyle();
        getWarehouses();
        todoButton.setVisible(false);
        tableWarehouse.setVisible(false);
        sendRequestGetNameSurname(loginUser);
        Name.setText(ResponseHandler.name);
        surname.setText(ResponseHandler.surname);

        id.setCellValueFactory(new PropertyValueFactory<Warehouse, Integer>("id"));
        name.setCellValueFactory(new PropertyValueFactory<Warehouse, String>("name"));
        volume.setCellValueFactory(new PropertyValueFactory<Warehouse, Integer>("volume"));
        fullness.setCellValueFactory(new PropertyValueFactory<Warehouse, Integer>("fullness"));

        exitButton.setOnAction(event -> {
            OpenWindow(exitButton, "/fxml/authentication/authMenu.fxml");
        });

        manageUsers.setOnAction(event -> {
            OpenWindow(manageUsers, "/fxml/admin/manageUsersMenu.fxml");
        });

        addWarehouseButton.setOnAction(event -> {
            ResponseHandler.massage = loginUser;
            OpenWindow(addWarehouseButton, "/fxml/admin/addMenuWarehouse.fxml");
        });

        editWarehouseButton.setOnAction(event -> {
            textArea.setVisible(false);
            scrollpane.setVisible(true);
            lineChartButton.setStyle(style);
            editWarehouseButton.setStyle("-fx-background-color: #3c4c5b; opacity: 0.3");
            addWarehouseButton.setStyle(style);
            manageUsers.setStyle(style);
            deleteWarehouseButton.setStyle(style);
            todoButton.setText("Изменить");
            todoButton.setVisible(true);
            tableWarehouse.setVisible(true);
            if(warehouses.isEmpty()){
                showAlert("Список пуст!");
            }
            else {
                tableWarehouse.setItems(warehouses);
                tableWarehouse.setRowFactory(tv2 -> {

                    TableRow<Warehouse> row = new TableRow<>();
                    row.setOnMouseClicked(event1 -> {
                        if (! row.isEmpty() && event1.getButton()== MouseButton.PRIMARY
                                && event1.getClickCount() == 1) {
                            Warehouse clickedRow = row.getItem();
                            todoButton.setOnAction(event2 -> {
                                ResponseHandler.massage = clickedRow.getId() + "%" + clickedRow.getName() + "%"
                                        + clickedRow.getVolume() + "%" + clickedRow.getFullness() + "%" + loginUser;
                                OpenWindow(exitButton, "/fxml/admin/editMenuWarehouse.fxml");
                            });
                        }
                    });
                    return row;
                });
            }
        });

        deleteWarehouseButton.setOnAction(event -> {
            scrollpane.setVisible(true);
            lineChartButton.setStyle(style);
            deleteWarehouseButton.setStyle("-fx-background-color: #3c4c5b; opacity: 0.3");
            addWarehouseButton.setStyle(style);
            manageUsers.setStyle(style);
            editWarehouseButton.setStyle(style);
            todoButton.setText("Удалить");
            todoButton.setVisible(true);
            tableWarehouse.setVisible(true);
            if(warehouses.isEmpty()){
                showAlert("Список пуст!");
            }
            else {
                tableWarehouse.setItems(warehouses);
                tableWarehouse.setRowFactory(tv2 -> {

                    TableRow<Warehouse> row = new TableRow<>();
                    row.setOnMouseClicked(event1 -> {
                        if (! row.isEmpty() && event1.getButton()== MouseButton.PRIMARY
                                && event1.getClickCount() == 1) {
                            Warehouse clickedRow = row.getItem();
                            todoButton.setOnAction(event2 -> {
                                if (clickedRow.getFullness() == 0) {
                                    sendRequestDeleteWarehouse(clickedRow.getId());
                                    OpenWindow(todoButton, "/fxml/admin/mainMenuAdmin.fxml");
                                    showAlert("Выполнено");
                                } else {
                                    showAlert("Заполненный склад невозможно удалить!");
                                }
                            });
                        }
                    });
                    return row;
                });
            }
        });

        lineChartButton.setOnAction(event -> {
            textArea.setVisible(false);
            todoButton.setVisible(true);
            todoButton.setText("Сохранение отчёта");
            scrollpane.setVisible(false);
            lineChart.setVisible(true);
            lineChartButton.setStyle("-fx-background-color: #3c4c5b; opacity: 0.3");
            report1Button.setStyle(style);
            editWarehouseButton.setStyle(style);
            addWarehouseButton.setStyle(style);
            manageUsers.setStyle(style);
            deleteWarehouseButton.setStyle(style);

            String[] array = getLineChartInfo().split("%");

            lineChart.getXAxis().setLabel("Месяц");

            lineChart.getYAxis().setLabel("Количество поставленного товара");
            lineChart.getXAxis().setLabel("Месяц");

            lineChart.setTitle("Количество поставленного товара за последние 6 месяцев");

            Calendar cal = Calendar.getInstance();

            XYChart.Series series = new XYChart.Series();
            series.setName("Количество товара");

            series.getData().add(new XYChart.Data(getMonth(cal.get(Calendar.MONTH) - 5), Integer.valueOf(array[5])));
            series.getData().add(new XYChart.Data(getMonth(cal.get(Calendar.MONTH) - 4), Integer.valueOf(array[4])));
            series.getData().add(new XYChart.Data(getMonth(cal.get(Calendar.MONTH) - 3), Integer.valueOf(array[3])));
            series.getData().add(new XYChart.Data(getMonth(cal.get(Calendar.MONTH) - 2), Integer.valueOf(array[2])));
            series.getData().add(new XYChart.Data(getMonth(cal.get(Calendar.MONTH) - 1), Integer.valueOf(array[1])));
            series.getData().add(new XYChart.Data(getMonth(cal.get(Calendar.MONTH)), Integer.valueOf(array[0])));

            lineChart.getData().clear();
            lineChart.getData().add(series);

            Axis xAxis = new CategoryAxis();
            xAxis.setLabel("Месяц");

            Axis yAxis = new NumberAxis();
            yAxis.setLabel("Количество поставленного товара");

            LineChart<String, Number> xyChart = new LineChart<>(xAxis, yAxis);
            xyChart.setAnimated(false);
            xyChart.setPrefWidth(662);
            xyChart.setPrefHeight(439);
            Group root = new Group(xyChart);
            Scene scene1 = new Scene(root, 662, 439);
            xyChart.setTitle("Количество поставленного товара за последние 6 месяцев");


            XYChart.Series<String, Number> series1 = new XYChart.Series();
            series1.setName("Количество товара");

            series1.getData().add(new XYChart.Data(getMonth(cal.get(Calendar.MONTH) - 5), Integer.valueOf(array[5])));
            series1.getData().add(new XYChart.Data(getMonth(cal.get(Calendar.MONTH) - 4), Integer.valueOf(array[4])));
            series1.getData().add(new XYChart.Data(getMonth(cal.get(Calendar.MONTH) - 3), Integer.valueOf(array[3])));
            series1.getData().add(new XYChart.Data(getMonth(cal.get(Calendar.MONTH) - 2), Integer.valueOf(array[2])));
            series1.getData().add(new XYChart.Data(getMonth(cal.get(Calendar.MONTH) - 1), Integer.valueOf(array[1])));
            series1.getData().add(new XYChart.Data(getMonth(cal.get(Calendar.MONTH)), Integer.valueOf(array[0])));

            xyChart.getData().clear();
            xyChart.getData().add(series1);


            todoButton.setOnAction(event1 -> {
                File file1 = new File("saved.png");
                WritableImage img = new WritableImage(662,
                        439);
                scene1.snapshot(img);
                RenderedImage renderedImage = SwingFXUtils.fromFXImage(img, null);
                try {
                    ImageIO.write(renderedImage,"png", file1);

                    File file = new File("deliveryChart.pdf");
                    PDDocument doc = PDDocument.load(file);

                    PDPage page = doc.getPage(0);

                    PDImageXObject pdImage =
                            PDImageXObject.createFromFile("saved.png",doc);

                    PDPageContentStream contents = new PDPageContentStream(doc, page);
                    contents.drawImage(pdImage, 0, 100);
                    contents.close();

                    doc.save("deliveryChart.pdf");
                    doc.close();
                    showAlert("Отчёт сохранён");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        });

        report1Button.setOnAction(event -> {
            String str = getReportGood();
            textArea.setText(str);
            textArea.setVisible(true);
            textArea.setEditable(false);
            report1Button.setStyle("-fx-background-color: #3c4c5b; opacity: 0.3");
            lineChartButton.setStyle(style);
            editWarehouseButton.setStyle(style);
            addWarehouseButton.setStyle(style);
            manageUsers.setStyle(style);
            deleteWarehouseButton.setStyle(style);

            todoButton.setText("Сохранить отчёт");
            todoButton.setVisible(true);
            todoButton.setOnAction(event1 ->{
                File file = new File("goodsReport.txt");
                try {
                    file.createNewFile();
                    FileWriter writer = new FileWriter(file);
                    writer.write(str);
                    showAlert("Выполнено");
                    writer.close();
                }
                catch (IOException e){}
            });
        });
    }

    private void sendRequestGetLineChartInfo(){
        String request = RequestHandler.requestGetLineChartInfo();
        MainApp.sendData(request);
    }

    private String getLineChartInfo(){
        sendRequestGetLineChartInfo();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(ResponseHandler.massage.trim().equals("")){
            showAlert("Данные для отображения отсутствуют!");
        }

        return ResponseHandler.massage;
    }

    private static String getMonth(int month){
        if(month < 0 ) month += 12;
        switch(month){
            case 0:return "Январь";
            case 1:return "Февраль";
            case 2:return "Март";
            case 3:return "Апрель";
            case 4:return "Май";
            case 5:return "Июнь";
            case 6:return "Июль";
            case 7:return "Август";
            case 8:return "Сентябрь";
            case 9:return "Октябрь";
            case 10:return "Ноябрь";
            case 11:return "Декабрь";
        }
        return "";
    }

    private void sendRequestDeleteWarehouse(int id){
        String request = RequestHandler.requestDeleteWarehouse(id);
        MainApp.sendData(request);
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

    private void sendRequestGetReportGood(){
        String request = RequestHandler.requestGetReportGood();
        MainApp.sendData(request);
    }

    private String getReportGood() {
        sendRequestGetReportGood();
        String str = "";
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(ResponseHandler.massage.trim().equals("")){}
        else {
            String[] arrayUsersData = ResponseHandler.massage.split("#");
            int sumNum = 0;
            float sumPrise = 0;
            for(int i = 0; i < arrayUsersData.length; i++){
                String[] array = arrayUsersData[i].split("%");
                for(int j = 0; j < array.length - 1;j++){
                    int tempNum = 0;
                    if (j == 0) str += "\t" + array[j] + "\n";
                    else j--;
                    str += "ID товара: " + array[++j] + "\n";
                    str += "Количество товара на складе: " + array[++j] + "\n";
                    sumNum += Integer.valueOf(array[j]);
                    tempNum = Integer.valueOf(array[j]);
                    str += "Наименование товара: " + array[++j] + "\n";
                    str += "Закупочная цена товара: " + array[++j] + "\n";
                    sumPrise += tempNum * Float.valueOf(array[j].replaceAll("руб","").trim());
                    str += "Занимаемое место: " + array[++j] + "\n\n";
                }
                str += "\n\n";
            }
            str += "Суммарное количество товаров на складах компании: " + sumNum + "\n";
            str += "Суммарное стоимость товаров по закупочной цене: " + sumPrise + "\n";
        }
        return str;
    }

    /*private static XYDataset createDataset(String[] array){
        Calendar cal = Calendar.getInstance();
        XYSeries series = new XYSeries("First");

        series.add(new XYChart((cal.get(Calendar.MONTH) - 5), Integer.valueOf(array[5])));
        series.add(new XYChart.Data((cal.get(Calendar.MONTH) - 4), Integer.valueOf(array[4])));
        series.add(new XYChart.Data((cal.get(Calendar.MONTH) - 3), Integer.valueOf(array[3])));
        series.add(new XYChart.Data((cal.get(Calendar.MONTH) - 2), Integer.valueOf(array[2])));
        series.add(new XYChart.Data((cal.get(Calendar.MONTH) - 1), Integer.valueOf(array[1])));
        series.add(new XYChart.Data((cal.get(Calendar.MONTH)), Integer.valueOf(array[0])));



        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);

        return dataset;
    }

    private static JFreeChart createChart(XYDataset dataset) {

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Chart Demo",      // chart title
                "X",                      // x axis label
                "Y",                      // y axis label
                dataset,                  // data
                PlotOrientation.VERTICAL,
                true,                     // include legend
                true,                     // tooltips
                false                     // urls
        );


        //chart.setBackgroundPaint(Color.WHITE);

        final XYPlot plot = chart.getXYPlot();
        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();


        //renderer.setSeriesPaint(0, Color.RED);
        //renderer.setSeriesStroke(0, new BasicStroke(2.0f));

        plot.setRenderer(renderer);
        //plot.setBackgroundPaint(Color.white);

        plot.setRangeGridlinesVisible(true);
        //plot.setRangeGridlinePaint(Color.BLACK);

        plot.setDomainGridlinesVisible(true);
        //plot.setDomainGridlinePaint(Color.BLACK);

        chart.getLegend().setFrame(BlockBorder.NONE);

        chart.setTitle(new TextTitle("Average Salary per Age"));

        return chart;
    }*/
}
