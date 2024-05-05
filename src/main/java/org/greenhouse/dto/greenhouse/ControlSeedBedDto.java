package org.greenhouse.dto.greenhouse;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.greenhouse.entity.greenhouse.ControlSeedBed;

public record ControlSeedBedDto(
    @JsonProperty("id") Long id,
    @JsonProperty("wateringEnabled") Boolean wateringEnabled,
    @JsonProperty("seedBed") SeedBedDto seedBed
) {
  public static ControlSeedBedDto fromControlSeedBed(ControlSeedBed controlSeedBed) {
    return new ControlSeedBedDto(
        controlSeedBed.getId(),
        controlSeedBed.getWateringEnabled(),
        SeedBedDto.fromSeedBeds(controlSeedBed.getSeedBed())
    );
  }
}
