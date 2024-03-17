package com.example.accessingdatamongodb.service;

import com.example.accessingdatamongodb.entity.PayloadEntity;

import java.util.List;

public interface ISearchService {

    /**
     * Envía la búsqueda al sistema de mensajería Kafka.
     *
     * @param request La búsqueda a enviar.
     * @return El ID generado para la búsqueda.
     */
    String sendToKafka(PayloadEntity request);

    /**
     * Cuenta el número de búsquedas similares basadas en el ID de la búsqueda.
     *
     * @param searchId El ID de la búsqueda.
     * @return El número de búsquedas similares.
     */
    int countSimilarSearches(PayloadEntity searchId);

    /**
     * Obtiene la búsqueda correspondiente al ID proporcionado.
     *
     * @param searchId El ID de la búsqueda.
     * @return La búsqueda correspondiente al ID.
     */
    PayloadEntity getSearch(String searchId);

    
    /**
     * Valida las fechas de la búsqueda.
     *
     * @param search La búsqueda a validar.
     * @return true si las fechas son válidas, false de lo contrario.
     */
    boolean validateSearchDates(PayloadEntity search);
}
