package org.greenhouse.controller.greenhouse;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.greenhouse.dto.greenhouse.GreenhouseDto;
import org.greenhouse.service.greenhouse.GreenhouseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/greenhouse")
@RequiredArgsConstructor
public class GreenhouseController {

  private final GreenhouseService greenhouseService;

  @PostMapping("/add")
  public ResponseEntity<GreenhouseDto> createGreenhouse(@RequestBody GreenhouseDto greenhouseDto) {
    GreenhouseDto result = greenhouseService.createGreenhouse(greenhouseDto);
    return ResponseEntity.ok(result);
  }

  @GetMapping("/{id}")
  public ResponseEntity<GreenhouseDto> getGreenhouse(@PathVariable Long id) {
    GreenhouseDto result = greenhouseService.getGreenhouse(id);
    return ResponseEntity.ok(result);
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<List<Long>> getGreenhouseIdsByUserId(@PathVariable Integer userId) {
    List<Long> result = greenhouseService.getGreenhouseIdsByUserId(userId);
    return ResponseEntity.ok(result);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteGreenhouse(@PathVariable Long id) {
    greenhouseService.deleteGreenhouse(id);
    return ResponseEntity.noContent().build();
  }
}
