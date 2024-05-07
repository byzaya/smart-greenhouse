package org.greenhouse.exception.message.not_found_message;

public class TopicNotFoundException extends RuntimeException {
  public TopicNotFoundException(String message) {
    super(message);
  }
}
