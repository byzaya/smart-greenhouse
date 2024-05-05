package org.greenhouse.service.control.manualControl;

import lombok.RequiredArgsConstructor;
import org.greenhouse.repository.greenhouse.GreenhousesRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ManualControl {

  private final GreenhousesRepository greenhousesRepository;

  // тут нужно записать в mqtt команду, записать в логах бд и записать в control бд

  // включить вентилятор в теплице
  public void fanOn(Long greenhouseId) {

  }

  // выключить вентилятор в теплице
  public void fanOff(Long greenhouseId) {

  }

  // включить тепловентилятор в теплице
  public void heaterOn(Long greenhouseId) {

  }

  // выключить тепловентилятор в теплице
  public void heaterOff(Long greenhouseId) {

  }

  // открыть окно на половину в теплице
  public void windowHalfOpen(Long greenhouseId) {

  }

  // открыть окно полностью в теплице
  public void windowAllOpen(Long greenhouseId) {

  }

  // закрыть окно в теплице
  public void windowClose(Long greenhouseId) {

  }

  // включить свет
  public void lightOn(Long greenhouseId) {

  }

  // выключить свет
  public void lightOff(Long greenhouseId) {

  }

  // включить полив на грядке
  public void wateringOn(Long seedBedId) {

  }

  // выключить полив на грядке
  public void wateringOff(Long seedBedId) {

  }

}
