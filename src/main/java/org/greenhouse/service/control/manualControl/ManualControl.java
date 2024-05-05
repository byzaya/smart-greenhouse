package org.greenhouse.service.control.manualControl;

import lombok.RequiredArgsConstructor;
import org.greenhouse.repository.greenhouse.GreenhousesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ManualControl {

  private final GreenhousesRepository greenhousesRepository;

  // тут нужно записать в mqtt команду, записать в логах бд и записать в control бд
  // проверить что не включен автоконтроль

  // включить вентилятор в теплице
  @Transactional
  public void fanOn(Long greenhouseId) {

  }

  // выключить вентилятор в теплице
  @Transactional
  public void fanOff(Long greenhouseId) {

  }

  // включить тепловентилятор в теплице
  @Transactional
  public void heaterOn(Long greenhouseId) {

  }

  // выключить тепловентилятор в теплице
  @Transactional
  public void heaterOff(Long greenhouseId) {

  }

  // открыть окно на половину в теплице
  @Transactional
  public void windowHalfOpen(Long greenhouseId) {

  }

  // открыть окно полностью в теплице
  @Transactional
  public void windowAllOpen(Long greenhouseId) {

  }

  // закрыть окно в теплице
  @Transactional
  public void windowClose(Long greenhouseId) {

  }

  // включить свет
  @Transactional
  public void lightOn(Long greenhouseId) {

  }

  // выключить свет
  @Transactional
  public void lightOff(Long greenhouseId) {

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
