package org.greenhouse.service.greenhouse;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.greenhouse.dto.greenhouse.SeedBedDto;
import org.greenhouse.entity.greenhouse.Greenhouses;
import org.greenhouse.entity.greenhouse.SeedBeds;
import org.greenhouse.exception.message.GreenhouseNotFoundException;
import org.greenhouse.exception.message.SeedBedNotFoundException;
import org.greenhouse.repository.greenhouse.GreenhousesRepository;
import org.greenhouse.repository.greenhouse.SeedBedsRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SeedBedService {

  private final GreenhousesRepository greenhouseRepository;
  private final SeedBedsRepository seedBedRepository;

  // TODO валидация
  // добавление грядки
  public SeedBedDto createSeedBed(SeedBedDto seedBedDto) {
    SeedBeds seedBeds = new SeedBeds();
    updateSeedBedsFromDto(seedBeds, seedBedDto);
    return SeedBedDto.fromSeedBeds(seedBedRepository.save(seedBeds));
  }

  // изменить конфигурацию грядки
  public SeedBedDto updateSeedBed(Long id, SeedBedDto updatedSeedBedDto) {
    SeedBeds seedBeds =
        seedBedRepository
            .findById(id)
            .orElseThrow(() -> new SeedBedNotFoundException("SeedBed not found with ID: " + id));
    updateSeedBedsFromDto(seedBeds, updatedSeedBedDto);
    return SeedBedDto.fromSeedBeds(seedBedRepository.save(seedBeds));
  }

  private void updateSeedBedsFromDto(SeedBeds seedBeds, SeedBedDto seedBedDto) {
    seedBeds.setSeedbedName(seedBedDto.seedbedName());
    seedBeds.setIsAuto(seedBedDto.isAuto());
    seedBeds.setIsActive(seedBedDto.isActive());
    seedBeds.setWateringDuration(seedBedDto.wateringDuration());
    seedBeds.setWateringFrequency(seedBedDto.wateringFrequency());
    seedBeds.setMinHumidity(seedBedDto.minHumidity());
    seedBeds.setMaxHumidity(seedBedDto.maxHumidity());
    seedBeds.setGreenhouse(
        greenhouseRepository
            .findById(seedBedDto.greenhouse().id())
            .orElseThrow(
                () ->
                    new GreenhouseNotFoundException(
                        "Greenhouse not found with ID: " + seedBedDto.greenhouse().id())));
  }

  // получение инфо о грядке
  public SeedBedDto getSeedBedById(Long id) {
    SeedBeds seedBeds =
        seedBedRepository
            .findById(id)
            .orElseThrow(() -> new SeedBedNotFoundException("SeedBed not found with ID: " + id));
    return SeedBedDto.fromSeedBeds(seedBeds);
  }

  // Получение id всех грядок по id теплицы
  public List<Long> getSeedBedIdsByGreenhouseId(Long greenhouseId) {
    Greenhouses greenhouses =
        greenhouseRepository
            .findById(greenhouseId)
            .orElseThrow(
                () ->
                    new GreenhouseNotFoundException(
                        "Greenhouse not found with ID: " + greenhouseId));
    List<SeedBeds> seedBeds = greenhouses.getSeedBeds();
    return seedBeds.stream().map(SeedBeds::getId).toList();
  }

  public void deleteSeedBedById(Long id) {
    if (!seedBedRepository.existsById(id)) {
      throw new SeedBedNotFoundException("SeedBed not found with ID: " + id);
    }
    seedBedRepository.deleteById(id);
  }
}
