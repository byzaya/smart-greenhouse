package org.greenhouse.service;

import lombok.RequiredArgsConstructor;
import org.greenhouse.entity.greenhouse.Configurations;
import org.greenhouse.entity.greenhouse.Greenhouses;
import org.greenhouse.entity.greenhouse.SeedBeds;
import org.greenhouse.entity.sensor.SensorType;
import org.greenhouse.entity.sensor.Sensors;
import org.greenhouse.entity.user.User;
import org.greenhouse.exception.message.ConfigurationNotFoundException;
import org.greenhouse.exception.message.UserNotFoundException;
import org.greenhouse.repository.greenhouse.ConfigurationsRepository;
import org.greenhouse.repository.greenhouse.GreenhousesRepository;
import org.greenhouse.repository.greenhouse.SeedBedsRepository;
import org.greenhouse.repository.sensor.SensorsRepository;
import org.greenhouse.repository.sensor.SensorsTypeRepository;
import org.greenhouse.repository.user.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ValidationService {

  private final UserRepository userRepository;
  private final ConfigurationsRepository configurationsRepository;
  private final GreenhousesRepository greenhousesRepository;
  private final SeedBedsRepository seedBedsRepository;
  private final SensorsRepository sensorsRepository;
  private final SensorsTypeRepository sensorsTypeRepository;

  @Transactional(readOnly = true)
  public User getUserOrThrow(Integer userId) {
    return userRepository
        .findById(userId)
        .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
  }

  @Transactional(readOnly = true)
  public Configurations getConfigurationOrThrow(Long configurationId) {
    return configurationsRepository
        .findById(configurationId)
        .orElseThrow(
            () ->
                new ConfigurationNotFoundException(
                    "Configuration not found with ID: " + configurationId));
  }

  @Transactional(readOnly = true)
  public Greenhouses getGreenhouseOrThrow(Long greenhouseId) {
    return greenhousesRepository
        .findById(greenhouseId)
        .orElseThrow(
            () ->
                new ConfigurationNotFoundException(
                    "Greenhouse not found with ID: " + greenhouseId));
  }

  @Transactional(readOnly = true)
  public SeedBeds getSeedBedOrThrow(Long seedBedId) {
    return seedBedsRepository
        .findById(seedBedId)
        .orElseThrow(
            () ->
                new ConfigurationNotFoundException(
                    "Seed bed not found with ID: " + seedBedId));
  }

  @Transactional(readOnly = true)
  public Sensors getSensorThrow(Long sensorId) {
    return sensorsRepository
        .findById(sensorId)
        .orElseThrow(
            () ->
                new ConfigurationNotFoundException(
                    "Sensor not found with ID: " + sensorId));
  }

  @Transactional(readOnly = true)
  public SensorType getSensorTypeThrow(Long sensorTypeId) {
    return sensorsTypeRepository
        .findById(sensorTypeId)
        .orElseThrow(
            () ->
                new ConfigurationNotFoundException(
                    "Sensor type not found with ID: " + sensorTypeId));
  }
}
