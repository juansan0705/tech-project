package com.example.accessingdatamongodb.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.example.accessingdatamongodb.entity.PayloadEntity;
import com.example.accessingdatamongodb.repository.PayloadRepository;

@Component
public class KafkaConsumer {

    @Autowired
    private PayloadRepository searchRepository;

    @KafkaListener(topics = "hotel_availability_searches", groupId = "group_id")
    public void consume(PayloadEntity request) {
        searchRepository.save(request);
    }
    
}