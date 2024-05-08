package org.greenhouse.dto.output.log.readings;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.sql.Timestamp;
import org.greenhouse.dto.output.greenhouse.GreenhouseDto;
import org.greenhouse.dto.output.log.ReceiveLogDto;
import org.greenhouse.dto.output.sensor.SensorsDto;
import org.greenhouse.entity.log.readings.Light;

public record LightDto(
    @JsonProperty("id") Integer id,
    @JsonProperty("value") Integer value,
    @JsonProperty("receiveTime") Timestamp receiveTime,
    @JsonProperty("receiveLogs") ReceiveLogDto receiveLogs,
    @JsonProperty("sensor") SensorsDto sensor,
    @JsonProperty("greenhouse") GreenhouseDto greenhouse) {
  public static LightDto fromLight(Light light) {
    return new LightDto(
        light.getId(),
        light.getValue(),
        light.getReceiveTime(),
        ReceiveLogDto.fromReceiveLogs(light.getReceiveLogs()),
        SensorsDto.fromSensors(light.getSensor()),
        GreenhouseDto.fromGreenhouse(light.getGreenhouse()));
  }
}
