package org.greenhouse.dto.log;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import org.greenhouse.entity.log.Topics;

public record TopicDto(
    @JsonProperty("id") Long id,
    @JsonProperty("topicName") String topicName,
    @JsonProperty("sendLogs") List<SendLogDto> sendLogs,
    @JsonProperty("receiveLogs") List<ReceiveLogDto> receiveLogs
) {
  public static TopicDto fromTopics(Topics topics) {
    return new TopicDto(
        topics.getId(),
        topics.getTopicName(),
        topics.getSendLogs().stream().map(SendLogDto::fromSendLogs).toList(),
        topics.getReceiveLogs().stream().map(ReceiveLogDto::fromReceiveLogs).toList()
    );
  }
}
