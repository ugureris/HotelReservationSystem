package com.example.finalproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

public class DBUtils {

    static String url = "jdbc:mysql://localhost:3306/upodOtelDB";
    static String username = "root";
    static String password = "password";

    static Connection connection = null;

    static PreparedStatement preparedStatement;
    static PreparedStatement psSet;
    static ResultSet resultSet;


    public static Connection connectDb() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
            return connection;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void changeScene(ActionEvent event, String fxmlFile, String title) {
        Parent root = null;

        try {
            FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlFile));
            root = loader.load();

        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root, 700, 450));
        stage.show();
    }

    public static TextFormatter<String> createStringTextFormatter() {
        return new TextFormatter<>(new StringConverter<String>() {
            @Override
            public String fromString(String string) {
                return string;
            }

            @Override
            public String toString(String object) {
                return object;
            }
        }, null, c -> {
            if (c.getControlNewText().matches("\\d*")) {
                return c;
            } else {
                showAlert("You can only enter numeric values.");
                return null;
            }
        });
    }

    public static TextFormatter<Integer> createIntegerTextFormatter() {
        return new TextFormatter<>(new IntegerStringConverter(), null, c -> {
            if (c.getControlNewText().matches("\\d*")) {
                return c;
            } else {
                showAlert("You can only enter numeric values.");
                return null;
            }
        });
    }

    public static void showAlert(String contentText) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    public static void addService(String serviceName, double unitPrice) {
        connection = connectDb();
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO service (serviceName, unitPrice) VALUES (?, ?)");
            preparedStatement.setString(1, serviceName);
            preparedStatement.setDouble(2, unitPrice);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }

                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void addRoom(int roomTypeId, String roomName, String capacity, String price, String features) {
        connection = connectDb();
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO room (roomTypeId, roomName, capacity, price, featureName) " +
                    "VALUES (?, ?, ?, ?, ?)");
            preparedStatement.setInt(1, roomTypeId);
            preparedStatement.setString(2, roomName);
            preparedStatement.setString(3, capacity);
            preparedStatement.setString(4, price);
            preparedStatement.setString(5, features);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }

                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public static ObservableList<RoomTypeModule> getRoomType() {
        connection = connectDb();
        ObservableList<RoomTypeModule> roomType = FXCollections.observableArrayList();
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM roomType");
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                roomType.add(new RoomTypeModule(Integer.parseInt(resultSet.getString("id"))
                        , resultSet.getString("roomName")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }

                if (preparedStatement != null) {
                    preparedStatement.close();
                }

                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return roomType;
    }


    public static ObservableList<RoomsModule> getRoomData() {
        connection = connectDb();
        ObservableList<RoomsModule> rooms = FXCollections.observableArrayList();
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM room");
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                rooms.add(new RoomsModule(Integer.parseInt(resultSet.getString("id"))
                        , resultSet.getString("roomName")
                        , Integer.parseInt(resultSet.getString("roomTypeId"))
                        , Integer.parseInt(resultSet.getString("capacity"))
                        , Double.parseDouble(resultSet.getString("price"))
                        , resultSet.getString("featureName")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }

                if (preparedStatement != null) {
                    preparedStatement.close();
                }

                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return rooms;
    }

    public static void deleteRoom(int id) {
        connection = connectDb();
        try {
            psSet = connection.prepareStatement("SET FOREIGN_KEY_CHECKS=0;");
            psSet.executeUpdate();
            preparedStatement = connection.prepareStatement("DELETE FROM room WHERE id = (?)");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }

                if (psSet != null) {
                    psSet.close();
                }

                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void updateRoom(RoomsModule rooms) {
        connection = connectDb();
        try {
            preparedStatement = connection.prepareStatement("UPDATE room SET roomName = ? " +
                    ", capacity = ? " +
                    ", price = ?" +
                    ",featureName = ?" +
                    "WHERE id = ?");
            preparedStatement.setString(1, rooms.getRoomName());
            preparedStatement.setInt(2, rooms.getCapacity());
            preparedStatement.setDouble(3, rooms.getPrice());
            preparedStatement.setString(4, rooms.getFeatures());
            preparedStatement.setInt(5, rooms.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }

                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public static double getRoomsPrice(int reservationId) {
        connection = connectDb();
        double total = 0;
        try {
            preparedStatement = connection.prepareStatement("SELECT DATEDIFF(reservation.checkOutDate, reservation.checkInDate ) * room.price " +
                    "AS total_price FROM reservation " +
                    "INNER JOIN room ON reservation.roomId = room.id " +
                    "WHERE reservation.reservationId = ?");
            preparedStatement.setInt(1, reservationId);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                total = resultSet.getDouble("total_price");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }

                if (preparedStatement != null) {
                    preparedStatement.close();
                }

                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return total;
    }

    public static ObservableList<ServicesModule> getServiceData() {
        connection = connectDb();
        ObservableList<ServicesModule> services = FXCollections.observableArrayList();
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM service");
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                services.add(new ServicesModule(Integer.parseInt(resultSet.getString("id"))
                        , resultSet.getString("serviceName")
                        , Double.parseDouble(resultSet.getString("unitPrice"))));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }

                if (preparedStatement != null) {
                    preparedStatement.close();
                }

                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return services;
    }

    public static ObservableList<FeaturesModule> getFeaturesData() {
        connection = connectDb();
        ObservableList<FeaturesModule> features = FXCollections.observableArrayList();
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM feature");
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                features.add(new FeaturesModule(Integer.parseInt(resultSet.getString("id"))
                        , resultSet.getString("featureName")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }

                if (preparedStatement != null) {
                    preparedStatement.close();
                }

                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return features;
    }

    public static void addExtraFeature(String featureName) {
        connection = connectDb();
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO extraFeature (featureName) VALUES (?)");
            preparedStatement.setString(1, featureName);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }

                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void deleteExtraFeatures(int id) {
        connection = connectDb();
        try {
            preparedStatement = connection.prepareStatement("DELETE FROM extraFeature WHERE id = (?)");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }

                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void updateExtraFeatures(FeaturesModule features) {
        connection = connectDb();
        try {
            preparedStatement = connection.prepareStatement("UPDATE extraFeature SET featureName = ? WHERE id = ?");
            preparedStatement.setString(1, features.getFeatureName());
            preparedStatement.setInt(2, features.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }

                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static ObservableList<FeaturesModule> getExtraFeaturesData() {
        connection = connectDb();
        ObservableList<FeaturesModule> extraFeatures = FXCollections.observableArrayList();
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM extraFeature");
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                extraFeatures.add(new FeaturesModule(Integer.parseInt(resultSet.getString("id"))
                        , resultSet.getString("featureName")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }

                if (preparedStatement != null) {
                    preparedStatement.close();
                }

                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return extraFeatures;
    }


    public static double getServicesPrice(int reservationId) {
        connection = connectDb();
        double total = 0;
        try {
            preparedStatement = connection.prepareStatement("SELECT SUM(reservationservice.quantity * service.unitPrice) AS total_price  " +
                    "FROM service " +
                    "INNER JOIN reservationservice  ON reservationservice.serviceId = service.id " +
                    "WHERE reservationservice.reservationId = ?;");
            preparedStatement.setInt(1, reservationId);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                total = resultSet.getDouble("total_price");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }

                if (preparedStatement != null) {
                    preparedStatement.close();
                }

                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return total;
    }

    public static ObservableList<ServicesModule> getServices(int reservationId) {
        connection = connectDb();
        ObservableList<ServicesModule> services = FXCollections.observableArrayList();
        try {
            preparedStatement = connection.prepareStatement("SELECT reservationservice.reservationServiceId, reservationservice.reservationId, service.serviceName, reservationservice.quantity, service.unitPrice " +
                    "FROM service INNER JOIN reservationservice ON reservationservice.serviceId = service.id " +
                    "WHERE reservationservice.reservationId = ?");
            preparedStatement.setInt(1, reservationId);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                services.add(new ServicesModule(Integer.parseInt(resultSet.getString("reservationId")),
                        Integer.parseInt(resultSet.getString("reservationServiceId")),
                        resultSet.getString("serviceName"),
                        Integer.parseInt(resultSet.getString("quantity")),
                        Double.parseDouble(resultSet.getString("unitPrice"))));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }

                if (preparedStatement != null) {
                    preparedStatement.close();
                }

                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return services;
    }

    public static ObservableList<RoomsModule> getRoomsForBill(int reservationId) {
        connection = connectDb();
        ObservableList<RoomsModule> rooms = FXCollections.observableArrayList();
        try {
            preparedStatement = connection.prepareStatement("SELECT room.roomName, room.price, DATEDIFF(reservation.checkOutDate , reservation.checkInDate) AS stayLength FROM room " +
                    "INNER JOIN reservation ON reservation.roomId = room.id " +
                    "WHERE reservation.reservationId = ?;");
            preparedStatement.setInt(1, reservationId);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                rooms.add(new RoomsModule(resultSet.getString("roomName"),
                        Double.parseDouble(resultSet.getString("price")),
                        Integer.parseInt(resultSet.getString("stayLength"))));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }

                if (preparedStatement != null) {
                    preparedStatement.close();
                }

                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return rooms;
    }

    public static ObservableList<ReservationModule> getCustomerForBill(int reservationId) {
        connection = connectDb();
        ObservableList<ReservationModule> customerName = FXCollections.observableArrayList();
        try {
            preparedStatement = connection.prepareStatement("SELECT customer.fullName, reservationcustomer.reservationId  FROM customer " +
                    "INNER JOIN reservationcustomer ON customer.id = reservationcustomer.customerId " +
                    "WHERE reservationcustomer.reservationId = ? ");
            preparedStatement.setInt(1, reservationId);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                customerName.add(new ReservationModule(Integer.parseInt(resultSet.getString("reservationId")),
                        resultSet.getString("fullName")));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }

                if (preparedStatement != null) {
                    preparedStatement.close();
                }

                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return customerName;
    }

    public static void addReservation(int roomId,
                                      LocalDate checkInDate,
                                      LocalDate checkOutDate,
                                      LocalDate checkedInTime) {
        connection = connectDb();

        try {
            preparedStatement = connection.prepareStatement("INSERT INTO reservation (roomId, checkInDate, checkOutDate, checkedInTime) " +
                    "VALUES (?, ?, ?, ?)");
            preparedStatement.setInt(1, roomId);
            preparedStatement.setDate(2, Date.valueOf(checkInDate));
            preparedStatement.setDate(3, Date.valueOf(checkOutDate));
            preparedStatement.setDate(4, Date.valueOf(checkedInTime));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }

                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void addReservationCustomer(int reservationId, int customerId) {
        connection = connectDb();
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO reservationcustomer (reservationId, customerId) " +
                    "VALUES (?, ?)");
            PreparedStatement ps = connection.prepareStatement("UPDATE reservation SET isCustomerAssigned = 1 WHERE reservationId = ?");
            ps.setInt(1, reservationId);
            ps.executeUpdate();
            preparedStatement.setInt(1, reservationId);
            preparedStatement.setInt(2, customerId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }

                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void addReservationService(int reservationId, int serviceId, int quantity) {
        connection = connectDb();
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO reservationservice (reservationId, serviceId, quantity) " +
                    "VALUES (?, ?, ?)");
            preparedStatement.setInt(1, reservationId);
            preparedStatement.setInt(2, serviceId);
            preparedStatement.setInt(3, quantity);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }

                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public static ObservableList<RoomsModule> getRoomsForReservation(LocalDate checkInDate, LocalDate checkOutDate) {
        connection = connectDb();
        ObservableList<RoomsModule> rooms = FXCollections.observableArrayList();
        try {
            preparedStatement = connection.prepareStatement("SELECT room.id, room.roomName " +
                    "FROM room " +
                    "WHERE room.id NOT IN (" +
                    "    SELECT DISTINCT reservation.roomId " +
                    "    FROM reservation " +
                    "    WHERE (" +
                    "(checkInDate <= ? AND checkOutDate >= ?) OR " +
                    "(checkInDate <= ? AND checkOutDate >= ?) OR " +
                    "(checkInDate >= ? AND checkOutDate <= ?)" +
                    ")" +
                    ");");
            preparedStatement.setDate(1, Date.valueOf(checkInDate));
            preparedStatement.setDate(2, Date.valueOf(checkInDate));
            preparedStatement.setDate(3, Date.valueOf(checkOutDate));
            preparedStatement.setDate(4, Date.valueOf(checkOutDate));
            preparedStatement.setDate(5, Date.valueOf(checkInDate));
            preparedStatement.setDate(6, Date.valueOf(checkOutDate));
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                rooms.add(new RoomsModule(Integer.parseInt(resultSet.getString("id"))
                        , resultSet.getString("roomName")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }

                if (preparedStatement != null) {
                    preparedStatement.close();
                }

                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return rooms;
    }

    public static ObservableList<ServicesModule> getServicesForServices() {
        connection = connectDb();
        ObservableList<ServicesModule> services = FXCollections.observableArrayList();
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM service");
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                services.add(new ServicesModule(Integer.parseInt(resultSet.getString("id")),
                        resultSet.getString("serviceName"),
                        Double.parseDouble(resultSet.getString("unitPrice"))));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }

                if (preparedStatement != null) {
                    preparedStatement.close();
                }

                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return services;
    }


    public static ObservableList<ReservationModule> getReservationsForServices() {
        connection = connectDb();
        ObservableList<ReservationModule> reservationsForServices = FXCollections.observableArrayList();
        try {
            preparedStatement = connection.prepareStatement("SELECT reservation.reservationId, room.roomName FROM room " +
                    "INNER JOIN  reservation ON reservation.roomId = room.id " +
                    "WHERE isCustomerAssigned = 1; ");
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                reservationsForServices.add(new ReservationModule(Integer.parseInt(resultSet.getString("reservationId")),
                        resultSet.getString("roomName")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }

                if (preparedStatement != null) {
                    preparedStatement.close();
                }

                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return reservationsForServices;
    }

    public static ObservableList<ReservationModule> getReservedRooms() {
        connection = connectDb();
        ObservableList<ReservationModule> reservedRooms = FXCollections.observableArrayList();

        try {
            preparedStatement = connection.prepareStatement("SELECT reservation.reservationId, room.id ,room.roomName FROM room " +
                    "INNER JOIN  reservation ON reservation.roomId = room.id " +
                    "WHERE isCustomerAssigned = 0;");

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                reservedRooms.add(new ReservationModule(Integer.parseInt(resultSet.getString("reservationId")),
                        resultSet.getString("roomName")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }

                if (preparedStatement != null) {
                    preparedStatement.close();
                }

                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return reservedRooms;
    }

    public static ObservableList<CustomersModule> getCustomersForReservation() {
        connection = connectDb();
        ObservableList<CustomersModule> customers = FXCollections.observableArrayList();
        try {
            preparedStatement = connection.prepareStatement("SELECT id, fullName FROM customer");
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                customers.add(new CustomersModule(Integer.parseInt(resultSet.getString("id"))
                        , resultSet.getString("fullName")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }

                if (preparedStatement != null) {
                    preparedStatement.close();
                }

                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return customers;
    }


    public static ObservableList<RoomsModule> getFeaturesForReservation(int id) {
        connection = connectDb();
        ObservableList<RoomsModule> roomFeature = FXCollections.observableArrayList();
        try {
            preparedStatement = connection.prepareStatement("SELECT id, featureName FROM room WHERE id = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                roomFeature.add(new RoomsModule(Integer.parseInt(resultSet.getString("id"))
                        , resultSet.getString("featureName")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }

                if (preparedStatement != null) {
                    preparedStatement.close();
                }

                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return roomFeature;
    }

    public static void addCustomer(String fullName,
                                   String identityNumber,
                                   String phoneNumber,
                                   LocalDate birthDate,
                                   String description) {
        connection = connectDb();

        try {
            if (isIdentityExist(identityNumber)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("A customer with this identity number already exists!");
                alert.showAndWait();

                return;
            }

            preparedStatement = connection.prepareStatement("INSERT INTO customer " +
                    "(fullName, identityNumber, phoneNumber, birthDate, description) " +
                    "VALUES (?, ?, ?, ?, ?)");
            preparedStatement.setString(1, fullName);
            preparedStatement.setString(2, identityNumber);
            preparedStatement.setString(3, phoneNumber);
            preparedStatement.setDate(4, Date.valueOf(birthDate));
            preparedStatement.setString(5, description);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }

                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean isIdentityExist(String identityNumber) {
        connection = connectDb();
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM customer WHERE identityNumber = ?");

            preparedStatement.setString(1, identityNumber);
            resultSet = preparedStatement.executeQuery();

            return resultSet.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    public static void deleteCustomer(int id) {
        connection = connectDb();
        try {
            psSet = connection.prepareStatement("SET FOREIGN_KEY_CHECKS=0;");
            psSet.executeUpdate();
            preparedStatement = connection.prepareStatement("DELETE FROM customer WHERE id = (?)");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }

                if (psSet != null) {
                    psSet.close();
                }

                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void deleteReservation(int id) {
        connection = connectDb();
        try {
            psSet = connection.prepareStatement("SET FOREIGN_KEY_CHECKS=0;");
            psSet.executeUpdate();
            preparedStatement = connection.prepareStatement("DELETE FROM reservation WHERE reservationId = (?)");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }

                if (psSet != null) {
                    psSet.close();
                }

                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static ObservableList<ReservationModule> getReservations() {
        connection = connectDb();
        ObservableList<ReservationModule> reservations = FXCollections.observableArrayList();
        try {
            preparedStatement = connection.prepareStatement("SELECT reservation.reservationId, " +
                    "reservation.roomId, " +
                    "room.roomName, " +
                    "reservation.checkInDate, " +
                    "reservation.checkOutDate, " +
                    "reservation.checkedInTime, " +
                    "customer.fullName  FROM reservation " +
                    "INNER JOIN room  ON reservation.roomId = room.id " +
                    "INNER JOIN reservationcustomer ON reservationcustomer.reservationId = reservation.reservationId " +
                    "INNER JOIN customer ON customer.id = reservationcustomer.customerId ;");
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                reservations.add(new ReservationModule(Integer.parseInt(resultSet.getString("reservationId")),
                        Integer.parseInt(resultSet.getString("roomId")),
                        resultSet.getString("roomName"),
                        LocalDate.parse(resultSet.getString("checkInDate")),
                        LocalDate.parse(resultSet.getString("checkOutDate")),
                        LocalDate.parse(resultSet.getString("checkedInTime")),
                        resultSet.getString("fullName")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }

                if (preparedStatement != null) {
                    preparedStatement.close();
                }

                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return reservations;
    }

    public static ObservableList<CustomersModule> getCustomersData() {
        connection = connectDb();
        ObservableList<CustomersModule> customers = FXCollections.observableArrayList();
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM customer");
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                customers.add(new CustomersModule(Integer.parseInt(resultSet.getString("id"))
                        , resultSet.getString("fullName")
                        , resultSet.getString("identityNumber")
                        , resultSet.getString("phoneNumber")
                        , LocalDate.parse(resultSet.getString("birthDate"))
                        , resultSet.getString("description")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }

                if (preparedStatement != null) {
                    preparedStatement.close();
                }

                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return customers;
    }


    public static void updateReservation(ReservationModule reservation) {
        connection = connectDb();
        try {
            preparedStatement = connection.prepareStatement("UPDATE reservation SET checkInDate = ? ," +
                    "checkOutDate = ? " +
                    "WHERE reservationId = ?");
            preparedStatement.setDate(1, Date.valueOf(reservation.getCheckInDate()));
            preparedStatement.setDate(2, Date.valueOf(reservation.getCheckOutDate()));
            preparedStatement.setInt(3, reservation.getReservationId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }

                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void updateCustomer(CustomersModule customers) {
        connection = connectDb();
        try {
            preparedStatement = connection.prepareStatement("UPDATE customer SET fullName = ?" +
                    ",identityNumber = ?" +
                    ",phoneNumber = ?" +
                    ",birthDate = ?" +
                    ",description = ?" +
                    "WHERE id = ?");

            preparedStatement.setString(1, customers.getFullName());
            preparedStatement.setString(2, customers.getIdentityNumber());
            preparedStatement.setString(3, customers.getPhoneNumber());
            preparedStatement.setDate(4, Date.valueOf(customers.getBirthDate()));
            preparedStatement.setString(5, customers.getDescription());
            preparedStatement.setInt(6, customers.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }

                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void updateService(ServicesModule services) {
        connection = connectDb();
        try {
            preparedStatement = connection.prepareStatement("UPDATE service SET serviceName = ?," +
                    " unitPrice = ?" +
                    " WHERE id = ?");
            preparedStatement.setString(1, services.getServiceName());
            preparedStatement.setDouble(2, services.getUnitPrice());
            preparedStatement.setInt(3, services.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }

                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void deleteReservationService(int reservationId) {
        connection = connectDb();
        try {
            preparedStatement = connection.prepareStatement("DELETE FROM reservationservice WHERE reservationServiceId = ?");
            preparedStatement.setInt(1, reservationId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }

                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void deleteRoomType(int id) {
        connection = connectDb();
        try {
            psSet = connection.prepareStatement("SET FOREIGN_KEY_CHECKS=0;");
            psSet.executeUpdate();
            preparedStatement = connection.prepareStatement("DELETE FROM roomtype WHERE id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }

                if (psSet != null) {
                    psSet.close();
                }

                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void deleteService(int id) {
        connection = connectDb();
        try {
            psSet = connection.prepareStatement("SET FOREIGN_KEY_CHECKS=0;");
            psSet.executeUpdate();
            preparedStatement = connection.prepareStatement("DELETE FROM service WHERE id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }

                if (psSet != null) {
                    psSet.close();
                }


                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void addRoomType(String roomName) {
        connection = connectDb();
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO roomType (roomName) " +
                    "VALUES (?)");
            preparedStatement.setString(1, roomName);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }

                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static ObservableList<RoomTypeModule> getRoomTypes() {
        connection = connectDb();
        ObservableList<RoomTypeModule> roomTypes = FXCollections.observableArrayList();
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM roomType");
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                roomTypes.add(new RoomTypeModule(Integer.parseInt(resultSet.getString("id")),
                        resultSet.getString("roomName")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }

                if (preparedStatement != null) {
                    preparedStatement.close();
                }

                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return roomTypes;
    }
}
