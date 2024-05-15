package org.greenhouse.dto.input.log;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.greenhouse.entity.log.Topics;

public record TopicInputDto(
    @JsonProperty("topicName") String topicName, @JsonProperty("greenhouseId") Long greenhouseId) {
  public static TopicInputDto fromTopics(Topics topics) {
    return new TopicInputDto(topics.getTopicName(), topics.getId());
  }
}
