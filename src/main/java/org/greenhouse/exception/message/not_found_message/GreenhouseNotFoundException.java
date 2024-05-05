package org.greenhouse.exception.message.not_found_message;

public class GreenhouseNotFoundException extends RuntimeException {
  public GreenhouseNotFoundException(String message) {
    super(message);
  }
}
