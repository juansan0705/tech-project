package com.example.accessingdatamongodb.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.example.accessingdatamongodb.entity.PayloadEntity;
import com.example.accessingdatamongodb.repository.PayloadRepository;

import jakarta.annotation.Nonnull;

@Service
public class SearchService {

    @Autowired
    private KafkaTemplate<String, PayloadEntity> kafkaTemplate;

    @Autowired
    private PayloadRepository searchRepository;

    public String sendToKafka(@Nonnull PayloadEntity request) {
        kafkaTemplate.send("hotel_availability_searches", request.getHotelId(), request);
        return "generatedSearchId";
    }

    public int getSimilarSearchCount(String searchId) {
        return searchRepository.countBySearchId(searchId);
    }
    
    public PayloadEntity getSearch(String searchId) {
        return searchRepository.findBySearchId(searchId);
    }
    
    
    public List<PayloadEntity> findSimilarSearches(PayloadEntity search) {
        List<PayloadEntity> similarSearches = searchRepository.findSimilarSearches(
                search.getHotelId(),
                search.getCheckIn(),
                search.getCheckOut(),
                search.getAges()
        );

        return similarSearches;
    }

    
}

