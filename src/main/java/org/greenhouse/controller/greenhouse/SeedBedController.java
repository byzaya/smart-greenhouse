package org.greenhouse.controller.greenhouse;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.greenhouse.dto.greenhouse.SeedBedDto;
import org.greenhouse.service.greenhouse.SeedBedService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/seedbed")
@RequiredArgsConstructor
public class SeedBedController {
  private final SeedBedService seedBedService;

  @PostMapping("/add")
  public ResponseEntity<SeedBedDto> createSeedBed(@RequestBody SeedBedDto seedBedDto) {
    SeedBedDto createdSeedBed = seedBedService.createSeedBed(seedBedDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdSeedBed);
  }

  @GetMapping("/{id}")
  public ResponseEntity<SeedBedDto> getSeedBedById(@PathVariable Long id) {
    SeedBedDto seedBedDto = seedBedService.getSeedBedById(id);
    return ResponseEntity.ok(seedBedDto);
  }

  @GetMapping("/greenhouses/{greenhouseId}")
  public ResponseEntity<List<Long>> getSeedBedIdsByGreenhouseId(@PathVariable Long greenhouseId) {
    List<Long> seedBedIds = seedBedService.getSeedBedIdsByGreenhouseId(greenhouseId);
    return ResponseEntity.ok(seedBedIds);
  }

  @DeleteMapping("/seedbeds/{id}")
  public ResponseEntity<Void> deleteSeedBedById(@PathVariable Long id) {
    seedBedService.deleteSeedBedById(id);
    return ResponseEntity.noContent().build();
  }
}
