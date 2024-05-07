package org.greenhouse.service.control.auto_control;

import lombok.RequiredArgsConstructor;
import org.greenhouse.entity.greenhouse.Greenhouses;
import org.greenhouse.repository.greenhouse.GreenhousesRepository;
import org.greenhouse.repository.greenhouse.SeedBedsRepository;
import org.greenhouse.service.ValidationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AutoControl {

  private final GreenhousesRepository greenhousesRepository;
  private final SeedBedsRepository seedBedsRepository;
  private final ValidationService validationService;

  // TODO сделать автоконтроль теплицы с Scheduled

  /*
     Тут прописана вся логика автоматического управления теплицей (включая грядки)
     Метод должен запускаться каждые N минут (задаем данные в application properties)
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

      Проверить данные с датчиков на валидность (что датчики не сломаны)
      Если данные не очень валидны, кинуть исключение по поводу неисправности датчика и поставить isActive - false
  */

  @Scheduled
  public void controlAll(Long greenhouseId) {
    Greenhouses greenhouse = validationService.getGreenhouseOrThrow(greenhouseId);
    if (greenhouse.getConfiguration().getIsAuto()) {

    } else { // тут нужно проверить также все грядки на isAuto
      return;
    }
  }
}
