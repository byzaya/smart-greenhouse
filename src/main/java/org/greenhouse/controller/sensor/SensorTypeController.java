package org.greenhouse.controller.sensor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.greenhouse.dto.input.sensor.SensorTypeInputDto;
import org.greenhouse.dto.output.sensor.SensorTypeDto;
import org.greenhouse.service.sensor.SensorTypeService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sensor-type")
@RequiredArgsConstructor
@Tag(name = "Тип датчика")
public class SensorTypeController {

  private final SensorTypeService sensorTypeService;

  @Operation(
      summary = "Добавление типа датчика",
      description = "Добавление нового типа датчика в бд пользователем")
//  @CrossOrigin(origins = "${cors-address}")
  @PostMapping("/add")
  public SensorTypeDto createSensorType(@RequestBody SensorTypeInputDto sensorTypeDto) {
    return sensorTypeService.createSensorType(sensorTypeDto);
  }

  @Operation(
      summary = "Получение типа датчика",
      description = "Получение типа датчика по его id")
//  @CrossOrigin(origins = "${cors-address}")
  @GetMapping("/{id}")
  public SensorTypeDto getSensorTypeById(@PathVariable Long id) {
    return sensorTypeService.getSensorTypeById(id);
  }
}
