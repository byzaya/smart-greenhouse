package org.greenhouse.controller.sensor;

import lombok.RequiredArgsConstructor;
import org.greenhouse.dto.sensor.SensorsDto;
import org.greenhouse.service.sensor.SensorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
  public ResponseEntity<SensorsDto> createSensor(@RequestBody SensorsDto sensorDto) {
    SensorsDto createdSensor = sensorService.createSensor(sensorDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdSensor);
  }

  @GetMapping("/{id}")
  public ResponseEntity<SensorsDto> getSensorById(@PathVariable Long id) {
    SensorsDto sensorDto = sensorService.getSensorById(id);
    return ResponseEntity.ok(sensorDto);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteSensorById(@PathVariable Long id) {
    sensorService.deleteSensorById(id);
    return ResponseEntity.noContent().build();
  }
}
