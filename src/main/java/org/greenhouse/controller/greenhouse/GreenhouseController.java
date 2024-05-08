package org.greenhouse.controller.greenhouse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.greenhouse.dto.output.greenhouse.GreenhouseDto;
import org.greenhouse.service.greenhouse.GreenhouseService;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@Tag(name = "Теплица")
public class GreenhouseController {

  private final GreenhouseService greenhouseService;

  @Operation(summary = "Добавление теплицы", description = "Создание новой теплицы пользователем")
  @CrossOrigin(origins = "${cors-address}")
  @PostMapping("/add")
  public GreenhouseDto createGreenhouse(@RequestBody GreenhouseDto greenhouseDto) {
    return greenhouseService.createGreenhouse(greenhouseDto);
  }

  @Operation(summary = "Получение теплицы", description = "Просмотр информации о теплице по ее id")
  @CrossOrigin(origins = "${cors-address}")
  @GetMapping("/{id}")
  public GreenhouseDto getGreenhouse(@PathVariable Long id) {
    return greenhouseService.getGreenhouse(id);
  }

  @Operation(
      summary = "Получение всех теплиц пользователя",
      description = "Получение списка id теплиц пользователя по его id")
  @CrossOrigin(origins = "${cors-address}")
  @GetMapping("/user/{userId}")
  public List<Long> getGreenhouseIdsByUserId(@PathVariable Integer userId) {
    return greenhouseService.getGreenhouseIdsByUserId(userId);
  }

  @Operation(summary = "Удаление теплицы", description = "Удаление теплицы по ее id")
  @CrossOrigin(origins = "${cors-address}")
  @DeleteMapping("/{id}")
  public void deleteGreenhouse(@PathVariable Long id) {
    greenhouseService.deleteGreenhouse(id);
  }
}
