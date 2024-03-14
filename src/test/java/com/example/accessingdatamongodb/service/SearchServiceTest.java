package com.example.accessingdatamongodb.service;

import com.example.accessingdatamongodb.entity.PayloadEntity;
import com.example.accessingdatamongodb.repository.PayloadRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class SearchServiceTest {

    @Mock
    private KafkaTemplate<String, PayloadEntity> kafkaTemplate;

    @Mock
    private PayloadRepository searchRepository;

    @InjectMocks
    private SearchService searchService;

    @Test
    void testSendToKafka() {
        PayloadEntity request = new PayloadEntity("1", "generatedSearchId", "1234",LocalDate.now(), LocalDate.now(), Arrays.asList(30, 29, 1, 3));
        when(kafkaTemplate.send(eq("hotel_availability_searches"), eq("1234"), eq(request))).thenReturn(null);

        String searchId = searchService.sendToKafka(request);

        assertEquals("generatedSearchId", searchId);
        verify(kafkaTemplate, times(1)).send(eq("hotel_availability_searches"), eq("1234"), eq(request));
    }

    @Test
    void testGetSearch() {
        PayloadEntity expectedSearch = new PayloadEntity("1", "searchId123", "1234", LocalDate.now(), LocalDate.now(), Arrays.asList(30, 29, 1, 3));
        when(searchRepository.findBySearchId("searchId123")).thenReturn(expectedSearch);

        PayloadEntity result = searchService.getSearch("searchId123");

        assertEquals(expectedSearch, result);
        verify(searchRepository, times(1)).findBySearchId("searchId123");
    }

    @Test
    void testGetSimilarSearchCountWithSearchId() {
        when(searchRepository.countBySearchId("searchId123")).thenReturn(5);

        int count = searchService.getSimilarSearchCount("searchId123");

        assertEquals(5, count);
        verify(searchRepository, times(1)).countBySearchId("searchId123");
    }

    @Test
    void testGetSimilarSearchCountWithSearchObject() {
        PayloadEntity search = new PayloadEntity("1", "searchId123", "1234", LocalDate.of(2023, 12, 29), LocalDate.of(2023, 12, 31), Arrays.asList(30, 29, 1, 3));

        List<PayloadEntity> similarSearches = new ArrayList<>();
        similarSearches.add(new PayloadEntity("id1", "searchId1", "1234", LocalDate.of(2023, 12, 29), LocalDate.of(2023, 12, 31), Arrays.asList(30, 29, 1, 3)));
        similarSearches.add(new PayloadEntity("id2", "searchId2", "1234", LocalDate.of(2023, 12, 29), LocalDate.of(2023, 12, 31), Arrays.asList(30, 29, 1, 3)));
        when(searchService.findSimilarSearches(search)).thenReturn(similarSearches);

        List<PayloadEntity> result = searchService.findSimilarSearches(search);

        assertEquals(2, result.size()); 
    }

}