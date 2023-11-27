package com.example.finalproject;

public class ServicesModule {

    int reservationServiceId;
    private int id;
    private String serviceName;
    private int quantity;
    private double unitPrice;

    public ServicesModule(int id, int reservationServiceId, String serviceName, int quantity, double unitPrice) {
        this.id = id;
        this.reservationServiceId = reservationServiceId;
        this.serviceName = serviceName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public ServicesModule(int id, String serviceName, double unitPrice) {
        this.id = id;
        this.serviceName = serviceName;
        this.unitPrice = unitPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReservationServiceId() {
        return reservationServiceId;
    }

    public void setReservationServiceId(int reservationServiceId) {
        this.reservationServiceId = reservationServiceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Override
    public String toString() {
        return serviceName;
    }
}
