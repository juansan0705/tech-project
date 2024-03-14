package com.example.accessingdatamongodb.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.accessingdatamongodb.entity.PayloadEntity;
import com.example.accessingdatamongodb.entity.SearchResponse;
import com.example.accessingdatamongodb.entity.SearchResult;
import com.example.accessingdatamongodb.service.SearchService;

@RestController
public class SearchController {

    @Autowired
    private SearchService searchService;
    

    @PostMapping("/search")
    public ResponseEntity<SearchResponse> search(@RequestBody PayloadEntity request) {
        String searchId = searchService.sendToKafka(request);
        SearchResponse response = new SearchResponse(searchId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<SearchResult> count(@RequestParam String searchId) {
        // Obtener la búsqueda correspondiente al searchId
        PayloadEntity search = searchService.getSearch(searchId);

        // Verificar si la búsqueda es nula
        if (search == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Validar las fechas de checkIn y checkOut
        LocalDate checkIn = search.getCheckIn();
        LocalDate checkOut = search.getCheckOut();
        if (checkOut.isBefore(checkIn)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Obtener las búsquedas similares
        List<PayloadEntity> similarSearches = searchService.findSimilarSearches(search);
        int count = similarSearches.size();

        SearchResult result = new SearchResult(searchId, search, count);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
}