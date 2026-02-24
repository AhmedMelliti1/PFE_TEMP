package com.madar.sbems.repository;

import com.madar.sbems.entity.WeatherReading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

@Repository

public interface WeatherRepository extends JpaRepository<WeatherReading, Long> {

    @Query(value = "SELECT * FROM weather_data ORDER BY timestamp DESC LIMIT 1", nativeQuery = true)
    WeatherReading findTopByOrderByTimestampDesc();
}
