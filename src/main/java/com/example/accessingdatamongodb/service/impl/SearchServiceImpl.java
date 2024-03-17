package com.example.accessingdatamongodb.service.impl;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.example.accessingdatamongodb.entity.PayloadEntity;
import com.example.accessingdatamongodb.repository.PayloadRepository;
import com.example.accessingdatamongodb.service.ISearchService;

import jakarta.annotation.Nonnull;

@Service
public class SearchServiceImpl implements ISearchService{

    @Autowired
    private KafkaTemplate<String, PayloadEntity> kafkaTemplate;

    @Autowired
    private PayloadRepository searchRepository;
    
    @Override
    public String sendToKafka(@Nonnull PayloadEntity request) {
    	
    	// Verificar que el payload no sea nulo
        if (request == null) {
            throw new IllegalArgumentException("Payload cannot be null");
        }
        
        // Verificar la validez de las fechas de búsqueda
        if (!validateSearchDates(request)) {
            throw new IllegalArgumentException("Invalid search dates: checkOut is before checkIn");
        }
        
        // Generar un ID único para la búsqueda
        String generatedSearchId = UUID.randomUUID().toString();
        
        // Crear una nueva instancia de PayloadEntity con el ID generado
        PayloadEntity entityWithId = new PayloadEntity(generatedSearchId, request.getSearchId(), request.getHotelId(), 
                                                       request.getCheckIn(), request.getCheckOut(), request.getAges());
        
        // Enviar la entidad con el ID generado a Kafka
        kafkaTemplate.send("hotel_availability_searches", entityWithId.getHotelId(), entityWithId);
        
        // Devolver el ID generado
        return generatedSearchId;
    }


    
    @Override
    public PayloadEntity getSearch(String searchId) {
        PayloadEntity search = searchRepository.findBySearchId(searchId);
        if (search != null && !validateSearchDates(search)) {
            throw new IllegalArgumentException("Invalid search dates: checkOut is before checkIn");
        }
        return search;
    }
   

    @Override
    public int countSimilarSearches(PayloadEntity search) {
    	if (!validateSearchDates(search)) {
            throw new IllegalArgumentException("Invalid search dates: checkOut is before checkIn");
        }
        int count = searchRepository.countByHotelIdAndCheckAndCheckOutAndAges(
                search.getHotelId(),
                search.getCheckIn(),
                search.getCheckOut(),
                search.getAges()
        );
        return count;
    }

	@Override
	public boolean validateSearchDates(PayloadEntity search) {
		LocalDate checkIn = search.getCheckIn();
        LocalDate checkOut = search.getCheckOut();
        
        if (checkOut.isBefore(checkIn)) {
            return false;
        }
        
        return true;
    }

    
}

