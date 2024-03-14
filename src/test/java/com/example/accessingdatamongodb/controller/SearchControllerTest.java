package com.example.accessingdatamongodb.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.accessingdatamongodb.entity.PayloadEntity;
import com.example.accessingdatamongodb.entity.SearchResponse;
import com.example.accessingdatamongodb.entity.SearchResult;
import com.example.accessingdatamongodb.service.SearchService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@SpringBootTest
public class SearchControllerTest {

	@Mock
    private SearchService searchService;

    @InjectMocks
    private SearchController searchController;

    @Test
    void testSearch() {
    	PayloadEntity request = new PayloadEntity("id", "searchId", "hotelId", LocalDate.now(), LocalDate.now(), Arrays.asList(30, 29, 1, 3));
        when(searchService.sendToKafka(request)).thenReturn("searchId123");

        ResponseEntity<SearchResponse> responseEntity = searchController.search(request);
        SearchResponse response = responseEntity.getBody();

        assertNotNull(response);
        assertEquals("searchId123", response.getSearchId());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(searchService, times(1)).sendToKafka(request);
    }

    @Test
    void testCountWithValidSearch() {
        PayloadEntity search = new PayloadEntity("id", "searchId", "hotelId", LocalDate.now(), LocalDate.now(), Arrays.asList(30, 29, 1, 3));
        when(searchService.getSearch("searchId123")).thenReturn(search);

        List<PayloadEntity> similarSearches = new ArrayList<>();
        similarSearches.add(new PayloadEntity("id1", "searchId1", "hotelId", LocalDate.now(), LocalDate.now(), Arrays.asList(30, 29, 1, 3)));
        similarSearches.add(new PayloadEntity("id2", "searchId2", "hotelId", LocalDate.now(), LocalDate.now(), Arrays.asList(30, 29, 1, 3)));
        when(searchService.findSimilarSearches(search)).thenReturn(similarSearches);

        ResponseEntity<SearchResult> responseEntity = searchController.count("searchId123");
        SearchResult result = responseEntity.getBody();

        assertNotNull(result);
        assertEquals("searchId123", result.getSearchId());
        assertEquals(search, result.getSearch());
        assertEquals(2, result.getCount()); 
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(searchService, times(1)).getSearch("searchId123");
        verify(searchService, times(1)).findSimilarSearches(search); 
    }

    @Test
    void testCountWithInvalidSearch() {
        when(searchService.getSearch("invalidSearchId")).thenReturn(null);

        ResponseEntity<SearchResult> responseEntity = searchController.count("invalidSearchId");

        assertNull(responseEntity.getBody());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        verify(searchService, times(1)).getSearch("invalidSearchId");
    }
}
