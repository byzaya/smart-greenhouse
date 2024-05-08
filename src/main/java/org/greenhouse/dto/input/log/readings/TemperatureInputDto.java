package org.greenhouse.dto.input.log.readings;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.sql.Timestamp;
import org.greenhouse.dto.input.log.ReceiveLogInputDto;
import org.greenhouse.entity.log.readings.Temperature;

public record TemperatureInputDto(
    @JsonProperty("value") Integer value,
    @JsonProperty("receiveTime") Timestamp receiveTime,
    @JsonProperty("receiveLogs") ReceiveLogInputDto receiveLogs,
    @JsonProperty("sensorId") Long sensorId,
    @JsonProperty("greenhouseId") Long greenhouseId) {
  public static TemperatureInputDto fromTemperature(Temperature temperature) {
    return new TemperatureInputDto(
        temperature.getValue(),
        temperature.getReceiveTime(),
        ReceiveLogInputDto.fromReceiveLogs(temperature.getReceiveLogs()),
        temperature.getSensor().getId(),
        temperature.getGreenhouse().getId());
  }
}
