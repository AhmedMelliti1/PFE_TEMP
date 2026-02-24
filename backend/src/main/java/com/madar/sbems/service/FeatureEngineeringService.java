package com.madar.sbems.service;

import com.madar.sbems.entity.EnergyReading;
import com.madar.sbems.entity.PredictionRequest;
import com.madar.sbems.repository.EnergyReadingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.web.bind.annotation.RestController;
import com.madar.sbems.repository.WeatherRepository;
import com.madar.sbems.entity.WeatherReading;

@Service
public class FeatureEngineeringService {

    @Autowired
    private EnergyReadingRepository repository;
    @Autowired
    private WeatherRepository weatherRepository;

    public PredictionRequest generateFeatures() {

        List<EnergyReading> readings = repository.findLast24Readings();

        if (readings.size() < 24) {
            throw new RuntimeException("Not enough data to compute features");
        }

        EnergyReading latest = readings.get(0);
        WeatherReading latestWeather = weatherRepository.findTopByOrderByTimestampDesc();

        LocalDateTime timestamp = latest.getTimestamp();

        int hour = timestamp.getHour();
        int dayofweek = timestamp.getDayOfWeek().getValue();
        int month = timestamp.getMonthValue();
        int is_weekend = (dayofweek == 6 || dayofweek == 7) ? 1 : 0;

        double cons_h_1 = readings.get(0).getConsumption();
        double cons_h_24 = readings.get(23).getConsumption();

        double conso_moy_6h = readings.stream()
                .limit(6)
                .mapToDouble(EnergyReading::getConsumption)
                .average()
                .orElse(0);

        double conso_moy_24h = readings.stream()
                .limit(24)
                .mapToDouble(EnergyReading::getConsumption)
                .average()
                .orElse(0);

        PredictionRequest request = new PredictionRequest();

        request.setAir_temperature(latestWeather.getAirTemperature());
        request.setDew_temperature(latestWeather.getDewTemperature());
        request.setHour(hour);
        request.setDayofweek(dayofweek);
        request.setMonth(month);
        request.setIs_weekend(is_weekend);
        request.setCons_h_1(cons_h_1);
        request.setCons_h_24(cons_h_24);
        request.setConso_moy_6h(conso_moy_6h);
        request.setConso_moy_24h(conso_moy_24h);

        return request;
    }
}
