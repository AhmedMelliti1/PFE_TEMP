package com.madar.sbems.repository;

import com.madar.sbems.entity.EnergyReading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnergyReadingRepository extends JpaRepository<EnergyReading, Long> {
}