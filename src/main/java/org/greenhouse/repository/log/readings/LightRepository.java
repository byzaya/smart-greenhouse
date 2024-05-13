package org.greenhouse.repository.log.readings;

import java.util.List;
import org.greenhouse.entity.log.readings.Light;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LightRepository extends JpaRepository<Light, Long> {
  List<Light> findTop10ByGreenhouseIdOrderByReceiveTimeDesc(Long greenhouseId);
}
