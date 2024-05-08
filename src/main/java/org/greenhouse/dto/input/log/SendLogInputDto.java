package org.greenhouse.dto.input.log;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.sql.Timestamp;
import org.greenhouse.entity.log.SendLogs;

public record SendLogInputDto(
    @JsonProperty("reply") Boolean reply,
    @JsonProperty("command") Integer command,
    @JsonProperty("sendTime") Timestamp sendTime,
    @JsonProperty("topicId") Long topicId) {
  public static SendLogInputDto fromSendLogs(SendLogs sendLogs) {
    return new SendLogInputDto(
        sendLogs.getReply(),
        sendLogs.getCommand(),
        sendLogs.getSendTime(),
        sendLogs.getTopic().getId());
  }
}
