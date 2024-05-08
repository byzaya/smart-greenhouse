package org.greenhouse.dto.output.greenhouse;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import org.greenhouse.dto.output.log.readings.LightDto;
import org.greenhouse.dto.output.log.readings.TemperatureDto;
import org.greenhouse.dto.output.sensor.SensorsDto;
import org.greenhouse.dto.output.user.UserDto;
import org.greenhouse.entity.greenhouse.Greenhouses;

public record GreenhouseDto(
    @JsonProperty("id") Long id,
    @JsonProperty("location") String location,
    @JsonProperty("greenhouseName") String greenhouseName,
    @JsonProperty("user") UserDto user,
    @JsonProperty("light") List<LightDto> light,
    @JsonProperty("temperature") List<TemperatureDto> temperature,
    @JsonProperty("seedBeds") List<SeedBedDto> seedBeds,
    @JsonProperty("sensors") List<SensorsDto> sensors) {
  public static GreenhouseDto fromGreenhouse(Greenhouses greenhouse) {
    return new GreenhouseDto(
        greenhouse.getId(),
        greenhouse.getLocation(),
        greenhouse.getGreenhouseName(),
        UserDto.fromUser(greenhouse.getUser()),
        greenhouse.getLight().stream().map(LightDto::fromLight).toList(),
        greenhouse.getTemperature().stream().map(TemperatureDto::fromTemperature).toList(),
        greenhouse.getSeedBeds().stream().map(SeedBedDto::fromSeedBeds).toList(),
        greenhouse.getSensors().stream().map(SensorsDto::fromSensors).toList());
  }
}
