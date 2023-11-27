package com.example.finalproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static com.example.finalproject.DBUtils.getFeaturesForReservation;

public class NewReservationController implements Initializable {

    @FXML
    private ListView<String> list_extraFeatures;

    @FXML
    private Button btn_back;

    @FXML
    private Button btn_addCustomer;

    @FXML
    private Button btn_reservation;

    @FXML
    private ChoiceBox<RoomsModule> cb_rooms;

    @FXML
    private ChoiceBox<CustomersModule> cb_customers;

    @FXML
    private ChoiceBox<ReservationModule> cb_reservedRooms;


    @FXML
    private DatePicker dp_checkIn;

    @FXML
    private DatePicker dp_checkOut;

    @FXML
    private ListView<FeaturesModule> list_features;

    ObservableList<FeaturesModule> features;

    ObservableList<RoomsModule> rooms;
    ObservableList<CustomersModule> customers;

    ObservableList<ReservationModule> reservedRooms;
    private int roomId;

    private int reservationIdCustomer;

    private int customerId;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_back.setOnAction(event -> DBUtils.changeScene(event, "home.fxml", "Home"));

        showFeatures();
        showRooms();
        addCustomer();
        getReservedRooms();
        add();
    }

    private void add() {
        btn_reservation.setOnAction(event -> {
            DBUtils.addReservation(roomId,
                    dp_checkIn.getValue(),
                    dp_checkOut.getValue(),
                    dp_checkIn.getValue());
            dp_checkIn.setValue(LocalDate.now());
            dp_checkOut.setValue(LocalDate.now());
            cb_rooms.getItems().clear();
            cb_reservedRooms.getItems().clear();
            getReservedRooms();
        });
    }

    private void showRooms() {
        dp_checkIn.setValue(LocalDate.now());
        dp_checkOut.setValue(LocalDate.now());

        dp_checkIn.setOnAction(event -> {
            if (dp_checkIn.getValue() != null) {
                dp_checkOut.setDayCellFactory(picker -> new DateCell() {
                    @Override
                    public void updateItem(LocalDate date, boolean empty) {
                        super.updateItem(date, empty);
                        setDisable(date.isBefore(dp_checkIn.getValue()));
                    }
                });
            } else {
                dp_checkOut.setDayCellFactory(null);
            }
        });


        dp_checkOut.setOnAction(event -> {
            cb_rooms.getItems().clear();
            rooms = DBUtils.getRoomsForReservation(dp_checkIn.getValue(), dp_checkOut.getValue());
            cb_rooms.getItems().addAll(rooms);
        });
    }

    private void getReservedRooms() {
        reservedRooms = DBUtils.getReservedRooms();
        cb_reservedRooms.getItems().addAll(reservedRooms);
    }

    private void addCustomer() {

        customers = DBUtils.getCustomersForReservation();
        cb_customers.getItems().addAll(customers);


        cb_reservedRooms.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                reservationIdCustomer = newSelection.getReservationId();
            }
        });

        cb_customers.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                customerId = newSelection.getId();
            }
        });

        btn_addCustomer.setOnAction(event -> {
            DBUtils.addReservationCustomer(reservationIdCustomer, customerId);

            cb_reservedRooms.getSelectionModel().select(null);
            cb_customers.getSelectionModel().select(null);
            cb_reservedRooms.getItems().clear();
            getReservedRooms();
        });
    }

    private void showFeatures() {
        features = DBUtils.getFeaturesData();
        list_features.getItems().addAll(features);

        cb_rooms.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                roomId = newSelection.getId();
            }
            String features = getFeaturesForReservation(roomId).toString();

            features = features.replace("[", "").replace("]", "").replaceAll(" ", "");
            String[] featureArray = features.split(",");
            ObservableList<String> list = FXCollections.observableArrayList(featureArray);
            list_extraFeatures.setItems(list);
        });
    }
}
