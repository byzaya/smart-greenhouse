package org.greenhouse.dto.output.log;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.greenhouse.dto.output.greenhouse.GreenhouseDto;
import org.greenhouse.entity.log.Topics;

public record TopicDto(
    @JsonProperty("id") Long id,
    @JsonProperty("topicName") String topicName,
    @JsonProperty("greenhouse") GreenhouseDto greenhouseDto) {
  public static TopicDto fromTopics(Topics topics) {
    return new TopicDto(
        topics.getId(),
        topics.getTopicName(),
        GreenhouseDto.fromGreenhouse(topics.getGreenhouse()));
  }
}
