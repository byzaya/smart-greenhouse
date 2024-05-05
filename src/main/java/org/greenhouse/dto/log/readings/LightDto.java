package org.greenhouse.dto.log.readings;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.sql.Timestamp;
import org.greenhouse.dto.greenhouse.GreenhouseDto;
import org.greenhouse.dto.log.ReceiveLogDto;
import org.greenhouse.entity.log.readings.Light;

public record LightDto(
    @JsonProperty("id") Integer id,
    @JsonProperty("value") Integer value,
    @JsonProperty("receiveTime") Timestamp receiveTime,
    @JsonProperty("receiveLogs") ReceiveLogDto receiveLogs,
    @JsonProperty("greenhouse") GreenhouseDto greenhouse) {
  public static LightDto fromLight(Light light) {
    return new LightDto(
        light.getId(),
        light.getValue(),
        light.getReceiveTime(),
        ReceiveLogDto.fromReceiveLogs(light.getReceiveLogs()),
        GreenhouseDto.fromGreenhouse(light.getGreenhouse()));
  }
}
