package org.greenhouse.exception.message.control_message;

public class LightAlreadyOffException extends RuntimeException {
  public LightAlreadyOffException(String message) {
    super(message);
  }
}
