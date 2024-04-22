package org.greenhouse.exception.message;

public class SensorNotFoundException extends RuntimeException {
  public SensorNotFoundException(String message) {
    super(message);
  }
}
