package org.greenhouse.exception.message.control_message;

public class FanAlreadyOnException extends RuntimeException {
  public FanAlreadyOnException(String message) {
    super(message);
  }
}
