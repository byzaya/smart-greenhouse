package org.greenhouse.service.control.manual_control;

import lombok.RequiredArgsConstructor;
import org.greenhouse.entity.greenhouse.Configurations;
import org.greenhouse.entity.greenhouse.Control;
import org.greenhouse.entity.greenhouse.Greenhouses;
import org.greenhouse.repository.greenhouse.ControlRepository;
import org.greenhouse.repository.greenhouse.GreenhousesRepository;
import org.greenhouse.service.ValidationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ManualControl {

  private final GreenhousesRepository greenhousesRepository;
  private final ControlRepository controlRepository;
  private final ValidationService validationService;

  // тут нужно записать в mqtt команду, записать в логах бд и записать в control бд
  // проверить что не включен автоконтроль

  // включить вентилятор в теплице
  @Transactional
  public void fanOn(Long greenhouseId) {
    Greenhouses greenhouses = validationService.getGreenhouseOrThrow(greenhouseId);
    Configurations conf = greenhouses.getConfiguration();
    validationService.isNotAutoGreenhouseOrThrow(conf.getId());
    Control control = greenhouses.getControl();
//    if (control.getFanEnabled()) {
//      throw new
//    }
    // TODO записать в MQTT

    greenhouses.getControl().setFanEnabled(true);
  }

  // выключить вентилятор в теплице
  @Transactional
  public void fanOff(Long greenhouseId) {
    Greenhouses greenhouses = validationService.getGreenhouseOrThrow(greenhouseId);
  }

  // включить тепловентилятор в теплице
  @Transactional
  public void heaterOn(Long greenhouseId) {
    Greenhouses greenhouses = validationService.getGreenhouseOrThrow(greenhouseId);
  }

  // выключить тепловентилятор в теплице
  @Transactional
  public void heaterOff(Long greenhouseId) {
    Greenhouses greenhouses = validationService.getGreenhouseOrThrow(greenhouseId);
  }

  // открыть окно на половину в теплице
  @Transactional
  public void windowHalfOpen(Long greenhouseId) {
    Greenhouses greenhouses = validationService.getGreenhouseOrThrow(greenhouseId);
  }

  // открыть окно полностью в теплице
  @Transactional
  public void windowAllOpen(Long greenhouseId) {
    Greenhouses greenhouses = validationService.getGreenhouseOrThrow(greenhouseId);
  }

  // закрыть окно в теплице
  @Transactional
  public void windowClose(Long greenhouseId) {
    Greenhouses greenhouses = validationService.getGreenhouseOrThrow(greenhouseId);
  }

  // включить свет
  @Transactional
  public void lightOn(Long greenhouseId) {
    Greenhouses greenhouses = validationService.getGreenhouseOrThrow(greenhouseId);
  }

  // выключить свет
  @Transactional
  public void lightOff(Long greenhouseId) {
    Greenhouses greenhouses = validationService.getGreenhouseOrThrow(greenhouseId);
  }

  // включить полив на грядке
  @Transactional
  public void wateringOn(Long seedBedId) {

  }

  // выключить полив на грядке
  @Transactional
  public void wateringOff(Long seedBedId) {

  }

}
