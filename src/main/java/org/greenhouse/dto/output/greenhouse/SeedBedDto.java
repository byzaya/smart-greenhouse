package org.greenhouse.dto.output.greenhouse;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import org.greenhouse.dto.output.log.readings.HumidityDto;
import org.greenhouse.entity.greenhouse.SeedBeds;

public record SeedBedDto(
    @JsonProperty("id") Long id,
    @JsonProperty("seedbedName") String seedbedName,
    @JsonProperty("isAuto") Boolean isAuto,
    @JsonProperty("wateringEnabled") Boolean wateringEnabled,
    @JsonProperty("wateringDuration") Integer wateringDuration,
    @JsonProperty("wateringFrequency") Integer wateringFrequency,
    @JsonProperty("minHumidity") Integer minHumidity,
    @JsonProperty("maxHumidity") Integer maxHumidity,
    @JsonProperty("greenhouse") GreenhouseDto greenhouse,
    @JsonProperty("humidity") List<HumidityDto> humidity) {
  public static SeedBedDto fromSeedBeds(SeedBeds seedBeds) {
    return new SeedBedDto(
        seedBeds.getId(),
        seedBeds.getSeedbedName(),
        seedBeds.getIsAuto(),
        seedBeds.getWateringEnabled(),
        seedBeds.getWateringDuration(),
        seedBeds.getWateringFrequency(),
        seedBeds.getMinHumidity(),
        seedBeds.getMaxHumidity(),
        GreenhouseDto.fromGreenhouse(seedBeds.getGreenhouse()),
        seedBeds.getHumidity().stream().map(HumidityDto::fromHumidity).toList());
  }
}
