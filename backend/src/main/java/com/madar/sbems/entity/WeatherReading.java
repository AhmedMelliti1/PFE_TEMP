package com.madar.sbems.entity;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "weather_data")
@Data
public class WeatherReading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "site_id")
    private Integer siteId;

    private LocalDateTime timestamp;

    @Column(name = "air_temperature")
    private Double airTemperature;

    @Column(name = "dew_temperature")
    private Double dewTemperature;

    @Column(name = "cloud_coverage")
    private Double cloudCoverage;

    @Column(name = "wind_speed")
    private Double windSpeed;
}