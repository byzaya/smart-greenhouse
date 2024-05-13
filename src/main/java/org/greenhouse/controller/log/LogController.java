package org.greenhouse.controller.log;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.greenhouse.dto.input.log.ReceiveLogInputDto;
import org.greenhouse.dto.input.log.SendLogInputDto;
import org.greenhouse.dto.output.log.ReceiveLogDto;
import org.greenhouse.dto.output.log.SendLogDto;
import org.greenhouse.service.control.LogService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/log")
@RequiredArgsConstructor
@Tag(name = "Логи")
public class LogController {
  private final LogService logService;

  @Operation(
      summary = "Отправить команду на датчик",
      description = "Отправить команду на датчик - лог")
  @PostMapping("/send")
  public SendLogDto sendLog(@RequestBody SendLogInputDto sendLogDto) {
    return logService.sendLog(sendLogDto);
  }

  @Operation(
      summary = "Получить инфо с датчика",
      description = "Получить инфо с датчика - лог ")
  @PostMapping("/receive")
  public ReceiveLogDto receiveLog(@RequestBody ReceiveLogInputDto receiveLogInputDto) {
    return logService.receiveLog(receiveLogInputDto);
  }
}
