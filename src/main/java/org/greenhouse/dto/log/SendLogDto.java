package org.greenhouse.dto.log;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.sql.Timestamp;
import org.greenhouse.entity.log.SendLogs;

public record SendLogDto(
    @JsonProperty("id") Long id,
    @JsonProperty("reply") Boolean reply,
    @JsonProperty("command") Integer command,
    @JsonProperty("sendTime") Timestamp sendTime,
    @JsonProperty("topic") TopicDto topic) {
  public static SendLogDto fromSendLogs(SendLogs sendLogs) {
    return new SendLogDto(
        sendLogs.getId(),
        sendLogs.getReply(),
        sendLogs.getCommand(),
        sendLogs.getSendTime(),
        TopicDto.fromTopics(sendLogs.getTopic()));
  }
}
