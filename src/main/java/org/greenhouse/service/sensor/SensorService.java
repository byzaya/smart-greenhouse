package org.greenhouse.service.sensor;

import lombok.RequiredArgsConstructor;
import org.greenhouse.dto.sensor.SensorsDto;
import org.greenhouse.entity.sensor.Sensors;
import org.greenhouse.exception.message.GreenhouseNotFoundException;
import org.greenhouse.exception.message.SensorNotFoundException;
import org.greenhouse.exception.message.SensorTypeNotFoundException;
import org.greenhouse.repository.greenhouse.GreenhousesRepository;
import org.greenhouse.repository.sensor.SensorsRepository;
import org.greenhouse.repository.sensor.SensorsTypeRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SensorService {

  private final SensorsRepository sensorRepository;
  private final SensorsTypeRepository sensorTypeRepository;
  private final GreenhousesRepository greenhouseRepository;

  // TODO валидация
  // добавление датчика
  public SensorsDto createSensor(SensorsDto sensorDto) {
    Sensors sensor = new Sensors();
    sensor.setIsActive(sensorDto.isActive());
    sensor.setSensorNumber(sensorDto.sensorNumber());
    sensor.setSensorType(
        sensorTypeRepository
            .findById(sensorDto.sensorType().id())
            .orElseThrow(
                () ->
                    new SensorTypeNotFoundException(
                        "Sensor type not found with ID: " + sensorDto.sensorType().id())));
    sensor.setGreenhouse(
        greenhouseRepository
            .findById(sensorDto.greenhouse().id())
            .orElseThrow(
                () ->
                    new GreenhouseNotFoundException(
                        "Greenhouse not found with ID: " + sensorDto.greenhouse().id())));
    Sensors savedSensor = sensorRepository.save(sensor);
    return SensorsDto.fromSensors(savedSensor);
  }

  // удаление датчика
  public void deleteSensorById(Long id) {
    if (!sensorRepository.existsById(id)) {
      throw new SensorNotFoundException("Sensor not found with ID: " + id);
    }
    sensorRepository.deleteById(id);
  }

  // получение информации о датчике
  public SensorsDto getSensorById(Long id) {
    Sensors sensor =
        sensorRepository
            .findById(id)
            .orElseThrow(() -> new SensorNotFoundException("Sensor not found with ID: " + id));
    return SensorsDto.fromSensors(sensor);
  }
}
