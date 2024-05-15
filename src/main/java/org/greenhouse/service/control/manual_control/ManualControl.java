package org.greenhouse.service.control.manual_control;

import java.sql.Timestamp;
import lombok.RequiredArgsConstructor;
import org.greenhouse.dto.input.log.SendLogInputDto;
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
import org.greenhouse.service.control.LogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ManualControl {

  private final ValidationService validationService;
  private final LogService logService;

  // TODO как выяснить id топика
  private void makeSendLog(
      Boolean reply, Integer command, Timestamp sendTime, Long topicId) {
    SendLogInputDto sendLogInputDto = new SendLogInputDto(reply, command, sendTime, topicId);
    logService.sendLog(sendLogInputDto);
  }

  // тут нужно записать в mqtt команду, записать в логах бд и записать в control бд

  // включить вентилятор в теплице
  // command 0
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
    makeSendLog(true, 0, new Timestamp(System.currentTimeMillis()), 1L);
    greenhouses.getControl().setFanEnabled(true);
  }

  // выключить вентилятор в теплице
  // command 1
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
    makeSendLog(true, 1, new Timestamp(System.currentTimeMillis()), 1L);
    greenhouses.getControl().setFanEnabled(false);
  }

  // включить тепловентилятор в теплице
  // command 2
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
    makeSendLog(true, 2, new Timestamp(System.currentTimeMillis()), 1L);
    greenhouses.getControl().setHeaterEnabled(true);
  }

  // выключить тепловентилятор в теплице
  // command 3
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
    makeSendLog(true, 3, new Timestamp(System.currentTimeMillis()), 1L);
    greenhouses.getControl().setHeaterEnabled(false);
  }

  // открыть окно на половину в теплице
  // command 4
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
    makeSendLog(true, 4, new Timestamp(System.currentTimeMillis()), 1L);
    greenhouses.getControl().setWindowStatus(1);
  }

  // открыть окно полностью в теплице
  // command 5
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
    makeSendLog(true, 5, new Timestamp(System.currentTimeMillis()), 1L);
    greenhouses.getControl().setWindowStatus(2);
  }

  // закрыть окно в теплице
  // command 6
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
    makeSendLog(true, 6, new Timestamp(System.currentTimeMillis()), 1L);
    greenhouses.getControl().setWindowStatus(0);
  }

  // включить свет
  // command 7
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
    makeSendLog(true, 7, new Timestamp(System.currentTimeMillis()), 1L);
    greenhouses.getControl().setLightEnabled(true);
  }

  // выключить свет
  // command 8
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
    makeSendLog(true, 8, new Timestamp(System.currentTimeMillis()), 1L);
    greenhouses.getControl().setLightEnabled(false);
  }

  // включить полив на грядке
  // command 9
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
    makeSendLog(true, 9, new Timestamp(System.currentTimeMillis()), 1L);
    seedBed.setWateringEnabled(true);
  }

  // выключить полив на грядке
  // command 10
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
    makeSendLog(true, 10, new Timestamp(System.currentTimeMillis()), 1L);
    seedBed.setWateringEnabled(false);
  }
}
