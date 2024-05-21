package org.greenhouse.service.control.auto_control;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.greenhouse.entity.greenhouse.Greenhouses;
import org.greenhouse.entity.greenhouse.SeedBeds;
import org.greenhouse.entity.sensor.Sensors;
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
     Метод должен запускаться каждые N минут (10 мин период - T, как часто приходят значения с датчиков)
                                    => 20-30 мин (задаем данные в application properties)

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

      - минимальный уровень влажности (minHumidity) от 0 до 100%
      - максимальный уровень влажности (maxHumidity) от 0 до 100%

      Далее берем список датчиков, у которых isActive=true
      Проверить данные с датчиков на валидность (что датчики не сломаны)
      Если данные не валидны, кинуть исключение по поводу неисправности датчика и поставить isActive=false

      Далее по списку активных датчиков смотрим, какие данные в логах (влажность, освещение, температура)
      и смотрим последние 5 записей (если записей в таблице больше 5)

      TODO а если вообще не приходят данные? чекать еще время в логах!!!
      TODO а если вообще все датчики сломаны

      TODO ЕСЛИ СЛОМАНЫ ВСЕ - выкл свет
      --------------Освещенность--------------
      Берем последнее значение
      Если оно не входит в диапазон, то:
       Если меньше, то вкл свет

      TODO ЕСЛИ СЛОМАНЫ ВСЕ - открыть окна
      --------------Температура--------------
      Берем первое значение по температуре из 10 последних значений (или ср знач)? и самое последнее значение
        Если последнее значение (или оба значения) попадают в диапазон, то
             Если ОБА значения (первое и последнее) попадают в диапазон, то
                - если вкл вентилятор, то выключаем его иначе
                - если окно полностью открыто, то открываем наполовину иначе
                - если окно открыто наполовину, то закрываем его
      Иначе
          Если эти значения меньше И разница между первым и последним значением меньше (?), то:
                - если вкл вентилятор, то выключаем его иначе
                - если окно полностью открыто, то открываем наполовину иначе
                - если окно открыто наполовину, то закрываем его иначе
                - если тепловентилятор не вкл, то вкл его
      Иначе
          Если эти значения больше И разница между первым и последним значением меньше (?), то:
             - если тепловентилятор вкл, то выкл его иначе
             - проверяем, закрыто ли окно, если да, открываем наполовину иначе
             - если окно открыто наполовину, открываем полностью иначе
             - если окно открыто полностью, то вкл вентилятор

      TODO ЕСЛИ СЛОМАНЫ ВСЕ - оставлять дефолтное значение
      --------------Влажность-----------------

      Тут проверяем для каждой грядки (СЕЙЧАС ВЛАЖНОСТЬ НЕ УЧИТЫВАЕТСЯ)
      Берем первое значение по влажности из 3 последних значений (или ср знач)? и самое последнее значение
        Если последнее значение (или оба значения) попадают в диапазон, то
          Если вкл полив, то выключаем
          Если выкл полив, то ничего не делаем
        Иначе
          Если выкл полив, то вкл его
          Если вкл полив, то ничего не делаем




  */

//  @Scheduled(fixedDelay = 1800000)
//  public void controlAll(Long greenhouseId) {
//    Greenhouses greenhouse = validationService.getGreenhouseOrThrow(greenhouseId);
//    List<SeedBeds> seedBedsList =
//        greenhouse.getSeedBeds().stream().filter(SeedBeds::getIsAuto).toList();
//    if (greenhouse.getConfiguration().getIsAuto() && greenhouse.getConfiguration().getIsActive()) {
//      Integer minTemperature = greenhouse.getConfiguration().getMinTemperature();
//      Integer maxTemperature = greenhouse.getConfiguration().getMaxTemperature();
//      Integer minLight = greenhouse.getConfiguration().getMinLight();
//      Integer maxLight = greenhouse.getConfiguration().getMaxLight();
//
//      List<Sensors> sensors =
//          greenhouse.getSensors().stream().filter(Sensors::getIsActive).toList();
//
//
//
//      if (seedBedsList.size() > 0) {
//        Map<Long, Integer> seedBedMinHumidity = new HashMap<>();
//        Map<Long, Integer> seedBedMaxHumidity = new HashMap<>();
//        for (int i = 0; i < seedBedsList.size(); i++) {}
//      }
//    } else if (seedBedsList.size() > 0) { // тут нужно проверить также все грядки на isAuto
//
//    } else {
//      return;
//    }
//  }
}
