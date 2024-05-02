package org.greenhouse.exception.message;

public class SensorTypeNotFoundException extends RuntimeException {
  public SensorTypeNotFoundException(String message) {
    super(message);
  }
}
