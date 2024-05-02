package org.greenhouse.controller.greenhouse;

import lombok.RequiredArgsConstructor;
import org.greenhouse.dto.greenhouse.ConfigurationDto;
import org.greenhouse.service.greenhouse.ConfigurationService;
import org.springframework.stereotype.Service;
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
public class ConfigurationController {

  private final ConfigurationService configurationService;

  @PostMapping("/add")
  public ConfigurationDto createConfiguration(@RequestBody ConfigurationDto configurationDto) {
    return configurationService.createConfiguration(configurationDto);
  }

  @PutMapping("/{id}")
  public ConfigurationDto updateConfiguration(@PathVariable Long id, @RequestBody ConfigurationDto updatedConfigurationDto) {
    return configurationService.updateConfiguration(id, updatedConfigurationDto);
  }

  @PatchMapping("/{id}/auto-mode")
  public ConfigurationDto changeAutoMode(@PathVariable Long id, @RequestBody Boolean isAuto) {
    return configurationService.changeAutoMode(id, isAuto);
  }

  @GetMapping("/configurations/{id}")
  public ConfigurationDto getConfigurationById(@PathVariable Long id) {
    return configurationService.getConfigurationById(id);
  }
}
