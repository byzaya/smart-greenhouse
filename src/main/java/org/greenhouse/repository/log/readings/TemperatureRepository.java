package org.greenhouse.repository.log.readings;

import org.greenhouse.entity.log.readings.Temperature;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemperatureRepository extends JpaRepository<Temperature, Integer> {}
