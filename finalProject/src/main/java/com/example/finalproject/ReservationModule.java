package com.example.finalproject;

import java.time.LocalDate;

public class ReservationModule {
    private int reservationId;
    private int roomId;
    private String roomName;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalDate checkedInTime;
    private String customerName;

    public ReservationModule(int reservationId, int roomId, String roomName, LocalDate checkInDate, LocalDate checkOutDate, LocalDate checkedInTime, String customerName) {
        this.reservationId = reservationId;
        this.roomId = roomId;
        this.roomName = roomName;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.checkedInTime = checkedInTime;
        this.customerName = customerName;
    }

    public ReservationModule(int reservationId, String roomName) {
        this.reservationId = reservationId;
        this.roomName = roomName;
    }

    public ReservationModule(LocalDate checkInDate, LocalDate checkOutDate) {
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @Override
    public String toString() {
        return roomName;
    }

    public boolean isWithinDateRange(String checkIn, String checkOut) {
        LocalDate reservationCheckIn = checkInDate;
        LocalDate reservationCheckOut = checkOutDate;
        LocalDate inputCheckIn = LocalDate.parse(checkIn);
        LocalDate inputCheckOut = LocalDate.parse(checkOut);

        return !inputCheckIn.isAfter(reservationCheckOut) && !inputCheckOut.isBefore(reservationCheckIn);
    }
}
