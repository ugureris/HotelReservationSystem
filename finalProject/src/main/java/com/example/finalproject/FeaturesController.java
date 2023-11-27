package com.example.finalproject;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ResourceBundle;

import static com.example.finalproject.DBUtils.updateExtraFeatures;

public class FeaturesController implements Initializable {

    @FXML
    private Button btn_home;

    @FXML
    private Button btn_services;

    @FXML
    private Button btn_customers;

    @FXML
    private Button btn_rooms;

    @FXML
    private Button btn_addFeature;

    @FXML
    private Button btn_delete;

    @FXML
    private Button btn_edit;

    @FXML
    private TextField tf_featureName;

    @FXML
    private TableView<FeaturesModule> tb_extraFeatures;

    @FXML
    private TableColumn<FeaturesModule, String> tb_extraFeatureName;


    @FXML
    private ListView<FeaturesModule> list_features;

    ObservableList<FeaturesModule> features;

    ObservableList<FeaturesModule> extraFeatures;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buttons();
        add();
        showFeatures();
        delete();
        update();
        sendWithEnter();
    }

    private void buttons() {
        btn_home.setOnAction(event -> DBUtils.changeScene(event, "home.fxml", "Home"));
        btn_services.setOnAction(event -> DBUtils.changeScene(event, "services.fxml", "Services"));
        btn_customers.setOnAction(event -> DBUtils.changeScene(event, "customers.fxml", "Customers"));
        btn_rooms.setOnAction(event -> DBUtils.changeScene(event, "rooms.fxml", "Rooms"));

        btn_edit.setOnAction(event -> {
            tb_extraFeatures.setEditable(true);
            tb_extraFeatures.getSelectionModel().setCellSelectionEnabled(true);
            tb_extraFeatureName.setCellFactory(TextFieldTableCell.<FeaturesModule>forTableColumn());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Feature Page Information");
            alert.setContentText("The features table can be edited");
            alert.show();

        });
    }

    private void delete() {
        tb_extraFeatures.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                int selectedIdText = newSelection.getId();

                btn_delete.setOnAction(event -> {
                    DBUtils.deleteExtraFeatures(Integer.parseInt(String.valueOf(selectedIdText)));
                    showFeatures();
                });
            }
        });
    }

    private void add() {
        features = DBUtils.getFeaturesData();
        list_features.getItems().addAll(features);

        btn_addFeature.setOnAction(event -> {
            DBUtils.addExtraFeature(tf_featureName.getText());
            tf_featureName.setText("");
            showFeatures();
        });
    }

    private void update() {
        tb_extraFeatureName.setOnEditCommit(event -> {
            String newValue = event.getNewValue();
            FeaturesModule selectedItem = event.getRowValue();
            selectedItem.setFeatureName(newValue);
            updateExtraFeatures(selectedItem);
        });
    }

    private void sendWithEnter() {
        tf_featureName.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                    DBUtils.addExtraFeature(tf_featureName.getText());
                    tf_featureName.setText("");
                    showFeatures();
                }
            }
        });
    }

    public void showFeatures() {
        tb_extraFeatureName.setCellValueFactory(new PropertyValueFactory<>("featureName"));

        extraFeatures = DBUtils.getExtraFeaturesData();
        tb_extraFeatures.setItems(extraFeatures);
    }
}
