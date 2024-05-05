package org.greenhouse.exception.message.control_message;

public class FanAlreadyOffException extends RuntimeException {
  public FanAlreadyOffException(String message) {
    super(message);
  }
}
