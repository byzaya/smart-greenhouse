package org.greenhouse.service.control.manual_control;

import lombok.RequiredArgsConstructor;
import org.greenhouse.entity.greenhouse.Configurations;
import org.greenhouse.entity.greenhouse.Control;
import org.greenhouse.entity.greenhouse.Greenhouses;
import org.greenhouse.entity.greenhouse.SeedBeds;
import org.greenhouse.exception.message.IsAutoTrueException;
import org.greenhouse.exception.message.control_message.FanAlreadyOffException;
import org.greenhouse.exception.message.control_message.FanAlreadyOnException;
import org.greenhouse.exception.message.control_message.HeaterAlreadyOffException;
import org.greenhouse.exception.message.control_message.HeaterAlreadyOnException;
import org.greenhouse.exception.message.control_message.WateringAlreadyOnException;
import org.greenhouse.exception.message.control_message.WindowAlreadyHalfOpenException;
import org.greenhouse.service.ValidationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ManualControl {

  private final ValidationService validationService;

  // тут нужно записать в mqtt команду, записать в логах бд и записать в control бд

  // включить вентилятор в теплице
  @Transactional
  public void fanOn(Long greenhouseId) {
    Greenhouses greenhouses = validationService.getGreenhouseOrThrow(greenhouseId);
    Configurations conf = greenhouses.getConfiguration();
    validationService.isNotAutoGreenhouseOrThrow(conf.getId());
    Control control = greenhouses.getControl();
    if (control.getFanEnabled()) {
      throw new FanAlreadyOnException("Fan is already on, maybe you need to off?");
    }
    // TODO записать в MQTT
    // TODO сделать запись о вкл фена в бд
    greenhouses.getControl().setFanEnabled(true);
  }

  // выключить вентилятор в теплице
  @Transactional
  public void fanOff(Long greenhouseId) {
    Greenhouses greenhouses = validationService.getGreenhouseOrThrow(greenhouseId);
    Configurations conf = greenhouses.getConfiguration();
    validationService.isNotAutoGreenhouseOrThrow(conf.getId());
    Control control = greenhouses.getControl();
    if (!control.getFanEnabled()) {
      throw new FanAlreadyOffException("Fan is already off, maybe you need to on?");
    }
    // TODO записать в MQTT
    // TODO сделать запись о выкл фена в бд
    greenhouses.getControl().setFanEnabled(false);
  }

  // включить тепловентилятор в теплице
  @Transactional
  public void heaterOn(Long greenhouseId) {
    Greenhouses greenhouses = validationService.getGreenhouseOrThrow(greenhouseId);
    Configurations conf = greenhouses.getConfiguration();
    validationService.isNotAutoGreenhouseOrThrow(conf.getId());
    Control control = greenhouses.getControl();
    if (control.getHeaterEnabled()) {
      throw new HeaterAlreadyOnException("Heater is already on, maybe you need to off?");
    }
    // TODO записать в MQTT
    // TODO сделать запись о вкл тепловентилятора в бд
    greenhouses.getControl().setHeaterEnabled(true);
  }

  // выключить тепловентилятор в теплице
  @Transactional
  public void heaterOff(Long greenhouseId) {
    Greenhouses greenhouses = validationService.getGreenhouseOrThrow(greenhouseId);
    Configurations conf = greenhouses.getConfiguration();
    validationService.isNotAutoGreenhouseOrThrow(conf.getId());
    Control control = greenhouses.getControl();
    if (!control.getHeaterEnabled()) {
      throw new HeaterAlreadyOffException("Heater is already off, maybe you need to on?");
    }
    // TODO записать в MQTT
    // TODO сделать запись о выкл тепловентилятора в бд
    greenhouses.getControl().setHeaterEnabled(false);
  }

  // открыть окно на половину в теплице
  @Transactional
  public void windowHalfOpen(Long greenhouseId) {
    Greenhouses greenhouses = validationService.getGreenhouseOrThrow(greenhouseId);
    Configurations conf = greenhouses.getConfiguration();
    validationService.isNotAutoGreenhouseOrThrow(conf.getId());
    Control control = greenhouses.getControl();
    if (control.getWindowStatus() == 1) {
      throw new WindowAlreadyHalfOpenException(
          "Window is already half open, maybe you need to full open or close it?");
    }
    // TODO записать в MQTT
    // TODO сделать запись об открытии окна наполовину в бд
    greenhouses.getControl().setWindowStatus(1);
  }

  // открыть окно полностью в теплице
  @Transactional
  public void windowFullOpen(Long greenhouseId) {
    Greenhouses greenhouses = validationService.getGreenhouseOrThrow(greenhouseId);
    Configurations conf = greenhouses.getConfiguration();
    validationService.isNotAutoGreenhouseOrThrow(conf.getId());
    Control control = greenhouses.getControl();
    if (control.getWindowStatus() == 2) {
      throw new WindowAlreadyHalfOpenException(
          "Window is already full open, maybe you need to half open or close it?");
    }
    // TODO записать в MQTT
    // TODO сделать запись об открытии окна полностью в бд
    greenhouses.getControl().setWindowStatus(2);
  }

  // закрыть окно в теплице
  @Transactional
  public void windowClose(Long greenhouseId) {
    Greenhouses greenhouses = validationService.getGreenhouseOrThrow(greenhouseId);
    Configurations conf = greenhouses.getConfiguration();
    validationService.isNotAutoGreenhouseOrThrow(conf.getId());
    Control control = greenhouses.getControl();
    if (control.getWindowStatus() == 0) {
      throw new WindowAlreadyHalfOpenException(
          "Window is already close, maybe you need to half open or full open it?");
    }
    // TODO записать в MQTT
    // TODO сделать запись об закрытии окна полностью в бд
    greenhouses.getControl().setWindowStatus(0);
  }

  // включить свет
  @Transactional
  public void lightOn(Long greenhouseId) {
    Greenhouses greenhouses = validationService.getGreenhouseOrThrow(greenhouseId);
    Configurations conf = greenhouses.getConfiguration();
    validationService.isNotAutoGreenhouseOrThrow(conf.getId());
    Control control = greenhouses.getControl();
    if (control.getLightEnabled()) {
      throw new HeaterAlreadyOnException("Light is already on, maybe you need to off?");
    }
    // TODO записать в MQTT
    // TODO сделать запись о вкл света в бд
    greenhouses.getControl().setLightEnabled(true);
  }

  // выключить свет
  @Transactional
  public void lightOff(Long greenhouseId) {
    Greenhouses greenhouses = validationService.getGreenhouseOrThrow(greenhouseId);
    Configurations conf = greenhouses.getConfiguration();
    validationService.isNotAutoGreenhouseOrThrow(conf.getId());
    Control control = greenhouses.getControl();
    if (!control.getLightEnabled()) {
      throw new HeaterAlreadyOffException("Light is already off, maybe you need to on?");
    }
    // TODO записать в MQTT
    // TODO сделать запись о вкл света в бд
    greenhouses.getControl().setLightEnabled(false);
  }

  // включить полив на грядке
  @Transactional
  public void wateringOn(Long seedBedId) {
    SeedBeds seedBed = validationService.getSeedBedOrThrow(seedBedId);
    if (seedBed.getIsAuto()) {
      throw new IsAutoTrueException(
          "Seed bed configuration is auto, turn it off before on/off watering");
    }
    if (seedBed.getWateringEnabled()) {
      throw new WateringAlreadyOnException("Watering is already on, maybe you need to off?");
    }
    // TODO записать в MQTT
    // TODO сделать запись о вкл полив в бд
    seedBed.setWateringEnabled(true);
  }

  // выключить полив на грядке
  @Transactional
  public void wateringOff(Long seedBedId) {
    SeedBeds seedBed = validationService.getSeedBedOrThrow(seedBedId);
    if (seedBed.getIsAuto()) {
      throw new IsAutoTrueException(
          "Seed bed configuration is auto, turn it off before on/off watering");
    }
    if (!seedBed.getWateringEnabled()) {
      throw new WateringAlreadyOnException("Watering is already off, maybe you need to on?");
    }
    // TODO записать в MQTT
    // TODO сделать запись о выкл полив в бд
    seedBed.setWateringEnabled(false);
  }
}
