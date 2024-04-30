package org.greenhouse.repository.sensor;

import org.greenhouse.entity.sensor.SensorType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorsTypeRepository extends JpaRepository<SensorType, Long> {}
