package org.greenhouse.service;

import lombok.RequiredArgsConstructor;
import org.greenhouse.entity.greenhouse.Configurations;
import org.greenhouse.entity.greenhouse.Control;
import org.greenhouse.entity.greenhouse.Greenhouses;
import org.greenhouse.entity.greenhouse.SeedBeds;
import org.greenhouse.entity.log.Topics;
import org.greenhouse.entity.sensor.SensorType;
import org.greenhouse.entity.sensor.Sensors;
import org.greenhouse.entity.user.User;
import org.greenhouse.exception.message.not_found_message.ConfigurationNotFoundException;
import org.greenhouse.exception.message.IsAutoFalseException;
import org.greenhouse.exception.message.IsAutoTrueException;
import org.greenhouse.exception.message.not_found_message.ControlNotFoundException;
import org.greenhouse.exception.message.not_found_message.GreenhouseNotFoundException;
import org.greenhouse.exception.message.not_found_message.SeedBedNotFoundException;
import org.greenhouse.exception.message.not_found_message.SensorNotFoundException;
import org.greenhouse.exception.message.not_found_message.SensorTypeNotFoundException;
import org.greenhouse.exception.message.not_found_message.TopicNotFoundException;
import org.greenhouse.exception.message.not_found_message.UserNotFoundException;
import org.greenhouse.repository.greenhouse.ConfigurationsRepository;
import org.greenhouse.repository.greenhouse.ControlRepository;
import org.greenhouse.repository.greenhouse.GreenhousesRepository;
import org.greenhouse.repository.greenhouse.SeedBedsRepository;
import org.greenhouse.repository.log.TopicsRepository;
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
  private final ControlRepository controlRepository;
  private final TopicsRepository topicsRepository;

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
            () -> new GreenhouseNotFoundException("Greenhouse not found with ID: " + greenhouseId));
  }

  @Transactional(readOnly = true)
  public SeedBeds getSeedBedOrThrow(Long seedBedId) {
    return seedBedsRepository
        .findById(seedBedId)
        .orElseThrow(
            () -> new SeedBedNotFoundException("Seed bed not found with ID: " + seedBedId));
  }

  @Transactional(readOnly = true)
  public Sensors getSensorOrThrow(Long sensorId) {
    return sensorsRepository
        .findById(sensorId)
        .orElseThrow(() -> new SensorNotFoundException("Sensor not found with ID: " + sensorId));
  }

  @Transactional(readOnly = true)
  public SensorType getSensorTypeOrThrow(Long sensorTypeId) {
    return sensorsTypeRepository
        .findById(sensorTypeId)
        .orElseThrow(
            () ->
                new SensorTypeNotFoundException("Sensor type not found with ID: " + sensorTypeId));
  }

  @Transactional(readOnly = true)
  public Control getControlOrThrow(Long controlId) {
    return controlRepository
        .findById(controlId)
        .orElseThrow(() -> new ControlNotFoundException("Control not found with ID: " + controlId));
  }

  @Transactional(readOnly = true)
  public Topics getTopicOrThrow(Long topicId) {
    return topicsRepository
        .findById(topicId)
        .orElseThrow(() -> new TopicNotFoundException("Topic not found with ID: " + topicId));
  }

  @Transactional(readOnly = true)
  public Boolean checkIsAutoGreenhouse(Long configurationId) {
    return getConfigurationOrThrow(configurationId).getIsAuto();
  }

  @Transactional(readOnly = true)
  public void isAutoGreenhouseOrThrow(Long configurationId) {
    if (!checkIsAutoGreenhouse(configurationId)) {
      throw new IsAutoFalseException("Auto configuration is disabled, turn it on to use auto control");
    }
  }

  @Transactional(readOnly = true)
  public void isNotAutoGreenhouseOrThrow(Long configurationId) {
    if (checkIsAutoGreenhouse(configurationId)) {
      throw new IsAutoTrueException("Auto configuration is enabled, turn it off to use manual control");
    }
  }
}
