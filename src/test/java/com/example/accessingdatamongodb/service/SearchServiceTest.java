package com.example.accessingdatamongodb.service;

import com.example.accessingdatamongodb.entity.PayloadEntity;
import com.example.accessingdatamongodb.repository.PayloadRepository;
import com.example.accessingdatamongodb.service.impl.SearchServiceImpl;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class SearchServiceTest {

    @Mock
    private KafkaTemplate<String, PayloadEntity> kafkaTemplate;

    @Mock
    private PayloadRepository searchRepository;

    @InjectMocks
    private SearchServiceImpl searchService;

    @Test
    void testSendToKafka() {
        // Arrange
        PayloadEntity request = new PayloadEntity("1", "searchId", "hotelId", LocalDate.now(), LocalDate.now(), Arrays.asList(30, 29, 1, 3));
        when(kafkaTemplate.send(anyString(), anyString(), any(PayloadEntity.class))).thenReturn(null);

        // Act
        String generatedSearchId = searchService.sendToKafka(request);

        // Assert
        assertNotNull(generatedSearchId);
        verify(kafkaTemplate, times(1)).send(anyString(), anyString(), any(PayloadEntity.class));
    }

    @Test
    void testGetSearch() {
        // Arrange
        String searchId = "searchId123";
        PayloadEntity expectedSearch = new PayloadEntity("1", searchId, "hotelId", LocalDate.now(), LocalDate.now(), Arrays.asList(30, 29, 1, 3));
        when(searchRepository.findBySearchId(searchId)).thenReturn(expectedSearch);

        // Act
        PayloadEntity result = searchService.getSearch(searchId);

        // Assert
        assertNotNull(result);
        assertEquals(expectedSearch, result);
    }

    @Test
    void testCountSimilarSearches() {
        // Arrange
        PayloadEntity search = new PayloadEntity("1", "searchId123", "hotelId", LocalDate.now(), LocalDate.now(), Arrays.asList(30, 29, 1, 3));
        when(searchRepository.countByHotelIdAndCheckAndCheckOutAndAges(anyString(), any(LocalDate.class), any(LocalDate.class), anyList())).thenReturn(5);

        // Act
        int count = searchService.countSimilarSearches(search);

        // Assert
        assertEquals(5, count);
    }

    @Test
    void testValidateSearchDates() {
        // Arrange
        PayloadEntity validSearch = new PayloadEntity("1", "searchId", "hotelId", LocalDate.now(), LocalDate.now().plusDays(1), Arrays.asList(30, 29, 1, 3));
        PayloadEntity invalidSearch = new PayloadEntity("1", "searchId", "hotelId", LocalDate.now().plusDays(1), LocalDate.now(), Arrays.asList(30, 29, 1, 3));

        // Act & Assert
        assertTrue(searchService.validateSearchDates(validSearch));
        assertFalse(searchService.validateSearchDates(invalidSearch));
    }


}