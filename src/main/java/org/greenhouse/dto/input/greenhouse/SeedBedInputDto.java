package org.greenhouse.dto.input.greenhouse;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.greenhouse.entity.greenhouse.SeedBeds;

public record SeedBedInputDto(
    @JsonProperty("seedbedName") String seedbedName,
    @JsonProperty("isAuto") Boolean isAuto,
    @JsonProperty("wateringEnabled") Boolean wateringEnabled,
    @JsonProperty("wateringDuration") Integer wateringDuration,
    @JsonProperty("wateringFrequency") Integer wateringFrequency,
    @JsonProperty("minHumidity") Integer minHumidity,
    @JsonProperty("maxHumidity") Integer maxHumidity,
    @JsonProperty("greenhouseId") Long greenhouseId) {
  public static SeedBedInputDto fromSeedBeds(SeedBeds seedBeds) {
    return new SeedBedInputDto(
        seedBeds.getSeedbedName(),
        seedBeds.getIsAuto(),
        seedBeds.getWateringEnabled(),
        seedBeds.getWateringDuration(),
        seedBeds.getWateringFrequency(),
        seedBeds.getMinHumidity(),
        seedBeds.getMaxHumidity(),
        seedBeds.getGreenhouse().getId());
  }
}
