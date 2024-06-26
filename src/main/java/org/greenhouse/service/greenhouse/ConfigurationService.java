package org.greenhouse.service.greenhouse;

import lombok.RequiredArgsConstructor;
import org.greenhouse.dto.input.greenhouse.ConfigurationInputDto;
import org.greenhouse.dto.output.greenhouse.ConfigurationDto;
import org.greenhouse.entity.greenhouse.Configurations;
import org.greenhouse.entity.greenhouse.Greenhouses;
import org.greenhouse.repository.greenhouse.ConfigurationsRepository;
import org.greenhouse.service.ValidationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConfigurationService {

  private final ConfigurationsRepository configurationRepository;
  private final ValidationService validationService;

  // создание конфигурации теплицы
  @Transactional
  public ConfigurationDto createConfiguration(ConfigurationInputDto configurationDto) {
    Configurations configuration = new Configurations();
    updateConfigurationFromDto(configuration, configurationDto);
    return ConfigurationDto.fromConfiguration(configurationRepository.save(configuration));
  }

  // изменение конфигурации теплицы
  @Transactional
  public ConfigurationDto updateConfiguration(Long id, ConfigurationInputDto updatedConfigurationDto) {
    Configurations configuration = validationService.getConfigurationOrThrow(id);
    updateConfigurationFromDto(configuration, updatedConfigurationDto);
    return ConfigurationDto.fromConfiguration(configurationRepository.save(configuration));
  }

  private void updateConfigurationFromDto(
      Configurations configuration, ConfigurationInputDto configurationDto) {
    configuration.setIsActive(configurationDto.isActive());
    configuration.setIsAuto(configurationDto.isAuto());
    configuration.setMinTemperature(configurationDto.minTemperature());
    configuration.setMaxTemperature(configurationDto.maxTemperature());
    configuration.setMinLight(configurationDto.minLight());
    configuration.setMaxLight(configurationDto.maxLight());
    configuration.setGreenhouse(
        validationService.getGreenhouseOrThrow(configurationDto.greenhouseId()));
  }

  // переключение isAuto конфигурации
  @Transactional
  public ConfigurationDto changeAutoMode(Long id, Boolean isAuto) {
    Configurations configuration = validationService.getConfigurationOrThrow(id);
    configuration.setIsAuto(isAuto);
    return ConfigurationDto.fromConfiguration(configurationRepository.save(configuration));
  }

  // получение конфигурации по id
  @Transactional(readOnly = true)
  public ConfigurationDto getConfigurationById(Long id) {
    Configurations configuration = validationService.getConfigurationOrThrow(id);
    return ConfigurationDto.fromConfiguration(configuration);
  }

  // получение конфигурации по id теплицы
  @Transactional(readOnly = true)
  public ConfigurationDto getConfigurationByGreenhouseId(Long id) {
    Greenhouses greenhouses = validationService.getGreenhouseOrThrow(id);
    return ConfigurationDto.fromConfiguration(greenhouses.getConfiguration());
  }
}
