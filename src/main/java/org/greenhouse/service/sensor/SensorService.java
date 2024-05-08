package org.greenhouse.service.sensor;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.greenhouse.dto.output.sensor.SensorsDto;
import org.greenhouse.entity.greenhouse.Greenhouses;
import org.greenhouse.entity.sensor.Sensors;
import org.greenhouse.repository.sensor.SensorsRepository;
import org.greenhouse.service.ValidationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SensorService {

  private final SensorsRepository sensorRepository;
  private final ValidationService validationService;

  // TODO валидация
  // добавление датчика
  @Transactional
  public SensorsDto createSensor(SensorsDto sensorDto) {
    Sensors sensor = new Sensors();
    sensor.setIsActive(sensorDto.isActive());
    sensor.setSensorNumber(sensorDto.sensorNumber());
    sensor.setSensorType(validationService.getSensorTypeOrThrow(sensorDto.sensorType().id()));
    sensor.setGreenhouse(validationService.getGreenhouseOrThrow(sensorDto.greenhouse().id()));
    Sensors savedSensor = sensorRepository.save(sensor);
    return SensorsDto.fromSensors(savedSensor);
  }

  // удаление датчика
  @Transactional
  public void deleteSensorById(Long id) {
    Sensors sensor = validationService.getSensorOrThrow(id);
    sensorRepository.delete(sensor);
  }

  // получение информации о датчике
  @Transactional(readOnly = true)
  public SensorsDto getSensorById(Long id) {
    Sensors sensor = validationService.getSensorOrThrow(id);
    return SensorsDto.fromSensors(sensor);
  }

  // получение списка всех работающих датчиков теплицы
  public List<SensorsDto> getActiveSensorsByGreenhouseId(Long greenhouseId) {
    Greenhouses greenhouses = validationService.getGreenhouseOrThrow(greenhouseId);
    List<Sensors> autoSensors = greenhouses.getSensors().stream().filter(Sensors::getIsActive)
        .toList();
    return autoSensors.stream()
        .map(SensorsDto::fromSensors)
        .collect(Collectors.toList());
  }

  // получение списка всех не работающих датчиков теплицы
  public List<SensorsDto> getNotActiveSensorsByGreenhouseId(Long greenhouseId) {
    Greenhouses greenhouses = validationService.getGreenhouseOrThrow(greenhouseId);
    List<Sensors> inactiveSensors = greenhouses.getSensors().stream().filter(sensor -> !sensor.getIsActive())
        .toList();
    return inactiveSensors.stream()
        .map(SensorsDto::fromSensors)
        .collect(Collectors.toList());
  }
}
