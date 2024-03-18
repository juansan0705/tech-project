package com.example.accessingdatamongodb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.accessingdatamongodb.dto.PayloadDTO;
import com.example.accessingdatamongodb.entity.PayloadEntity;
import com.example.accessingdatamongodb.service.impl.SearchServiceImpl;

@RestController
public class SearchController {

    @Autowired
    private SearchServiceImpl searchService;
    

    @PostMapping("/search")
    public ResponseEntity<String> search(@RequestBody PayloadDTO requestDTO) {
        // Convertir el DTO en una entidad
        PayloadEntity payloadEntity = requestDTO.toEntity();
        // Enviar la entidad al servicio para procesar la búsqueda
        String searchId = searchService.sendToKafka(payloadEntity);
        return ResponseEntity.ok(searchId);
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> count(@RequestParam String searchId) {
        // Obtener la búsqueda correspondiente al searchId
        PayloadEntity search = searchService.getSearch(searchId);
        if (search == null) {
            // Manejar el caso en que no se encuentre la búsqueda
            return ResponseEntity.notFound().build();
        }
        // Obtener el número de búsquedas similares
        int count = searchService.countSimilarSearches(search);
        return ResponseEntity.ok(count);
    }

    
}