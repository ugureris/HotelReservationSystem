package com.example.finalproject;

public class RoomsModule {
    int id;
    String roomName;
    int roomTypeId;
    int capacity;
    double price;
    String features;
    int stayLength;

    public RoomsModule(int id, String roomName, int roomTypeId, int capacity, double price, String features) {
        this.id = id;
        this.roomTypeId = roomTypeId;
        this.roomName = roomName;
        this.capacity = capacity;
        this.price = price;
        this.features = features;
    }

    public RoomsModule(String roomName, double price, int stayLength) {
        this.roomName = roomName;
        this.price = price;
        this.stayLength = stayLength;
    }

    public RoomsModule(int id, String roomName) {
        this.id = id;
        this.roomName = roomName;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public int getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(int roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStayLength() {
        return stayLength;
    }

    public void setStayLength(int stayLength) {
        this.stayLength = stayLength;
    }

    @Override
    public String toString() {
        return roomName;
    }
}
