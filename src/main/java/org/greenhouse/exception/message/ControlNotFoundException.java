package org.greenhouse.exception.message;

public class ControlNotFoundException extends RuntimeException {
  public ControlNotFoundException(String message) {
    super(message);
  }
}
