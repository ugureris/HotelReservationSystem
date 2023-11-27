package com.example.finalproject;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class RoomTypeController implements Initializable {


    @FXML
    private TextField tf_roomType;

    @FXML
    private Button btn_add;

    @FXML
    private Button btn_delete;

    @FXML
    private TableView<RoomTypeModule> tb_roomTypes;

    @FXML
    private TableColumn<RoomTypeModule, String> tb_roomName;

    private Stage stage;

    ObservableList<RoomTypeModule> roomTypes;

    public void setStage(Stage stage) {
        this.stage = stage;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tb_roomName.setCellValueFactory(new PropertyValueFactory<>("roomName"));

        showRoomTypes();

        add();

        delete();
    }

    private void showRoomTypes() {
        roomTypes = DBUtils.getRoomTypes();
        tb_roomTypes.getItems().addAll(roomTypes);
    }

    private void add() {
        btn_add.setOnAction(event -> {
            DBUtils.addRoomType(tf_roomType.getText());
            tb_roomTypes.getItems().clear();
            showRoomTypes();
            tf_roomType.setText("");
        });

    }

    private void delete() {
        tb_roomTypes.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                int selectedId = newSelection.getId();

                btn_delete.setOnAction(event -> {
                    DBUtils.deleteRoomType(Integer.parseInt(String.valueOf(selectedId)));
                    tb_roomTypes.getItems().clear();
                    showRoomTypes();
                });
            }
        });
    }
}
