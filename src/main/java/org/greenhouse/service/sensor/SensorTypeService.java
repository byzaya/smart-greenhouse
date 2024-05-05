package org.greenhouse.service.sensor;

import lombok.RequiredArgsConstructor;
import org.greenhouse.dto.sensor.SensorTypeDto;
import org.greenhouse.entity.sensor.SensorType;
import org.greenhouse.repository.sensor.SensorsTypeRepository;
import org.greenhouse.service.ValidationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SensorTypeService {

  private final SensorsTypeRepository sensorTypeRepository;
  private final ValidationService validationService;

  // TODO валидация
  // добавление типа датчика
  @Transactional
  public SensorTypeDto createSensorType(SensorTypeDto sensorTypeDto) {
    SensorType sensorType = new SensorType();
    sensorType.setSensorName(sensorTypeDto.sensorName());
    SensorType savedSensorType = sensorTypeRepository.save(sensorType);
    return SensorTypeDto.fromSensorType(savedSensorType);
  }

  // получение типа датчика
  @Transactional(readOnly = true)
  public SensorTypeDto getSensorTypeById(Long id) {
    SensorType sensorType = validationService.getSensorTypeOrThrow(id);
    return SensorTypeDto.fromSensorType(sensorType);
  }
}
