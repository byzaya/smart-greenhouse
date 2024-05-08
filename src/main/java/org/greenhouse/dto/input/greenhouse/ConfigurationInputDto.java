package org.greenhouse.dto.input.greenhouse;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.greenhouse.entity.greenhouse.Configurations;

public record ConfigurationInputDto(
    @JsonProperty("isActive") Boolean isActive,
    @JsonProperty("isAuto") Boolean isAuto,
    @JsonProperty("minTemperature") Integer minTemperature,
    @JsonProperty("maxTemperature") Integer maxTemperature,
    @JsonProperty("minLight") Integer minLight,
    @JsonProperty("maxLight") Integer maxLight,
    @JsonProperty("greenhouseId") Long greenhouseId) {
  public static ConfigurationInputDto fromConfiguration(Configurations configuration) {
    return new ConfigurationInputDto(
        configuration.getIsActive(),
        configuration.getIsAuto(),
        configuration.getMinTemperature(),
        configuration.getMaxTemperature(),
        configuration.getMinLight(),
        configuration.getMaxLight(),
        configuration.getGreenhouse().getId());
  }
}
