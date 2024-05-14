package org.greenhouse.dto.output.greenhouse;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.greenhouse.dto.output.user.UserDto;
import org.greenhouse.entity.greenhouse.Greenhouses;

public record GreenhouseDto(
    @JsonProperty("id") Long id,
    @JsonProperty("location") String location,
    @JsonProperty("greenhouseName") String greenhouseName,
    @JsonProperty("user") UserDto user) {
  public static GreenhouseDto fromGreenhouse(Greenhouses greenhouse) {
    return new GreenhouseDto(
        greenhouse.getId(),
        greenhouse.getLocation(),
        greenhouse.getGreenhouseName(),
        UserDto.fromUser(greenhouse.getUser()));
  }
}
