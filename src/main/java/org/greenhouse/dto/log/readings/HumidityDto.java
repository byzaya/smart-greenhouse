package org.greenhouse.dto.log.readings;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.sql.Timestamp;
import org.greenhouse.dto.greenhouse.SeedBedDto;
import org.greenhouse.dto.log.ReceiveLogDto;
import org.greenhouse.entity.log.readings.Humidity;

public record HumidityDto(
    @JsonProperty("id") Integer id,
    @JsonProperty("value") Integer value,
    @JsonProperty("receiveTime") Timestamp receiveTime,
    @JsonProperty("receiveLogs") ReceiveLogDto receiveLogs,
    @JsonProperty("seedBed") SeedBedDto seedBed
) {
  public static HumidityDto fromHumidity(Humidity humidity) {
    return new HumidityDto(
        humidity.getId(),
        humidity.getValue(),
        humidity.getReceiveTime(),
        ReceiveLogDto.fromReceiveLogs(humidity.getReceiveLogs()),
        SeedBedDto.fromSeedBeds(humidity.getSeedBed())
    );
  }
}
