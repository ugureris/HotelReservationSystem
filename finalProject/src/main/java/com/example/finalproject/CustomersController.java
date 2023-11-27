package com.example.finalproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LocalDateStringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static com.example.finalproject.DBUtils.updateCustomer;

public class CustomersController implements Initializable {

    @FXML
    private Button btn_home;

    @FXML
    private Button btn_services;

    @FXML
    private Button btn_rooms;

    @FXML
    private Button btn_features;

    @FXML
    private Button btn_delete;

    @FXML
    private Button btn_edit;

    @FXML
    private Button btn_create;

    @FXML
    private TextField tf_fullName;

    @FXML
    private TextField tf_identity;

    @FXML
    private TextField tf_phone;

    @FXML
    private DatePicker dp_birthDate;

    @FXML
    private TextArea ta_description;

    @FXML
    private TableView<CustomersModule> tb_customers;

    @FXML
    private TableColumn<CustomersModule, String> tb_fullName;

    @FXML
    private TableColumn<CustomersModule, String> tb_identity;

    @FXML
    private TableColumn<CustomersModule, String> tb_phone;

    @FXML
    private TableColumn<CustomersModule, LocalDate> tb_birthDate;

    @FXML
    private TableColumn<CustomersModule, String> tb_description;

    ObservableList<CustomersModule> customers = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buttons();
        create();
        customersTable();
        update();
        delete();
    }

    private void buttons() {
        btn_home.setOnAction(event -> DBUtils.changeScene(event, "home.fxml", "Home"));
        btn_services.setOnAction(event -> DBUtils.changeScene(event, "services.fxml", "Services"));
        btn_rooms.setOnAction(event -> DBUtils.changeScene(event, "rooms.fxml", "Rooms"));
        btn_features.setOnAction(event -> DBUtils.changeScene(event, "features.fxml", "Features"));

        btn_edit.setOnAction(event -> {
                    editableCustomer();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Customer Page Information");
                    alert.setContentText("The customers table can be edited");
                    alert.show();
                }
        );
    }

    private void customersTable() {
        tb_fullName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        tb_identity.setCellValueFactory(new PropertyValueFactory<>("identityNumber"));
        tb_phone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        tb_birthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        tb_description.setCellValueFactory(new PropertyValueFactory<>("description"));

        showCustomers();
    }

    private void showCustomers() {
        customers = DBUtils.getCustomersData();
        tb_customers.setItems(customers);
    }


    private void create() {
        TextFormatter<String> identityFormatter = DBUtils.createStringTextFormatter();
        TextFormatter<String> phoneFormatter = DBUtils.createStringTextFormatter();
        tf_identity.setTextFormatter(identityFormatter);
        tf_phone.setTextFormatter(phoneFormatter);

        btn_create.setOnAction(event -> {
            if (!isValidLength(tf_identity.getText(), 11, "Identity Number")) {
                return;
            }

            if (!isValidLength(tf_phone.getText(), 10, "Phone Number")) {
                return;
            }

            if (!isValidIdentity(tf_identity.getText())) {
                DBUtils.showAlert("Invalid Identity Number");
                return;
            }

            DBUtils.addCustomer(tf_fullName.getText(),
                    tf_identity.getText(),
                    tf_phone.getText(),
                    dp_birthDate.getValue(),
                    ta_description.getText());
            clearField();
            showCustomers();
        });
    }

    private void clearField() {
        tf_fullName.setText("");
        tf_identity.setText("");
        tf_phone.setText("");
        dp_birthDate.setValue(LocalDate.now());
        ta_description.setText("");
    }

    private static boolean isValidIdentity(String identityNumber) {
        int[] identityDigits = splitIdentity(identityNumber);

        if (identityDigits != null) {
            boolean condition1 = (identityDigits[0] + identityDigits[1] + identityDigits[2] + identityDigits[3] + identityDigits[4] + identityDigits[5] + identityDigits[6] + identityDigits[7] + identityDigits[8] + identityDigits[9]) % 10 == identityDigits[10];
            boolean condition2 = (((identityDigits[0] + identityDigits[2] + identityDigits[4] + identityDigits[6] + identityDigits[8]) * 7) + ((identityDigits[1] + identityDigits[3] + identityDigits[5] + identityDigits[7]) * 9)) % 10 == identityDigits[9];
            boolean condition3 = ((identityDigits[0] + identityDigits[2] + identityDigits[4] + identityDigits[6] + identityDigits[8]) * 8) % 10 == identityDigits[10];

            return condition1 && condition2 && condition3;
        }
        return false;
    }

    private static int[] splitIdentity(String identityNumber) {
        int[] numbers = new int[11];

        if (identityNumber == null) {
            return null;
        }

        for (int i = 0; i < 11; i++) {
            numbers[i] = Integer.parseInt(identityNumber.substring(i, (i + 1)));
        }
        return numbers;
    }

    private boolean isValidLength(String text, int maxLength, String fieldName) {
        if (text.length() != maxLength) {
            DBUtils.showAlert(fieldName + " must be " + maxLength + " digits.");
            return false;
        }
        return true;
    }


    private void update() {
        tb_fullName.setOnEditCommit(fullNameEvent -> {
            String newValue = fullNameEvent.getNewValue();
            CustomersModule selectedItem = fullNameEvent.getRowValue();
            selectedItem.setFullName(newValue);
            updateCustomer(selectedItem);
        });
        tb_identity.setOnEditCommit(identityEvent -> {
            String newValue = identityEvent.getNewValue();
            CustomersModule selectedItem = identityEvent.getRowValue();
            selectedItem.setIdentityNumber(newValue);
            updateCustomer(selectedItem);
        });
        tb_phone.setOnEditCommit(phoneEvent -> {
            String newValue = phoneEvent.getNewValue();
            CustomersModule selectedItem = phoneEvent.getRowValue();
            selectedItem.setPhoneNumber(newValue);
            updateCustomer(selectedItem);
        });
        tb_birthDate.setOnEditCommit(birthDateEvent -> {
            LocalDate newDate = birthDateEvent.getNewValue();
            CustomersModule selectedItem = birthDateEvent.getRowValue();
            selectedItem.setBirthDate(newDate);
            updateCustomer(selectedItem);
        });
        tb_description.setOnEditCommit(descEvent -> {
            String newValue = descEvent.getNewValue();
            CustomersModule selectedItem = descEvent.getRowValue();
            selectedItem.setDescription(newValue);
            updateCustomer(selectedItem);
        });
    }

    private void editableCustomer() {
        tb_customers.setEditable(true);
        tb_customers.getSelectionModel().setCellSelectionEnabled(true);
        tb_fullName.setCellFactory(TextFieldTableCell.<CustomersModule>forTableColumn());
        tb_identity.setCellFactory(TextFieldTableCell.<CustomersModule>forTableColumn());
        tb_phone.setCellFactory(TextFieldTableCell.<CustomersModule>forTableColumn());
        tb_birthDate.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateStringConverter()));
        tb_description.setCellFactory(TextFieldTableCell.<CustomersModule>forTableColumn());
    }

    private void delete() {
        tb_customers.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                int selectedId = newSelection.getId();

                btn_delete.setOnAction(event -> {
                    DBUtils.deleteCustomer(Integer.parseInt(String.valueOf(selectedId)));
                    showCustomers();
                });
            }
        });
    }
}
