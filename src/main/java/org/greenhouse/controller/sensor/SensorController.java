package org.greenhouse.controller.sensor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.greenhouse.dto.input.sensor.SensorsInputDto;
import org.greenhouse.dto.output.sensor.SensorsDto;
import org.greenhouse.service.sensor.SensorService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sensor")
@RequiredArgsConstructor
@Tag(name = "Датчик")
public class SensorController {

  private final SensorService sensorService;

  @Operation(
      summary = "Добавление датчика",
      description = "Добавление нового датчика в бд пользователем")
  @CrossOrigin(origins = "${cors-address}")
  @PostMapping("/add")
  public SensorsDto createSensor(@RequestBody SensorsInputDto sensorDto) {
    return sensorService.createSensor(sensorDto);
  }

  @Operation(summary = "Получение датчика", description = "Получение датчика по его id")
  @CrossOrigin(origins = "${cors-address}")
  @GetMapping("/{id}")
  public SensorsDto getSensorById(@PathVariable Long id) {
    return sensorService.getSensorById(id);
  }

  @Operation(summary = "Удаление датчика", description = "Удаление датчика по его id")
  @CrossOrigin(origins = "${cors-address}")
  @DeleteMapping("/{id}")
  public void deleteSensorById(@PathVariable Long id) {
    sensorService.deleteSensorById(id);
  }

  @Operation(
      summary = "Получение списка активных датчиков",
      description = "Получение списка активных датчиков по id теплицы")
  @CrossOrigin(origins = "${cors-address}")
  @GetMapping("/{id}/active")
  public List<SensorsDto> getActiveSensorsByGreenhouseId(@PathVariable Long id) {
    return sensorService.getActiveSensorsByGreenhouseId(id);
  }

  @Operation(
      summary = "Получение списка неактивных датчиков",
      description = "Получение списка неактивных датчиков по id теплицы")
  @CrossOrigin(origins = "${cors-address}")
  @GetMapping("/{id}/inactive")
  public List<SensorsDto> getNotActiveSensorsByGreenhouseId(@PathVariable Long id) {
    return sensorService.getNotActiveSensorsByGreenhouseId(id);
  }
}
