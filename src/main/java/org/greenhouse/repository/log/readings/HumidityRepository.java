package org.greenhouse.repository.log.readings;

import java.util.List;
import org.greenhouse.entity.log.readings.Humidity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HumidityRepository extends JpaRepository<Humidity, Long> {
  List<Humidity> findTop10BySeedBedIdOrderByReceiveTimeDesc(Long seedBedId);
}
