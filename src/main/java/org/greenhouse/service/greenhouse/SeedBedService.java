package org.greenhouse.service.greenhouse;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.greenhouse.dto.greenhouse.SeedBedDto;
import org.greenhouse.entity.greenhouse.Greenhouses;
import org.greenhouse.entity.greenhouse.SeedBeds;
import org.greenhouse.repository.greenhouse.SeedBedsRepository;
import org.greenhouse.service.ValidationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SeedBedService {

  private final SeedBedsRepository seedBedRepository;
  private final ValidationService validationService;

  // TODO валидация
  // добавление грядки
  @Transactional
  public SeedBedDto createSeedBed(SeedBedDto seedBedDto) {
    SeedBeds seedBeds = new SeedBeds();
    updateSeedBedsFromDto(seedBeds, seedBedDto);
    return SeedBedDto.fromSeedBeds(seedBedRepository.save(seedBeds));
  }

  // изменить конфигурацию грядки
  @Transactional
  public SeedBedDto updateSeedBed(Long id, SeedBedDto updatedSeedBedDto) {
    SeedBeds seedBeds = validationService.getSeedBedOrThrow(id);
    updateSeedBedsFromDto(seedBeds, updatedSeedBedDto);
    return SeedBedDto.fromSeedBeds(seedBedRepository.save(seedBeds));
  }

  private void updateSeedBedsFromDto(SeedBeds seedBeds, SeedBedDto seedBedDto) {
    seedBeds.setSeedbedName(seedBedDto.seedbedName());
    seedBeds.setIsAuto(seedBedDto.isAuto());
    seedBeds.setWateringEnabled(seedBedDto.wateringEnabled());
    seedBeds.setWateringDuration(seedBedDto.wateringDuration());
    seedBeds.setWateringFrequency(seedBedDto.wateringFrequency());
    seedBeds.setMinHumidity(seedBedDto.minHumidity());
    seedBeds.setMaxHumidity(seedBedDto.maxHumidity());
    seedBeds.setGreenhouse(validationService.getGreenhouseOrThrow(seedBedDto.greenhouse().id()));
  }

  // получение инфо о грядке
  @Transactional(readOnly = true)
  public SeedBedDto getSeedBedById(Long id) {
    SeedBeds seedBeds = validationService.getSeedBedOrThrow(id);
    return SeedBedDto.fromSeedBeds(seedBeds);
  }

  // Получение id всех грядок по id теплицы
  @Transactional(readOnly = true)
  public List<Long> getSeedBedIdsByGreenhouseId(Long greenhouseId) {
    Greenhouses greenhouses =validationService.getGreenhouseOrThrow(greenhouseId);
    List<SeedBeds> seedBeds = greenhouses.getSeedBeds();
    return seedBeds.stream().map(SeedBeds::getId).toList();
  }

  // удаление грядки по ее id
  @Transactional
  public void deleteSeedBedById(Long id) {
    SeedBeds seedBeds = validationService.getSeedBedOrThrow(id);
    seedBedRepository.delete(seedBeds);
  }

  // переключение isAuto конфигурации
  @Transactional
  public SeedBedDto changeAutoMode(Long id, Boolean isAuto) {
    SeedBeds seedBeds = validationService.getSeedBedOrThrow(id);
    seedBeds.setIsAuto(isAuto);
    return SeedBedDto.fromSeedBeds(seedBedRepository.save(seedBeds));
  }
}
