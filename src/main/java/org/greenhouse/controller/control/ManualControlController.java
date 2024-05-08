package org.greenhouse.controller.control;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.greenhouse.service.control.manual_control.ManualControl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/manual-control")
@RequiredArgsConstructor
@Tag(name = "Ручное управление теплицей и грядками")
public class ManualControlController {

  private final ManualControl manualControl;

  @Operation(
      summary = "Вкл вентилятор",
      description = "Вкл вентилятор в теплице по ее id")
//  @CrossOrigin(origins = "${cors-address}")
  @PostMapping("/{greenhouseId}/fan/on")
  public void fanOn(@PathVariable Long greenhouseId) {
    manualControl.fanOn(greenhouseId);
  }

  @Operation(
      summary = "Выкл вентилятор",
      description = "Выкл вентилятор в теплице по ее id")
//  @CrossOrigin(origins = "${cors-address}")
  @PostMapping("/{greenhouseId}/fan/off")
  public void fanOff(@PathVariable Long greenhouseId) {
    manualControl.fanOff(greenhouseId);
  }

  @Operation(
      summary = "Вкл тепловентилятор",
      description = "Вкл тепловентилятор в теплице по ее id")
//  @CrossOrigin(origins = "${cors-address}")
  @PostMapping("/{greenhouseId}/heater/on")
  public void heaterOn(@PathVariable Long greenhouseId) {
    manualControl.heaterOn(greenhouseId);
  }

  @Operation(
      summary = "Выкл тепловентилятор",
      description = "Выкл тепловентилятор в теплице по ее id")
//  @CrossOrigin(origins = "${cors-address}")
  @PostMapping("/{greenhouseId}/heater/off")
  public void heaterOff(@PathVariable Long greenhouseId) {
    manualControl.heaterOff(greenhouseId);
  }

  @Operation(
      summary = "Открыть окно наполовину",
      description = "Открыть окно наполовину в теплице по ее id")
//  @CrossOrigin(origins = "${cors-address}")
  @PostMapping("/{greenhouseId}/window/half-open")
  public void windowHalfOpen(@PathVariable Long greenhouseId) {
    manualControl.windowHalfOpen(greenhouseId);
  }

  @Operation(
      summary = "Открыть окно полностью",
      description = "Открыть окно полностью в теплице по ее id")
//  @CrossOrigin(origins = "${cors-address}")
  @PostMapping("/{greenhouseId}/window/full-open")
  public void windowFullOpen(@PathVariable Long greenhouseId) {
    manualControl.windowFullOpen(greenhouseId);
  }

  @Operation(
      summary = "Закрыть окно",
      description = "Открыть окно наполовину в теплице по ее id")
//  @CrossOrigin(origins = "${cors-address}")
  @PostMapping("/{greenhouseId}/window/close")
  public void windowClose(@PathVariable Long greenhouseId) {
    manualControl.windowClose(greenhouseId);
  }

  @Operation(
      summary = "Вкл освещение",
      description = "Вкл освещение в теплице по ее id")
//  @CrossOrigin(origins = "${cors-address}")
  @PostMapping("/{greenhouseId}/light/on")
  public void lightOn(@PathVariable Long greenhouseId) {
    manualControl.lightOn(greenhouseId);
  }

  @Operation(
      summary = "Выкл освещение",
      description = "Выкл освещение в теплице по ее id")
//  @CrossOrigin(origins = "${cors-address}")
  @PostMapping("/{greenhouseId}/light/off")
  public void lightOff(@PathVariable Long greenhouseId) {
    manualControl.lightOff(greenhouseId);
  }

  @Operation(
      summary = "Вкл полив",
      description = "Вкл полив грядки по ее id")
//  @CrossOrigin(origins = "${cors-address}")
  @PostMapping("/seedbeds/{seedbedId}/watering/on")
  public void wateringOn(@PathVariable Long seedbedId) {
    manualControl.wateringOn(seedbedId);
  }

  @Operation(
      summary = "Выкл полив",
      description = "Выкл полив грядки по ее id")
//  @CrossOrigin(origins = "${cors-address}")
  @PostMapping("/seedbeds/{seedbedId}/watering/off")
  public void wateringOff(@PathVariable Long seedbedId) {
    manualControl.wateringOff(seedbedId);
  }
}
