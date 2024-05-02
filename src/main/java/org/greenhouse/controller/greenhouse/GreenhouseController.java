package org.greenhouse.controller.greenhouse;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.greenhouse.dto.greenhouse.GreenhouseDto;
import org.greenhouse.service.greenhouse.GreenhouseService;
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
  public GreenhouseDto createGreenhouse(@RequestBody GreenhouseDto greenhouseDto) {
    return greenhouseService.createGreenhouse(greenhouseDto);
  }

  @GetMapping("/{id}")
  public GreenhouseDto getGreenhouse(@PathVariable Long id) {
    return greenhouseService.getGreenhouse(id);
  }

  @GetMapping("/user/{userId}")
  public List<Long> getGreenhouseIdsByUserId(@PathVariable Integer userId) {
    return greenhouseService.getGreenhouseIdsByUserId(userId);
  }

  @DeleteMapping("/{id}")
  public void deleteGreenhouse(@PathVariable Long id) {
    greenhouseService.deleteGreenhouse(id);
  }
}
