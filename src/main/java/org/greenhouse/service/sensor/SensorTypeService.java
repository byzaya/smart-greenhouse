package org.greenhouse.service.sensor;

import lombok.RequiredArgsConstructor;
import org.greenhouse.dto.sensor.SensorTypeDto;
import org.greenhouse.entity.sensor.SensorType;
import org.greenhouse.exception.message.SensorTypeNotFoundException;
import org.greenhouse.repository.sensor.SensorsTypeRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SensorTypeService {

  private final SensorsTypeRepository sensorTypeRepository;

  public SensorTypeDto createSensorType(SensorTypeDto sensorTypeDto) {
    SensorType sensorType = new SensorType();
    sensorType.setSensorName(sensorTypeDto.sensorName());
    SensorType savedSensorType = sensorTypeRepository.save(sensorType);
    return SensorTypeDto.fromSensorType(savedSensorType);
  }

  public SensorTypeDto getSensorTypeById(Long id) {
    SensorType sensorType =
        sensorTypeRepository
            .findById(id)
            .orElseThrow(
                () -> new SensorTypeNotFoundException("SensorType not found with ID: " + id));
    return SensorTypeDto.fromSensorType(sensorType);
  }
}
