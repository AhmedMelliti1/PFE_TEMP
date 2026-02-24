package com.madar.sbems.repository;

import com.madar.sbems.entity.EnergyReading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
@Repository
public interface EnergyReadingRepository extends JpaRepository<EnergyReading, Long> {
    

@Query(value = "SELECT * FROM energy_readings ORDER BY timestamp DESC LIMIT 24", nativeQuery = true)
List<EnergyReading> findLast24Readings();
}