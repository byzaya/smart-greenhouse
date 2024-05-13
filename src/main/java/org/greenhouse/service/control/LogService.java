package org.greenhouse.service.control;

import lombok.RequiredArgsConstructor;
import org.greenhouse.dto.input.log.ReceiveLogInputDto;
import org.greenhouse.dto.input.log.SendLogInputDto;
import org.greenhouse.dto.output.log.ReceiveLogDto;
import org.greenhouse.dto.output.log.SendLogDto;
import org.greenhouse.entity.log.ReceiveLogs;
import org.greenhouse.entity.log.SendLogs;
import org.greenhouse.repository.log.ReceiveLogsRepository;
import org.greenhouse.repository.log.SendLogsRepository;
import org.greenhouse.service.ValidationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LogService {

  private final ReceiveLogsRepository receiveLogsRepository;
  private final SendLogsRepository sendLogsRepository;
  private final ValidationService validationService;

  @Transactional
  public SendLogDto sendLog(SendLogInputDto sendLogDto) {
    SendLogs sendLogs = new SendLogs();
    sendLogs.setReply(sendLogDto.reply());
    sendLogs.setCommand(sendLogDto.command());
    sendLogs.setSendTime(sendLogDto.sendTime());
    sendLogs.setTopic(validationService.getTopicOrThrow(sendLogDto.topicId()));
    sendLogsRepository.save(sendLogs);
    return SendLogDto.fromSendLogs(sendLogs);
  }

  @Transactional
  public ReceiveLogDto receiveLog(ReceiveLogInputDto receiveLogInputDto) {
    ReceiveLogs receiveLogs = new ReceiveLogs();
    receiveLogs.setMessage(receiveLogInputDto.message());
    receiveLogs.setValidity(receiveLogInputDto.validity());
    receiveLogs.setReceiveTime(receiveLogInputDto.receiveTime());
    receiveLogs.setTopic(validationService.getTopicOrThrow(receiveLogInputDto.topicId()));
    receiveLogsRepository.save(receiveLogs);
    return ReceiveLogDto.fromReceiveLogs(receiveLogs);
  }

}
