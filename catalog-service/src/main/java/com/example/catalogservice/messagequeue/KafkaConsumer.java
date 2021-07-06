package com.example.catalogservice.messagequeue;

import com.example.catalogservice.entity.Catalog;
import com.example.catalogservice.respository.CatalogRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {

    private final CatalogRepository catalogRepository;

    @KafkaListener(topics = "example-catalog-topic")
    public void updateQty(String kafkaMessage) {
        log.info("Kafka Message : -> ", kafkaMessage);

        Map<Object, Object> map = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        // deserialize
        try {
            map = objectMapper.readValue(
                kafkaMessage, new TypeReference<Map<Object, Object>>() {
                }
            );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        Catalog findCatalog = catalogRepository.findByProductId((String) map.get("productId"));

        if (findCatalog != null) {
            findCatalog.setStock(findCatalog.getStock() - (Integer) map.get("qty"));
            catalogRepository.save(findCatalog);
        }
    }
}
