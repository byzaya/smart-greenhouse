package org.greenhouse.service.greenhouse;

import lombok.RequiredArgsConstructor;
import org.greenhouse.dto.output.greenhouse.ControlDto;
import org.greenhouse.entity.greenhouse.Control;
import org.greenhouse.service.ValidationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ControlService {

  private final ValidationService validationService;

  // получение информации о вкл/выкл устройствах
  @Transactional(readOnly = true)
  public ControlDto getControlById(Long id) {
    Control control = validationService.getControlOrThrow(id);
    return ControlDto.fromControl(control);
  }
}
