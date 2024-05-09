package org.greenhouse.controller.greenhouse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.greenhouse.dto.output.greenhouse.ControlDto;
import org.greenhouse.service.greenhouse.ControlService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/control")
@RequiredArgsConstructor
@Tag(name = "Информация о приборах")
public class ControlController {

  private final ControlService controlService;

  @Operation(summary = "Получение инфо о приборах", description = "Получение инфо о приборах по его id")
  @CrossOrigin
  @GetMapping("/{id}")
  public ControlDto getSensorById(@PathVariable Long id) {
    return controlService.getControlById(id);
  }
}
