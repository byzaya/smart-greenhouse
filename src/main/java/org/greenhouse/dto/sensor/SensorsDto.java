package org.greenhouse.dto.sensor;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.greenhouse.dto.greenhouse.GreenhouseDto;
import org.greenhouse.entity.sensor.Sensors;

public record SensorsDto(
    @JsonProperty("id") Long id,
    @JsonProperty("isActive") Boolean isActive,
    @JsonProperty("sensorNumber") Integer sensorNumber,
    @JsonProperty("sensorType") SensorTypeDto sensorType,
    @JsonProperty("greenhouse") GreenhouseDto greenhouse
) {
  public static SensorsDto fromSensors(Sensors sensors) {
    return new SensorsDto(
        sensors.getId(),
        sensors.getIsActive(),
        sensors.getSensorNumber(),
        SensorTypeDto.fromSensorType(sensors.getSensorType()),
        GreenhouseDto.fromGreenhouse(sensors.getGreenhouse())
    );
  }
}
