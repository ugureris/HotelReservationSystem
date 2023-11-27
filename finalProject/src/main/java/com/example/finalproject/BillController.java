package com.example.finalproject;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class BillController implements Initializable {
    @FXML
    private Label lbl_customerName;

    @FXML
    private Label lbl_date;

    @FXML
    private TableView<ServicesModule> tb_service;

    @FXML
    private TableColumn<ServicesModule, String> tb_serviceName;

    @FXML
    private TableColumn<ServicesModule, Integer> tb_quantity;

    @FXML
    private TableColumn<ServicesModule, Double> tb_servicePrice;

    @FXML
    private TableView<RoomsModule> tb_room;

    @FXML
    private TableColumn<RoomsModule, String> tb_roomName;

    @FXML
    private TableColumn<RoomsModule, Double> tb_roomPrice;

    @FXML
    private TableColumn<RoomsModule, Integer> tb_day;

    @FXML
    private Label lbl_servicePrice;

    @FXML
    private Label lbl_totalRoom;

    @FXML
    private Label lbl_billTotal;

    private int reservationId;
    double totalServicePrice;
    double totalRoomPrice;

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
        getCustomer();
        getServices();
        getServicesPrice();
        getRoomData();
        getRoomPrice();

        double totalPrice = totalRoomPrice + totalServicePrice;
        lbl_billTotal.setText(String.valueOf(totalPrice));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lbl_date.setText(LocalDate.now().toString());

    }

    private void getRoomPrice() {
        totalRoomPrice = DBUtils.getRoomsPrice(reservationId);
        lbl_totalRoom.setText(String.valueOf(totalRoomPrice));
    }

    private void getServicesPrice() {
        totalServicePrice = DBUtils.getServicesPrice(reservationId);
        lbl_servicePrice.setText(String.valueOf(totalServicePrice));
    }

    private void getServices() {
        ObservableList<ServicesModule> service = DBUtils.getServices(reservationId);
        tb_serviceName.setCellValueFactory(new PropertyValueFactory<>("serviceName"));
        tb_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tb_servicePrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        tb_service.setItems(service);
    }

    private void getRoomData() {
        ObservableList<RoomsModule> room = DBUtils.getRoomsForBill(reservationId);
        tb_roomName.setCellValueFactory(new PropertyValueFactory<>("roomName"));
        tb_roomPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        tb_day.setCellValueFactory(new PropertyValueFactory<>("stayLength"));
        tb_room.setItems(room);
    }

    private void getCustomer() {
        ObservableList<ReservationModule> customerData = DBUtils.getCustomerForBill(reservationId);

        if (!customerData.isEmpty()) {
            lbl_customerName.setText(customerData.toString());
        }
    }
}
