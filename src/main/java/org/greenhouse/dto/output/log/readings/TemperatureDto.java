package org.greenhouse.dto.output.log.readings;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.sql.Timestamp;
import org.greenhouse.dto.output.greenhouse.GreenhouseDto;
import org.greenhouse.dto.output.log.ReceiveLogDto;
import org.greenhouse.dto.output.sensor.SensorsDto;
import org.greenhouse.entity.log.readings.Temperature;

public record TemperatureDto(
    @JsonProperty("id") Integer id,
    @JsonProperty("value") Integer value,
    @JsonProperty("receiveTime") Timestamp receiveTime,
    @JsonProperty("receiveLogs") ReceiveLogDto receiveLogs,
    @JsonProperty("sensor") SensorsDto sensor,
    @JsonProperty("greenhouse") GreenhouseDto greenhouse) {
  public static TemperatureDto fromTemperature(Temperature temperature) {
    return new TemperatureDto(
        temperature.getId(),
        temperature.getValue(),
        temperature.getReceiveTime(),
        ReceiveLogDto.fromReceiveLogs(temperature.getReceiveLogs()),
        SensorsDto.fromSensors(temperature.getSensor()),
        GreenhouseDto.fromGreenhouse(temperature.getGreenhouse()));
  }
}
