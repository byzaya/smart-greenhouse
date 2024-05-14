package org.greenhouse.dto.output.sensor;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.greenhouse.entity.sensor.SensorType;

public record SensorTypeDto(
    @JsonProperty("id") Long id, @JsonProperty("sensorName") String sensorName) {
  public static SensorTypeDto fromSensorType(SensorType sensorType) {
    return new SensorTypeDto(sensorType.getId(), sensorType.getSensorName());
  }
}
