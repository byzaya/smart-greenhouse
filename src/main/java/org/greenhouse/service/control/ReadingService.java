package org.greenhouse.service.control;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.greenhouse.dto.output.log.readings.HumidityDto;
import org.greenhouse.dto.output.log.readings.LightDto;
import org.greenhouse.dto.output.log.readings.TemperatureDto;
import org.greenhouse.entity.log.readings.Humidity;
import org.greenhouse.entity.log.readings.Light;
import org.greenhouse.entity.log.readings.Temperature;
import org.greenhouse.repository.log.readings.HumidityRepository;
import org.greenhouse.repository.log.readings.LightRepository;
import org.greenhouse.repository.log.readings.TemperatureRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReadingService {

  private final HumidityRepository humidityRepository;
  private final LightRepository lightRepository;
  private final TemperatureRepository temperatureRepository;

  @Transactional(readOnly = true)
  public List<HumidityDto> getLatestHumidityForSeedBed(Long seedBedId) {
    List<Humidity> humidities =
        humidityRepository.findTop10BySeedBedIdOrderByReceiveTimeDesc(seedBedId);
    return humidities.stream().map(HumidityDto::fromHumidity).collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<LightDto> getLatestLightForGreenhouse(Long greenhouseId) {
    List<Light> lights =
        lightRepository.findTop10ByGreenhouseIdOrderByReceiveTimeDesc(greenhouseId);
    return lights.stream().map(LightDto::fromLight).collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<TemperatureDto> getLatestTemperatureForGreenhouse(Long greenhouseId) {
    List<Temperature> temperatures =
        temperatureRepository.findTop10ByGreenhouseIdOrderByReceiveTimeDesc(greenhouseId);
    return temperatures.stream().map(TemperatureDto::fromTemperature).collect(Collectors.toList());
  }
}
