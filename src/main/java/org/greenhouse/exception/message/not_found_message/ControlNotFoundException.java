package org.greenhouse.exception.message.not_found_message;

public class ControlNotFoundException extends RuntimeException {
  public ControlNotFoundException(String message) {
    super(message);
  }
}
