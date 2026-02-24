package com.madar.sbems.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "energy_readings")
@Data // Lombok génère les getters/setters
public class EnergyReading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "building_id")
    private Integer buildingId;


    private Integer meter; // 0: Elec, 1: Eau...

    private LocalDateTime timestamp;

    @Column(name = "meter_reading")
    private Double meterReading;

    public Double getConsumption() {
        return meterReading;
    }

    public void setConsumption(Double consumption) {
        this.meterReading = consumption;
    }

    

    
}
