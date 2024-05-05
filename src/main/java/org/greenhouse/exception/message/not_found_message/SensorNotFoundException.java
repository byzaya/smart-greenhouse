package org.greenhouse.exception.message.not_found_message;

public class SensorNotFoundException extends RuntimeException {
  public SensorNotFoundException(String message) {
    super(message);
  }
}
