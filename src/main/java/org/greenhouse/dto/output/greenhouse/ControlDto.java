package org.greenhouse.dto.output.greenhouse;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.greenhouse.entity.greenhouse.Control;

public record ControlDto(
    @JsonProperty("id") Long id,
    @JsonProperty("windowStatus") Integer windowStatus,
    @JsonProperty("lightEnabled") Boolean lightEnabled,
    @JsonProperty("fanEnabled") Boolean fanEnabled,
    @JsonProperty("heaterEnabled") Boolean heaterEnabled,
    @JsonProperty("greenhouse") GreenhouseDto greenhouse) {
  public static ControlDto fromControl(Control control) {
    return new ControlDto(
        control.getId(),
        control.getWindowStatus(),
        control.getLightEnabled(),
        control.getFanEnabled(),
        control.getHeaterEnabled(),
        GreenhouseDto.fromGreenhouse(control.getGreenhouse()));
  }
}
