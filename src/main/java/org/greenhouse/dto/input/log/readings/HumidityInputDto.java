package org.greenhouse.dto.input.log.readings;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.sql.Timestamp;
import org.greenhouse.dto.output.greenhouse.SeedBedDto;
import org.greenhouse.dto.input.log.ReceiveLogInputDto;
import org.greenhouse.dto.output.sensor.SensorsDto;
import org.greenhouse.entity.log.readings.Humidity;

public record HumidityInputDto(
    @JsonProperty("value") Integer value,
    @JsonProperty("receiveTime") Timestamp receiveTime,
    @JsonProperty("receiveLogs") ReceiveLogInputDto receiveLogs,
    @JsonProperty("sensorId") Long sensorId,
    @JsonProperty("seedBedId") Long seedBedId) {
  public static HumidityInputDto fromHumidity(Humidity humidity) {
    return new HumidityInputDto(
        humidity.getValue(),
        humidity.getReceiveTime(),
        ReceiveLogInputDto.fromReceiveLogs(humidity.getReceiveLogs()),
        humidity.getSensor().getId(),
        humidity.getSeedBed().getId());
  }
}
