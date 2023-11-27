package com.example.finalproject;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @FXML
    private Button btn_customers;

    @FXML
    private Button btn_services;

    @FXML
    private Button btn_features;

    @FXML
    private Button btn_rooms;

    @FXML
    private Button btn_reservation;

    @FXML
    private Button btn_delete;

    @FXML
    private Button btn_bill;

    @FXML
    private Button btn_edit;

    @FXML
    private DatePicker dp_from;

    @FXML
    private DatePicker dp_to;

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
    private TableColumn<ReservationModule, String> tb_checkedIn;

    @FXML
    private TableColumn<ReservationModule, String> tb_checkedOut;

    @FXML
    private TableColumn<ReservationModule, String> tb_customer;

    @FXML
    private TextField tf_filter;

    ObservableList<ReservationModule> reservations;
    private int reservationId;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        buttons();
        reservationsTable();
        delete();
        searchItems();
        bill();


    }

    public void searchItems() {

        dp_from.setOnAction(event -> {
            if (dp_from.getValue() != null) {
                dp_to.setDayCellFactory(picker -> new DateCell() {
                    @Override
                    public void updateItem(LocalDate date, boolean empty) {
                        super.updateItem(date, empty);
                        setDisable(date.isBefore(dp_from.getValue()));

                    }
                });
            } else {
                dp_to.setDayCellFactory(null);
            }
        });
        dp_to.setOnAction(event -> filterWithDateAndSearch());
        tf_filter.textProperty().addListener((observable, oldValue, newValue) -> {
            filterWithDateAndSearch();
        });
    }

    public void bill() {
        tb_reservation.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                reservationId = newSelection.getReservationId();
            }
            btn_bill.setOnAction(event -> openBillPopup());


        });
    }

    private void openBillPopup() {
        try {
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.initStyle(StageStyle.UTILITY);
            popupStage.setTitle("View Bill");


            FXMLLoader loader = new FXMLLoader(getClass().getResource("bill.fxml"));
            Parent root = loader.load();

            BillController billController = loader.getController();
            billController.setReservationId(reservationId);

            StackPane rootLayout = new StackPane();
            rootLayout.getChildren().add(root);

            Scene scene = new Scene(rootLayout, 400, 450);

            popupStage.setScene(scene);
            popupStage.show();
            bill();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void filterWithDateAndSearch() {
        String checkIn = dp_from.getValue().toString();
        String checkOut = dp_to.getValue().toString();
        String searchText = tf_filter.getText().toLowerCase();

        ObservableList<ReservationModule> filteredData = FXCollections.observableArrayList();

        for (ReservationModule reservation : reservations) {
            boolean dateMatch = reservation.isWithinDateRange(checkIn, checkOut);
            boolean textMatch = searchText.isEmpty() || reservation.getCustomerName().toLowerCase().startsWith(searchText.toLowerCase());
            //    boolean textMatch = searchText.isEmpty() || reservation.getCustomerName().toLowerCase().contains(searchText);

            if (dateMatch && textMatch) {
                filteredData.add(reservation);
            }
        }

        tb_reservation.setItems(filteredData);
    }

    private void reservationsTable() {
        tb_id.setCellValueFactory(new PropertyValueFactory<>("reservationId"));
        tb_room.setCellValueFactory(new PropertyValueFactory<>("roomName"));
        tb_checkIn.setCellValueFactory(new PropertyValueFactory<>("checkInDate"));
        tb_checkOut.setCellValueFactory(new PropertyValueFactory<>("checkOutDate"));
        tb_checkedIn.setCellValueFactory(cellData -> {
            LocalDate checkInDate = cellData.getValue().getCheckInDate();
            LocalDate checkedInTime = cellData.getValue().getCheckedInTime();
            if (checkInDate.isAfter(LocalDate.now()) || checkInDate.isEqual(LocalDate.now()) && checkedInTime.isAfter(LocalDate.now())) {
                return new SimpleStringProperty("No");
            } else {
                return new SimpleStringProperty("Yes");
            }
        });
        tb_checkedOut.setCellValueFactory(cellData -> {
            LocalDate checkOutDate = cellData.getValue().getCheckOutDate();
            if (checkOutDate.isBefore(LocalDate.now())) {
                return new SimpleStringProperty("Yes");
            } else {
                return new SimpleStringProperty("No");
            }
        });
        tb_customer.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        getReservations();
        dp_from.setValue(LocalDate.now());
        dp_to.setValue(LocalDate.now());
    }

    private void getReservations() {
        reservations = DBUtils.getReservations();
        tb_reservation.setItems(reservations);
    }

    private void buttons() {
        btn_customers.setOnAction(event -> DBUtils.changeScene(event, "customers.fxml", "Customers"));
        btn_services.setOnAction(event -> DBUtils.changeScene(event, "services.fxml", "Services"));
        btn_features.setOnAction(event -> DBUtils.changeScene(event, "features.fxml", "Features"));
        btn_rooms.setOnAction(event -> DBUtils.changeScene(event, "rooms.fxml", "Rooms"));
        btn_reservation.setOnAction(event -> DBUtils.changeScene(event, "reservation.fxml", "New Reservation"));
        btn_edit.setOnAction(event -> DBUtils.changeScene(event, "edit.fxml", "Edit Reservation"));
    }

    private void delete() {
        tb_reservation.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                int selectedId = newSelection.getReservationId();

                btn_delete.setOnAction(event -> {
                    DBUtils.deleteReservation(Integer.parseInt(String.valueOf(selectedId)));
                    getReservations();
                });
            }
        });
    }
}
