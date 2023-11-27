package com.example.finalproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.net.URL;
import java.util.ResourceBundle;

import static com.example.finalproject.DBUtils.updateRoom;

public class RoomsController implements Initializable {

    @FXML
    private Button btn_home;

    @FXML
    private Button btn_customers;

    @FXML
    private Button btn_features;

    @FXML
    private Button btn_services;

    @FXML
    private Button btn_add;

    @FXML
    private Button btn_delete;

    @FXML
    private Button btn_edit;

    @FXML
    private Button btn_addRoomType;

    @FXML
    private ChoiceBox<RoomTypeModule> cb_roomTypes;

    @FXML
    private TextField tf_roomNumber;

    @FXML
    private TextField tf_capacity;

    @FXML
    private TextField tf_price;

    @FXML
    private TableView<RoomsModule> tb_rooms;

    @FXML
    private TableColumn<RoomsModule, String> tb_roomName;

    @FXML
    private TableColumn<RoomsModule, Integer> tb_capacity;

    @FXML
    private TableColumn<RoomsModule, Double> tb_price;

    @FXML
    private ListView<FeaturesModule> list_extraFeatures;

    @FXML
    private TableColumn<FeaturesModule, String> tb_features;

    ObservableList<RoomsModule> rooms = FXCollections.observableArrayList();
    ObservableList<RoomTypeModule> roomTypes;
    ObservableList<FeaturesModule> features;
    private int selectedRoomTypeId;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buttons();
        pullRoomType();
        add();
        showFeatures();
        showRoomData();
        delete();
        update();

        btn_addRoomType.setOnAction(event -> openRoomTypePopup());
    }

    private void buttons() {
        btn_home.setOnAction(event -> DBUtils.changeScene(event, "home.fxml", "Home"));
        btn_customers.setOnAction(event -> DBUtils.changeScene(event, "customers.fxml", "Customers"));
        btn_features.setOnAction(event -> DBUtils.changeScene(event, "features.fxml", "Features"));
        btn_services.setOnAction(event -> DBUtils.changeScene(event, "services.fxml", "Services"));

        btn_edit.setOnAction(event -> {
            editable();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Room Page Information");
            alert.setContentText("The rooms table can be edited");
            alert.show();
        });
    }


    @FXML
    private void openRoomTypePopup() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("roomTypePopup.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add Room Type");
            stage.setScene(new Scene(root, 400, 450));

            RoomTypeController controller = loader.getController();
            controller.setStage(stage);

            stage.showAndWait();
            cb_roomTypes.getItems().clear();
            pullRoomType();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void editable() {
        tb_rooms.setEditable(true);
        tb_rooms.getSelectionModel().setCellSelectionEnabled(true);
        tb_roomName.setCellFactory(TextFieldTableCell.<RoomsModule>forTableColumn());
        tb_capacity.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        tb_price.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        tb_features.setCellFactory(TextFieldTableCell.forTableColumn());
    }

    private void update() {
        tb_roomName.setOnEditCommit(event -> {
            String newValue = event.getNewValue();
            RoomsModule selectedItem = event.getRowValue();
            selectedItem.setRoomName(newValue);
            updateRoom(selectedItem);
        });

        tb_capacity.setOnEditCommit(event -> {
            int newValue = event.getNewValue();
            RoomsModule selectedItem = event.getRowValue();
            selectedItem.setCapacity(newValue);
            updateRoom(selectedItem);
        });

        tb_price.setOnEditCommit(event -> {
            double newValue = event.getNewValue();
            RoomsModule selectedItem = event.getRowValue();
            selectedItem.setPrice(newValue);
            updateRoom(selectedItem);
        });
    }

    private void pullRoomType() {
        roomTypes = DBUtils.getRoomType();
        cb_roomTypes.getItems().addAll(roomTypes);
    }

    private void delete() {
        tb_rooms.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                int selectedId = newSelection.getId();

                btn_delete.setOnAction(event -> {
                    DBUtils.deleteRoom(Integer.parseInt(String.valueOf(selectedId)));
                    showRoomData();
                });
            }
        });
    }

    private void showFeatures() {
        features = DBUtils.getExtraFeaturesData();
        list_extraFeatures.getItems().addAll(features);
        list_extraFeatures.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    private void add() {
        ObservableList<FeaturesModule> selectedItems = list_extraFeatures.getSelectionModel().getSelectedItems();

        StringBuilder combinedFeatures = new StringBuilder();
        for (FeaturesModule feature : selectedItems) {
            combinedFeatures.append(feature.getFeatureName()).append(",");
        }

        cb_roomTypes.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedRoomTypeId = newSelection.getId();
            }
        });

        TextFormatter<Integer> capacityFormatter = DBUtils.createIntegerTextFormatter();
        TextFormatter<Integer> priceFormatter = DBUtils.createIntegerTextFormatter();
        TextFormatter<Integer> numberFormatter = DBUtils.createIntegerTextFormatter();

        tf_capacity.setTextFormatter(capacityFormatter);
        tf_price.setTextFormatter(priceFormatter);
        tf_roomNumber.setTextFormatter(numberFormatter);

        btn_add.setOnAction(event -> {
            DBUtils.addRoom(selectedRoomTypeId, (cb_roomTypes.getValue() + "-" + tf_roomNumber.getText()),
                    tf_capacity.getText(),
                    tf_price.getText(),
                    selectedItems.toString()
            );
            cb_roomTypes.getSelectionModel().select(null);
            tf_roomNumber.setText("");
            tf_capacity.setText("");
            tf_price.setText("");
            showRoomData();
        });
    }

    private void showRoomData() {
        tb_roomName.setCellValueFactory(new PropertyValueFactory<>("roomName"));
        tb_capacity.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        tb_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        tb_features.setCellValueFactory(new PropertyValueFactory<>("features"));
        rooms = DBUtils.getRoomData();
        tb_rooms.setItems(rooms);
    }

}
