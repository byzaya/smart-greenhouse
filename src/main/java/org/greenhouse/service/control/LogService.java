package org.greenhouse.service.control;

import lombok.RequiredArgsConstructor;
import org.greenhouse.dto.output.log.SendLogDto;
import org.greenhouse.repository.log.ReceiveLogsRepository;
import org.greenhouse.repository.log.SendLogsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LogService {

  private final ReceiveLogsRepository receiveLogsRepository;
  private final SendLogsRepository sendLogsRepository;

  @Transactional
  public void sendLog(SendLogDto sendLogDto) {

  }
}
