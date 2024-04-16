package org.greenhouse.repository.sensor;

import org.greenhouse.entity.sensor.Sensors;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorsRepository extends JpaRepository<Sensors, Integer> {}
