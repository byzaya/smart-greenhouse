package org.greenhouse.exception.message.not_found_message;

public class SensorTypeNotFoundException extends RuntimeException {
  public SensorTypeNotFoundException(String message) {
    super(message);
  }
}
