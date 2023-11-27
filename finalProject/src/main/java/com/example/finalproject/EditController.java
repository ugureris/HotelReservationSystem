package com.example.finalproject;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.LocalDateStringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static com.example.finalproject.DBUtils.updateReservation;

public class EditController implements Initializable {

    @FXML
    private Button btn_addService;

    @FXML
    private Button btn_back;

    @FXML
    private Button btn_delete;

    @FXML
    private ChoiceBox<ReservationModule> cb_reservations;

    @FXML
    private ChoiceBox<ServicesModule> cb_services;

    @FXML
    private TextField tf_quantity;

    @FXML
    private TableView<ReservationModule> tb_reservation;

    @FXML
    private TableColumn<ReservationModule, Integer> tb_id;

    @FXML
    private TableColumn<ReservationModule, String> tb_room;

    @FXML
    private TableColumn<ReservationModule, LocalDate> tb_checkIn;

    @FXML
    private TableColumn<ReservationModule, LocalDate> tb_checkOut;

    @FXML
    private TableView<ServicesModule> tb_service;

    @FXML
    private TableColumn<ServicesModule, String> tb_serviceName;

    @FXML
    private TableColumn<ServicesModule, Integer> tb_quantity;

    @FXML
    private TableColumn<ReservationModule, String> tb_customer;

    ObservableList<ReservationModule> reservation;

    ObservableList<ServicesModule> services;

    ObservableList<ReservationModule> reservations;

    private int reservationId;

    private int serviceId;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_back.setOnAction(event -> DBUtils.changeScene(event, "home.fxml", "Home"));
        showReservationsForServices();
        showServices();
        addService();

        reservationsTable();

        editable();

        update();

        delete();

    }


    private void reservationsTable() {
        tb_id.setCellValueFactory(new PropertyValueFactory<>("reservationId"));
        tb_room.setCellValueFactory(new PropertyValueFactory<>("roomName"));
        tb_checkIn.setCellValueFactory(new PropertyValueFactory<>("checkInDate"));
        tb_checkOut.setCellValueFactory(new PropertyValueFactory<>("checkOutDate"));
        tb_customer.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        getReservations();
    }

    private void getReservations() {
        reservations = DBUtils.getReservations();
        tb_reservation.setItems(reservations);
    }

    private void editable() {
        tb_reservation.setEditable(true);
        tb_reservation.getSelectionModel().setCellSelectionEnabled(true);
        tb_checkIn.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateStringConverter()));
        tb_checkOut.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateStringConverter()));
    }

    private void update() {
        tb_checkIn.setOnEditCommit(event -> {
            LocalDate newValue = event.getNewValue();
            ReservationModule selectedItem = event.getRowValue();
            selectedItem.setCheckInDate(newValue);
            updateReservation(selectedItem);
        });
        tb_checkOut.setOnEditCommit(event -> {
            LocalDate newValue = event.getNewValue();
            ReservationModule selectedItem = event.getRowValue();
            selectedItem.setCheckOutDate(newValue);
            updateReservation(selectedItem);
        });

    }

    private void showReservationsForServices() {
        reservation = DBUtils.getReservationsForServices();
        cb_reservations.getItems().addAll(reservation);
    }

    private void showServices() {
        services = DBUtils.getServicesForServices();
        cb_services.getItems().addAll(services);
    }

    private void addService() {

        cb_reservations.setOnAction(event -> {
            ReservationModule res = cb_reservations.getValue();
            reservationId = res.getReservationId();
            getServices();
        });

        cb_services.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                serviceId = newSelection.getId();
            }
        });


        TextFormatter<Integer> quantityFormatter = DBUtils.createIntegerTextFormatter();
        tf_quantity.setTextFormatter(quantityFormatter);

        btn_addService.setOnAction(event -> {
            DBUtils.addReservationService(reservationId, serviceId, Integer.parseInt(tf_quantity.getText()));
            cb_services.getSelectionModel().select(null);
            tf_quantity.setText("");
            getServices();
        });
    }

    private void getServices() {
        ObservableList<ServicesModule> service = DBUtils.getServices(reservationId);
        tb_serviceName.setCellValueFactory(new PropertyValueFactory<>("serviceName"));
        tb_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tb_service.setItems(service);
    }

    private void delete() {
        tb_service.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                int selectedIdText = newSelection.getReservationServiceId();
                btn_delete.setOnAction(event -> {
                    DBUtils.deleteReservationService(Integer.parseInt(String.valueOf(selectedIdText)));
                    getServices();
                });
            }
        });
    }
}
