package org.greenhouse.dto.input.sensor;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.greenhouse.entity.sensor.Sensors;

public record SensorsInputDto(
    @JsonProperty("isActive") Boolean isActive,
    @JsonProperty("sensorNumber") Integer sensorNumber,
    @JsonProperty("sensorTypeId") Long sensorTypeId,
    @JsonProperty("greenhouseId") Long greenhouseId) {
  public static SensorsInputDto fromSensors(Sensors sensors) {
    return new SensorsInputDto(
        sensors.getIsActive(),
        sensors.getSensorNumber(),
        sensors.getSensorType().getId(),
        sensors.getGreenhouse().getId());
  }
}
