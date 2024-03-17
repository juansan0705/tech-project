package com.example.accessingdatamongodb.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.example.accessingdatamongodb.dto.PayloadDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

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
    
    // Constructor para crear una entidad con un ID de búsqueda específico
    public PayloadEntity(String id, String searchId, String hotelId, LocalDate checkIn, LocalDate checkOut, List<Integer> ages) {
        this.id = id;
        this.searchId = searchId;
        this.hotelId = hotelId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.ages = ages;
    }
    
    // Constructor adicional para crear una entidad con un ID de búsqueda generado automáticamente
    public PayloadEntity(String hotelId, LocalDate check, LocalDate checkOut, List<Integer> ages) {
        this(UUID.randomUUID().toString(), null, hotelId, check, checkOut, ages);
    }
    
    // Constructor que acepta un PayloadDTO y crea una entidad correspondiente
    public PayloadEntity(PayloadDTO dto) {
        this.id = null; // El ID se generará automáticamente
        this.searchId = null; // El ID de búsqueda se establecerá en null
        this.hotelId = dto.getHotelId();
        this.checkIn = dto.getCheckIn();
        this.checkOut = dto.getCheckOut();
        this.ages = dto.getAges();
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