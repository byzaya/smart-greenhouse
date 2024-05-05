package org.greenhouse.dto.greenhouse;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.greenhouse.dto.user.UserDto;
import org.greenhouse.entity.greenhouse.Configurations;

public record ConfigurationDto(
    @JsonProperty("id") Long id,
    @JsonProperty("isActive") Boolean isActive,
    @JsonProperty("isAuto") Boolean isAuto,
    @JsonProperty("minTemperature") Integer minTemperature,
    @JsonProperty("maxTemperature") Integer maxTemperature,
    @JsonProperty("minLight") Integer minLight,
    @JsonProperty("maxLight") Integer maxLight,
    @JsonProperty("user") UserDto user) {
  public static ConfigurationDto fromConfiguration(Configurations configuration) {
    return new ConfigurationDto(
        configuration.getId(),
        configuration.getIsActive(),
        configuration.getIsAuto(),
        configuration.getMinTemperature(),
        configuration.getMaxTemperature(),
        configuration.getMinLight(),
        configuration.getMaxLight(),
        UserDto.fromUser(configuration.getUser()));
  }
}
