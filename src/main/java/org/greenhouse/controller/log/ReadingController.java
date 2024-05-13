package org.greenhouse.controller.log;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.greenhouse.dto.output.log.readings.HumidityDto;
import org.greenhouse.dto.output.log.readings.LightDto;
import org.greenhouse.dto.output.log.readings.TemperatureDto;
import org.greenhouse.service.control.ReadingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/log")
@RequiredArgsConstructor
@Tag(name = "Данные с датчиков")
public class ReadingController {

  private final ReadingService readingService;

  @GetMapping("/humidity/{seedBedId}")
  public List<HumidityDto> getLatestHumidityForSeedBed(@PathVariable Long seedBedId) {
    return readingService.getLatestHumidityForSeedBed(seedBedId);
  }

  @GetMapping("/light/{greenhouseId}")
  public List<LightDto> getLatestLightForGreenhouse(@PathVariable Long greenhouseId) {
    return readingService.getLatestLightForGreenhouse(greenhouseId);
  }

  @GetMapping("/temperature/{greenhouseId}")
  public List<TemperatureDto> getLatestTemperatureForGreenhouse(@PathVariable Long greenhouseId) {
    return readingService.getLatestTemperatureForGreenhouse(greenhouseId);
  }
}
