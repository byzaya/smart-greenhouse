package org.greenhouse.dto.input.sensor;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.greenhouse.entity.sensor.SensorType;

public record SensorTypeInputDto(@JsonProperty("sensorName") String sensorName) {
  public static SensorTypeInputDto fromSensorType(SensorType sensorType) {
    return new SensorTypeInputDto(sensorType.getSensorName());
  }
}
