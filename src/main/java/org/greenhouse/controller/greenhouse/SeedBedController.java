package org.greenhouse.controller.greenhouse;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.greenhouse.dto.greenhouse.SeedBedDto;
import org.greenhouse.service.greenhouse.SeedBedService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/seedbed")
@RequiredArgsConstructor
public class SeedBedController {
  private final SeedBedService seedBedService;

  @PostMapping("/add")
  public SeedBedDto createSeedBed(@RequestBody SeedBedDto seedBedDto) {
    return seedBedService.createSeedBed(seedBedDto);
  }

  @PutMapping("/update/{id}")
  public SeedBedDto updateSeedBed(@PathVariable Long id, @RequestBody SeedBedDto updatedSeedBedDto) {
    return seedBedService.updateSeedBed(id, updatedSeedBedDto);
  }

  @GetMapping("/{id}")
  public SeedBedDto getSeedBedById(@PathVariable Long id) {
    return seedBedService.getSeedBedById(id);
  }

  @GetMapping("/greenhouses/{greenhouseId}")
  public List<Long> getSeedBedIdsByGreenhouseId(@PathVariable Long greenhouseId) {
    return seedBedService.getSeedBedIdsByGreenhouseId(greenhouseId);
  }

  @DeleteMapping("/seedbeds/{id}")
  public void deleteSeedBedById(@PathVariable Long id) {
    seedBedService.deleteSeedBedById(id);
  }
}
