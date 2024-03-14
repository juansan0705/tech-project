package com.example.accessingdatamongodb.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.List;

@Document(collection = "hotel_availability_searches")
public class PayloadEntity {
	
	@Id
    private final String id;
    @Field("searchId")
    private final String searchId;
    private final String hotelId;
    private final LocalDate checkIn;
    private final LocalDate checkOut;
    private final List<Integer> ages;
    
    public PayloadEntity(String id, String searchId, String hotelId, LocalDate check, LocalDate checkOut, List<Integer> ages) {
        this.id = id;
        this.searchId = searchId;
        this.hotelId = hotelId;
        this.checkIn = check;
        this.checkOut = checkOut;
        this.ages = ages;
    }
    
    public String getId() {
        return id;
    }
    
    public String getSearchId() {
        return searchId;
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
	

   
}