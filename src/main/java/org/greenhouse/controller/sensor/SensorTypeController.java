package org.greenhouse.controller.sensor;

import lombok.RequiredArgsConstructor;
import org.greenhouse.dto.sensor.SensorTypeDto;
import org.greenhouse.service.sensor.SensorTypeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sensor-type")
@RequiredArgsConstructor
public class SensorTypeController {

  private final SensorTypeService sensorTypeService;

  @PostMapping("/add")
  public SensorTypeDto createSensorType(@RequestBody SensorTypeDto sensorTypeDto) {
    return sensorTypeService.createSensorType(sensorTypeDto);
  }

  @GetMapping("/{id}")
  public SensorTypeDto getSensorTypeById(@PathVariable Long id) {
    return sensorTypeService.getSensorTypeById(id);
  }
}
