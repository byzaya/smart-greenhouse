package org.greenhouse.controller.greenhouse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.greenhouse.dto.output.greenhouse.SeedBedDto;
import org.greenhouse.service.greenhouse.SeedBedService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/seedbed")
@RequiredArgsConstructor
@Tag(name = "Грядка")
public class SeedBedController {
  private final SeedBedService seedBedService;

  @Operation(
      summary = "Добавление грядки",
      description = "Создание новой грядки (в том числе ее конфигурации) пользователем")
  @CrossOrigin(origins = "${cors-address}")
  @PostMapping("/add")
  public SeedBedDto createSeedBed(@RequestBody SeedBedDto seedBedDto) {
    return seedBedService.createSeedBed(seedBedDto);
  }

  @Operation(
      summary = "Изменение грядки",
      description = "Изменение грядки (в том числе ее конфигурации) по ее id")
  @CrossOrigin(origins = "${cors-address}")
  @PutMapping("/update/{id}")
  public SeedBedDto updateSeedBed(
      @PathVariable Long id, @RequestBody SeedBedDto updatedSeedBedDto) {
    return seedBedService.updateSeedBed(id, updatedSeedBedDto);
  }

  @Operation(
      summary = "Получение грядки",
      description = "Просмотр информации о грядке (в том числе ее конфигурации) по ее id")
  @CrossOrigin(origins = "${cors-address}")
  @GetMapping("/{id}")
  public SeedBedDto getSeedBedById(@PathVariable Long id) {
    return seedBedService.getSeedBedById(id);
  }

  @Operation(
      summary = "Получение всех грядок теплицы",
      description = "Получение списка id грядок в теплице по ее id")
  @CrossOrigin(origins = "${cors-address}")
  @GetMapping("/greenhouses/{greenhouseId}")
  public List<Long> getSeedBedIdsByGreenhouseId(@PathVariable Long greenhouseId) {
    return seedBedService.getSeedBedIdsByGreenhouseId(greenhouseId);
  }

  @Operation(summary = "Удаление грядки", description = "Удаление грядки по ее id")
  @CrossOrigin(origins = "${cors-address}")
  @DeleteMapping("/{id}")
  public void deleteSeedBedById(@PathVariable Long id) {
    seedBedService.deleteSeedBedById(id);
  }

  @Operation(
      summary = "Вкл/выкл автоуправление",
      description = "Вкл/выкл автоуправление в конфигурации грядки")
  @CrossOrigin(origins = "${cors-address}")
  @PatchMapping("/{id}/auto-mode")
  public SeedBedDto changeAutoMode(@PathVariable Long id, @RequestBody Boolean isAuto) {
    return seedBedService.changeAutoMode(id, isAuto);
  }
}
