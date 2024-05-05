package org.greenhouse.dto.log;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.sql.Timestamp;
import org.greenhouse.entity.log.ReceiveLogs;

public record ReceiveLogDto(
    @JsonProperty("id") Long id,
    @JsonProperty("message") String message,
    @JsonProperty("validity") Boolean validity,
    @JsonProperty("receiveTime") Timestamp receiveTime,
    @JsonProperty("topic") TopicDto topic) {
  public static ReceiveLogDto fromReceiveLogs(ReceiveLogs receiveLogs) {
    return new ReceiveLogDto(
        receiveLogs.getId(),
        receiveLogs.getMessage(),
        receiveLogs.getValidity(),
        receiveLogs.getReceiveTime(),
        TopicDto.fromTopics(receiveLogs.getTopic()));
  }
}
