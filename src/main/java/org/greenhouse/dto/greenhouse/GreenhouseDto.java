package org.greenhouse.dto.greenhouse;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import org.greenhouse.dto.log.readings.LightDto;
import org.greenhouse.dto.log.readings.TemperatureDto;
import org.greenhouse.dto.sensor.SensorsDto;
import org.greenhouse.dto.user.UserDto;
import org.greenhouse.entity.greenhouse.Greenhouses;

public record GreenhouseDto(
    @JsonProperty("id") Long id,
    @JsonProperty("location") String location,
    @JsonProperty("greenhouseName") String greenhouseName,
    @JsonProperty("user") UserDto user,
    @JsonProperty("light") List<LightDto> light,
    @JsonProperty("temperature") List<TemperatureDto> temperature,
    @JsonProperty("seedBeds") List<SeedBedDto> seedBeds,
    @JsonProperty("control") List<ControlDto> control,
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
        greenhouse.getControl().stream().map(ControlDto::fromControl).toList(),
        greenhouse.getSensors().stream().map(SensorsDto::fromSensors).toList());
  }
}
