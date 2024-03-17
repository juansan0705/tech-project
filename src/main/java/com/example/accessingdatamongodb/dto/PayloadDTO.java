package com.example.accessingdatamongodb.dto;

import java.time.LocalDate;
import java.util.List;

import com.example.accessingdatamongodb.entity.PayloadEntity;

public class PayloadDTO {

    private final String hotelId;
    private final LocalDate checkIn;
    private final LocalDate checkOut;
    private final List<Integer> ages;

    public PayloadDTO(String hotelId, LocalDate checkIn, LocalDate checkOut, List<Integer> ages) {
        this.hotelId = hotelId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.ages = ages;
    }

    public String getHotelId() {
        return hotelId;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public List<Integer> getAges() {
        return ages;
    }

    public PayloadEntity toEntity() {
        return new PayloadEntity(hotelId, checkIn, checkOut, ages);
    }
}
