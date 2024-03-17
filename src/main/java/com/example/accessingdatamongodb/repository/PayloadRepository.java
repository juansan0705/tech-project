package com.example.accessingdatamongodb.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.accessingdatamongodb.entity.PayloadEntity;

public interface PayloadRepository extends MongoRepository<PayloadEntity, String> {
	
    int countBySearchId(String searchId);
    
    PayloadEntity findBySearchId(String searchId); 
    
    @Query(value = "{ 'hotelId' : ?0, 'checkIn' : ?1, 'checkOut' : ?2, 'ages' : { $in: ?3 } }", count = true)
    int countByHotelIdAndCheckAndCheckOutAndAges(String hotelId, LocalDate checkIn, LocalDate checkOut, List<Integer> ages);
    
    @Query("{ 'hotelId' : ?0, 'checkIn' : ?1, 'checkOut' : ?2, 'ages' : { $in: ?3 } }")
    List<PayloadEntity> findSimilarSearches(String hotelId, LocalDate checkIn, LocalDate checkOut, List<Integer> ages);
    
}