package org.greenhouse.controller.sensor;

import lombok.RequiredArgsConstructor;
import org.greenhouse.dto.sensor.SensorsDto;
import org.greenhouse.service.sensor.SensorService;
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
public class SensorController {

  private final SensorService sensorService;
  @PostMapping("/add")
  public SensorsDto createSensor(@RequestBody SensorsDto sensorDto) {
    return sensorService.createSensor(sensorDto);
  }

  @GetMapping("/{id}")
  public SensorsDto getSensorById(@PathVariable Long id) {
    return sensorService.getSensorById(id);
  }

  @DeleteMapping("/{id}")
  public void deleteSensorById(@PathVariable Long id) {
    sensorService.deleteSensorById(id);
  }
}
