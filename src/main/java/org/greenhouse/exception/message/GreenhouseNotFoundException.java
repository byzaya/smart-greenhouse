package org.greenhouse.exception.message;

public class GreenhouseNotFoundException extends RuntimeException {
  public GreenhouseNotFoundException(String message) {
    super(message);
  }
}
