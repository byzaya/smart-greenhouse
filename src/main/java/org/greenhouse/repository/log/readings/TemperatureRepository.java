package org.greenhouse.repository.log.readings;

import java.util.List;
import org.greenhouse.entity.log.readings.Temperature;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemperatureRepository extends JpaRepository<Temperature, Long> {
  List<Temperature> findTop10ByGreenhouseIdOrderByReceiveTimeDesc(Long greenhouseId);
}
