package com.example.finalproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.converter.DoubleStringConverter;

import java.net.URL;
import java.util.ResourceBundle;

import static com.example.finalproject.DBUtils.*;

public class ServicesController implements Initializable {

    @FXML
    private Button btn_home;

    @FXML
    private Button btn_customers;

    @FXML
    private Button btn_features;

    @FXML
    private Button btn_rooms;

    @FXML
    private Button btn_addService;

    @FXML
    private Button btn_delete;

    @FXML
    private Button btn_edit;

    @FXML
    private TableView<ServicesModule> tb_services;

    @FXML
    private TextField tf_serviceName;

    @FXML
    private TextField tf_unitPrice;

    @FXML
    private TableColumn<ServicesModule, String> tb_serviceName;

    @FXML
    private TableColumn<ServicesModule, Double> tb_unitPrice;

    ObservableList<ServicesModule> services = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        buttons();
        add();
        sendWithEnter();
        showServices();
        delete();
        update();

    }

    private void buttons() {
        btn_home.setOnAction(event -> DBUtils.changeScene(event, "home.fxml", "Home"));
        btn_customers.setOnAction(event -> DBUtils.changeScene(event, "customers.fxml", "Customers"));
        btn_features.setOnAction(event -> DBUtils.changeScene(event, "features.fxml", "Features"));
        btn_rooms.setOnAction(event -> DBUtils.changeScene(event, "rooms.fxml", "Rooms"));

        btn_edit.setOnAction(event -> {
            tb_services.setEditable(true);
            tb_services.getSelectionModel().setCellSelectionEnabled(true);
            tb_serviceName.setCellFactory(TextFieldTableCell.<ServicesModule>forTableColumn());
            tb_unitPrice.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Service Page Information");
            alert.setContentText("The services table can be edited");
            alert.show();
        });
    }

    private void delete() {
        tb_services.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                int selectedIdText = newSelection.getId();

                btn_delete.setOnAction(event -> {
                    DBUtils.deleteService(Integer.parseInt(String.valueOf(selectedIdText)));
                    showServices();
                });
            }
        });
    }

    private void update() {
        tb_serviceName.setOnEditCommit(event -> {
            String newValue = event.getNewValue();
            ServicesModule selectedItem = event.getRowValue();
            selectedItem.setServiceName(newValue);
            updateService(selectedItem);
        });
        tb_unitPrice.setOnEditCommit(event -> {
            Double newValue = event.getNewValue();
            ServicesModule selectedItem = event.getRowValue();
            selectedItem.setUnitPrice(newValue);
            updateService(selectedItem);
        });
    }

    private void sendWithEnter() {
        tf_unitPrice.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                    DBUtils.addService(tf_serviceName.getText(), Double.parseDouble(tf_unitPrice.getText()));
                    tf_serviceName.setText("");
                    tf_unitPrice.setText("");
                    showServices();
                }
            }
        });
    }

    private void add() {
        TextFormatter<Integer> unitPriceFormatter = DBUtils.createIntegerTextFormatter();
        tf_unitPrice.setTextFormatter(unitPriceFormatter);

        btn_addService.setOnAction(event -> {
            DBUtils.addService(tf_serviceName.getText(), Double.parseDouble(tf_unitPrice.getText()));
            tf_serviceName.setText("");
            tf_unitPrice.setText("");
            showServices();
        });
    }

    private void showServices() {
        tb_serviceName.setCellValueFactory(new PropertyValueFactory<>("serviceName"));
        tb_unitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));

        services = DBUtils.getServiceData();
        tb_services.setItems(services);
    }

}
