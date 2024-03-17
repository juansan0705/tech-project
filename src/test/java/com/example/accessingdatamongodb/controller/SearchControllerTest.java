package com.example.accessingdatamongodb.controller;

import com.example.accessingdatamongodb.dto.PayloadDTO;
import com.example.accessingdatamongodb.entity.PayloadEntity;
import com.example.accessingdatamongodb.service.impl.SearchServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;

@SpringBootTest
class SearchControllerTest {

    @Mock
    private SearchServiceImpl searchService;

    @InjectMocks
    private SearchController searchController;

    @Test
    void testSearch() {
        // Datos de ejemplo para el DTO
        PayloadDTO requestDTO = new PayloadDTO("hotel123", LocalDate.now(), LocalDate.now().plusDays(3), Arrays.asList(30, 29, 1, 3));

        // Crear la entidad correspondiente al DTO
        PayloadEntity payloadEntity = new PayloadEntity(requestDTO);

        // Simular el comportamiento del servicio
        String expectedSearchId = "generatedSearchId";
        when(searchService.sendToKafka(payloadEntity)).thenReturn(expectedSearchId);

        // Llamar al método del controlador
        ResponseEntity<String> responseEntity = searchController.search(requestDTO);

        // Verificar la respuesta
        assertNotNull(responseEntity);

    }

    @Test
    void testCount() {
        String searchId = "searchId123";
        PayloadEntity search = new PayloadEntity("1", searchId, "hotel123", null, null, null);
        int expectedCount = 5;
        when(searchService.getSearch(searchId)).thenReturn(search);
        when(searchService.countSimilarSearches(search)).thenReturn(expectedCount);

        ResponseEntity<Integer> responseEntity = searchController.count(searchId);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedCount, responseEntity.getBody());
        verify(searchService, times(1)).getSearch(searchId);
        verify(searchService, times(1)).countSimilarSearches(search);
    }
}
