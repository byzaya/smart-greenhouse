package org.greenhouse.service.control.auto_control;

import lombok.RequiredArgsConstructor;
import org.greenhouse.entity.greenhouse.Greenhouses;
import org.greenhouse.repository.greenhouse.GreenhousesRepository;
import org.greenhouse.repository.greenhouse.SeedBedsRepository;
import org.greenhouse.service.ValidationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AutoControl {

  private final GreenhousesRepository greenhousesRepository;
  private final SeedBedsRepository seedBedsRepository;
  private final ValidationService validationService;

  @Value("${scheduled-duration}")
  private final Integer scheduledDuration = 1800000;

  // TODO сделать автоконтроль теплицы с Scheduled

  /*
     Тут прописана вся логика автоматического управления теплицей (включая грядки)
     Метод должен запускаться каждые N минут (10 мин) - T => 20-30 мин (задаем данные в application properties)
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

      Далее берем список датчиков, у которых isActive=true
      Проверить данные с датчиков на валидность (что датчики не сломаны)
      Если данные не валидны, кинуть исключение по поводу неисправности датчика и поставить isActive=false

      Далее по списку активных датчиков смотрим, какие данные в логах (влажность, освещение, температура)
      и смотрим последние 5 записей (если записей в таблице больше 5)

      Берем первое значение по температуре из 5 последних значений (или ср знач)? и самое последнее значение
        Если последнее значение (или оба значения) попадают в диапазон, то
             Если ОБА значения (первое и последнее) попадают в диапазон, то
                - если вкл вентилятор, то выключаем его
                - если окно полностью открыто, то открываем наполовину
                - если окно открыто наполовину, то закрываем его
      Иначе
          Если эти значения больше И разница между первым и последним значением меньше (?), то:
             - проверяем, закрыто ли окно, если да, открываем наполовину
             - если окно открыто наполовину, открываем полностью
             - если окно открыто полностью, то вкл вентилятор



  */

  @Scheduled(fixedDelay = scheduledDuration)
  public void controlAll(Long greenhouseId) {
    Greenhouses greenhouse = validationService.getGreenhouseOrThrow(greenhouseId);
    if (greenhouse.getConfiguration().getIsAuto()) {

    } else { // тут нужно проверить также все грядки на isAuto
      return;
    }
  }
}
