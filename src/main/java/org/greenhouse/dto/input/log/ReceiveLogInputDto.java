package org.greenhouse.dto.input.log;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.sql.Timestamp;
import org.greenhouse.entity.log.ReceiveLogs;

public record ReceiveLogInputDto(
    @JsonProperty("message") String message,
    @JsonProperty("validity") Boolean validity,
    @JsonProperty("receiveTime") Timestamp receiveTime,
    @JsonProperty("topicId") Long topicId) {
  public static ReceiveLogInputDto fromReceiveLogs(ReceiveLogs receiveLogs) {
    return new ReceiveLogInputDto(
        receiveLogs.getMessage(),
        receiveLogs.getValidity(),
        receiveLogs.getReceiveTime(),
        receiveLogs.getTopic().getId());
  }
}
