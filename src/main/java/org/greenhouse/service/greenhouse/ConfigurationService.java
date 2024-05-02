package org.greenhouse.service.greenhouse;

import lombok.RequiredArgsConstructor;
import org.greenhouse.dto.greenhouse.ConfigurationDto;
import org.greenhouse.entity.greenhouse.Configurations;
import org.greenhouse.exception.message.ConfigurationNotFoundException;
import org.greenhouse.repository.greenhouse.ConfigurationsRepository;
import org.greenhouse.repository.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConfigurationService {

  private final ConfigurationsRepository configurationRepository;
  private final UserRepository userRepository;

  // создание конфигурации теплицы
  @Transactional
  public ConfigurationDto createConfiguration(ConfigurationDto configurationDto) {
    Configurations configuration = new Configurations();
    updateConfigurationFromDto(configuration, configurationDto);
    return ConfigurationDto.fromConfiguration(configurationRepository.save(configuration));
  }

  // изменение конфигурации теплицы
  @Transactional
  public ConfigurationDto updateConfiguration(Long id, ConfigurationDto updatedConfigurationDto) {
    Configurations configuration =
        configurationRepository
            .findById(id)
            .orElseThrow(
                () -> new ConfigurationNotFoundException("Configuration not found with ID: " + id));
    updateConfigurationFromDto(configuration, updatedConfigurationDto);
    return ConfigurationDto.fromConfiguration(configurationRepository.save(configuration));
  }

  private void updateConfigurationFromDto(
      Configurations configuration, ConfigurationDto configurationDto) {
    configuration.setIsActive(configurationDto.isActive());
    configuration.setIsAuto(configurationDto.isAuto());
    configuration.setMinTemperature(configurationDto.minTemperature());
    configuration.setMaxTemperature(configurationDto.maxTemperature());
    configuration.setMinLight(configurationDto.minLight());
    configuration.setMaxLight(configurationDto.maxLight());
    configuration.setUser(userRepository.findById(configurationDto.user().id()).orElseThrow());
  }

  // переключение isAuto конфигурации
  // TODO добавить проверку, чтобы у одной теплицы была только одна вкл конфигурация
  @Transactional
  public ConfigurationDto changeAutoMode(Long id, Boolean isAuto) {
    Configurations configuration =
        configurationRepository
            .findById(id)
            .orElseThrow(
                () -> new ConfigurationNotFoundException("Configuration not found with ID: " + id));
    configuration.setIsAuto(isAuto);
    return ConfigurationDto.fromConfiguration(configurationRepository.save(configuration));
  }

  // получение конфигурации по id
  @Transactional(readOnly = true)
  public ConfigurationDto getConfigurationById(Long id) {
    Configurations configuration =
        configurationRepository
            .findById(id)
            .orElseThrow(
                () -> new ConfigurationNotFoundException("Configuration not found with ID: " + id));
    return ConfigurationDto.fromConfiguration(configuration);
  }
}
