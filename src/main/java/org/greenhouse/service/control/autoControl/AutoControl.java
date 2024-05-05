package org.greenhouse.service.control.autoControl;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class AutoControl {
  // TODO сделать автоконтроль теплицы с Scheduled

  /*
     Тут прописана вся логика автоматического управления теплицей (включая грядки)
     На вход должно поступать id теплицы

     Далее проверки:
      - теплица с таким id существует
      - у теплицы есть конфигурация и isAuto - true (Configuration - isAuto)
      - у теплицы в конфигурации isActive - true (Configuration - isActive)

     Далее проверяем грядки:
      - получаем список грядок по id теплицы (List<SeedBed>)
      - для каждой грядки проверяем, что у них isAuto - true и если это не так, в итоговый список грядок не включаем

     Для грядок и теплицы выделяем параметры конфигурации:
      - минимальная температура (minTemperature) измеряется в градусах C
      - максимальная температура (maxTemperature) измеряется в градусах C
      - минимальное освещение (minLight) от 0 до 100%
      - максимальное освещение (maxLight) от 0 до 100%

      - длительность полива (wateringDuration) измеряется в минутах
      - частота полива (wateringFrequency) кол-во раз в день вкл полива грядки
      - минимальный уровень влажности (minHumidity) от 0 до 100%
      - максимальный уровень влажности (maxHumidity) от 0 до 100%

  */


  @Scheduled
  public void controlAll(Long greenhouseId) {

  }
}
