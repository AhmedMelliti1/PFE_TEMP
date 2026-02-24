package com.madar.sbems.service;

import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.madar.sbems.entity.EnergyReading;
import com.madar.sbems.repository.EnergyReadingRepository;

@Service
public class KafkaConsumerService {
    @Autowired
    private EnergyReadingRepository repository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "sensor-data", groupId = "sbems-backend-group")
    public void consume(String message) {
        try {
            System.out.println("message Reçu : " + message);
            Map<String, Object> map = objectMapper.readValue(message, Map.class);
            EnergyReading reading = new EnergyReading();
            reading.setBuildingId((Integer) map.get("building_id"));
            reading.setMeter((Integer) map.get("meter"));
            Object val = map.get("meter_reading");
            if (val instanceof Integer) {
                reading.setMeterReading(((Integer) val).doubleValue());
            } else {
                reading.setMeterReading((Double) val);
            }
            String dateStr = (String) map.get("timestamp");
            LocalDateTime timestamp = LocalDateTime.parse(dateStr.replace("Z", ""));
            reading.setTimestamp(timestamp);
            repository.save(reading);
            System.out.println("Sauvegardé avec ID : " + reading.getId());
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
