package com.example.finalproject;

import java.time.LocalDate;

public class NewReservationModule {
    int id;

    int roomId;
    LocalDate checkInDate;
    LocalDate checkOutDate;
    LocalDate checkedInTime;

    public NewReservationModule(int id, int roomId, LocalDate checkInDate, LocalDate checkOutDate, LocalDate checkedInTime) {
        this.id = id;
        this.roomId = roomId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.checkedInTime = checkedInTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public LocalDate getCheckedInTime() {
        return checkedInTime;
    }

    public void setCheckedInTime(LocalDate checkedInTime) {
        this.checkedInTime = checkedInTime;
    }
}
