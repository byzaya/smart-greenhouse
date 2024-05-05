package org.greenhouse.controller.greenhouse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.greenhouse.dto.greenhouse.ConfigurationDto;
import org.greenhouse.service.greenhouse.ConfigurationService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Service
@RequestMapping("/api/v1/config")
@RequiredArgsConstructor
@Tag(name = "Конфигурация теплицы")
public class ConfigurationController {

  private final ConfigurationService configurationService;

  @Operation(
      summary = "Создание конфигурации",
      description =
          "Создание новой конфигурации теплицы пользователем - параметры, которые отвечают за состояние теплицы")
  @CrossOrigin(origins = "${cors-address}")
  @PostMapping("/add")
  public ConfigurationDto createConfiguration(@RequestBody ConfigurationDto configurationDto) {
    return configurationService.createConfiguration(configurationDto);
  }

  @Operation(
      summary = "Обновление конфигурации",
      description =
          "Обновление конфигурации теплицы - параметры, которые отвечают за состояние теплицы")
  @CrossOrigin(origins = "${cors-address}")
  @PutMapping("/{id}")
  public ConfigurationDto updateConfiguration(
      @PathVariable Long id, @RequestBody ConfigurationDto updatedConfigurationDto) {
    return configurationService.updateConfiguration(id, updatedConfigurationDto);
  }

  @Operation(
      summary = "Вкл/выкл автоуправление",
      description = "Вкл/выкл автоуправление в конфигурации теплицы")
  @CrossOrigin(origins = "${cors-address}")
  @PatchMapping("/{id}/auto-mode")
  public ConfigurationDto changeAutoMode(@PathVariable Long id, @RequestBody Boolean isAuto) {
    return configurationService.changeAutoMode(id, isAuto);
  }

  @Operation(
      summary = "Получение конфигурации",
      description = "Просмотр конфигурации теплицы по ее id")
  @CrossOrigin(origins = "${cors-address}")
  @GetMapping("/{id}")
  public ConfigurationDto getConfigurationById(@PathVariable Long id) {
    return configurationService.getConfigurationById(id);
  }
}
