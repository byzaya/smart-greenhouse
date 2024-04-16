package org.greenhouse.repository.log.readings;

import org.greenhouse.entity.log.readings.Humidity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HumidityRepository extends JpaRepository<Humidity, Integer> {}
