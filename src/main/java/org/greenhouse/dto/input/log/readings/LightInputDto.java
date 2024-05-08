package org.greenhouse.dto.input.log.readings;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.sql.Timestamp;
import org.greenhouse.dto.input.log.ReceiveLogInputDto;
import org.greenhouse.entity.log.readings.Light;

public record LightInputDto(
    @JsonProperty("value") Integer value,
    @JsonProperty("receiveTime") Timestamp receiveTime,
    @JsonProperty("receiveLogs") ReceiveLogInputDto receiveLogs,
    @JsonProperty("sensorId") Long sensorId,
    @JsonProperty("greenhouseId") Long greenhouseId) {
  public static LightInputDto fromLight(Light light) {
    return new LightInputDto(
        light.getValue(),
        light.getReceiveTime(),
        ReceiveLogInputDto.fromReceiveLogs(light.getReceiveLogs()),
        light.getSensor().getId(),
        light.getGreenhouse().getId());
  }
}
