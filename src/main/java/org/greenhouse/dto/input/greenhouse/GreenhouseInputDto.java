package org.greenhouse.dto.input.greenhouse;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.greenhouse.entity.greenhouse.Greenhouses;

public record GreenhouseInputDto(
    @JsonProperty("location") String location,
    @JsonProperty("greenhouseName") String greenhouseName,
    @JsonProperty("userId") Integer userId) {
  public static GreenhouseInputDto fromGreenhouse(Greenhouses greenhouse) {
    return new GreenhouseInputDto(
        greenhouse.getLocation(), greenhouse.getGreenhouseName(), greenhouse.getUser().getId());
  }
}
