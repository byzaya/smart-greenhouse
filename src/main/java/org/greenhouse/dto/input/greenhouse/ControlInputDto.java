package org.greenhouse.dto.input.greenhouse;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.greenhouse.entity.greenhouse.Control;

public record ControlInputDto(
    @JsonProperty("windowStatus") Integer windowStatus,
    @JsonProperty("lightEnabled") Boolean lightEnabled,
    @JsonProperty("fanEnabled") Boolean fanEnabled,
    @JsonProperty("heaterEnabled") Boolean heaterEnabled,
    @JsonProperty("greenhouseId") Long greenhouseId) {
  public static ControlInputDto fromControl(Control control) {
    return new ControlInputDto(
        control.getWindowStatus(),
        control.getLightEnabled(),
        control.getFanEnabled(),
        control.getHeaterEnabled(),
        control.getGreenhouse().getId());
  }
}
